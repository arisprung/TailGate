/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arisprung.tailgate;

import static com.arisprung.tailgate.gcm.CommonUtilities.SENDER_ID;
import static com.arisprung.tailgate.gcm.CommonUtilities.displayMessage;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.util.Log;

import com.arisprung.tailgate.db.TailGateMessagesDataBase;
import com.arisprung.tailgate.gcm.JsonUtil;
import com.arisprung.tailgate.gcm.ServerUtilities;
import com.arisprung.tailgate.utilities.TailGateUtility;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService
{

	@SuppressWarnings("hiding")
	private static final String TAG = "GCMIntentService";
	private static final String AUTHORITY = "com.tailgate.contentprovider";
	private static final String BASE_PATH_MESSAGES = "messages";
	private static final String BASE_PATH_LOCATION = "location";
	private String firstMessage = null;
	private CheckBoxPreference notifyCheckBox;
	long time;
	public static final Uri CONTENT_URI_MESSAGES = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_MESSAGES);
	public static final Uri CONTENT_URI_LOCATION = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_LOCATION);
	private TailGateSharedPreferences mTailgateSharedPreferences = null;

	public GCMIntentService()
	{
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId)
	{
		Log.i(TAG, "Device registered: regId = " + registrationId);
		if (mTailgateSharedPreferences == null)
			mTailgateSharedPreferences = TailGateSharedPreferences.getInstance(getApplicationContext());

		mTailgateSharedPreferences.putStringSharedPreferences(TailGateSharedPreferences.REG_ID, registrationId);

		displayMessage(context, getString(R.string.gcm_registered));
		ServerUtilities.register(context, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId)
	{
		Log.i(TAG, "Device unregistered");
		displayMessage(context, getString(R.string.gcm_unregistered));
		if (GCMRegistrar.isRegisteredOnServer(context))
		{
			ServerUtilities.unregister(context, registrationId);
		}
		else
		{
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			Log.i(TAG, "Ignoring unregister callback");
		}
	}

	@Override
	protected void onMessage(Context context, Intent intent)
	{
		Log.i(TAG, "Received message");

		if (mTailgateSharedPreferences == null)
			mTailgateSharedPreferences = TailGateSharedPreferences.getInstance(getApplicationContext());
		// String message = getString(R.string.gcm_message);
		// displayMessage(context, message);
		// notifies user

		// May return null if a EasyTracker has not yet been initialized with a
		// property ID.
		


		String strJson = intent.getStringExtra("json_message");
		JSONObject jObj = null;
		try
		{
			jObj = new JSONObject(strJson);
		}
		catch (JSONException e)
		{

			e.printStackTrace();
		}

		MessageBean message = JsonUtil.parseJsonToMessageBean(jObj);
		
//		EasyTracker easyTracker = EasyTracker.getInstance(context);
//		if (easyTracker != null)
//		{
//			// MapBuilder.createEvent().build() returns a Map of event fields and values
//			// that are set and sent with the hit.
//			easyTracker.send(MapBuilder.createEvent("backround_service", // Event category (required)
//					"OnMessage", // Event action (required)
//					"message_recieved", // Event label
//					Long.valueOf(message.getFaceID())) // Event value
//					.build());
//		}
		
		if (!message.getFaceID().equals(mTailgateSharedPreferences.getStringSharedPreferences
				(TailGateSharedPreferences.FACEBOOK_ID, "")))
		{
			TailGateUtility.addMessageDB(message,context);
			
			SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			boolean notify_checkbox_preference = mySharedPreferences.getBoolean("notification_preference", false);
			if (notify_checkbox_preference)
			{
				generateNotification(context, message);	
			}
			
		}

		if (jObj.has("json_array_location"))
		{
			parseLocationJSON(jObj);
		}

	}

	@Override
	protected void onDeletedMessages(Context context, int total)
	{
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
		//generateNotification(context, message);
	}

	@Override
	public void onError(Context context, String errorId)
	{
		Log.i(TAG, "Received error: " + errorId);
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	

	@Override
	protected boolean onRecoverableError(Context context, String errorId)
	{
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		displayMessage(context, getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, MessageBean message)
	{
//		Intent notificationIntent = new Intent(ctx, MainActivity.class);
//		PendingIntent contentIntent = PendingIntent.getActivity(ctx,
//		        10, notificationIntent,
//		        PendingIntent.FLAG_CANCEL_CURRENT);
//
//		NotificationManager nm = (NotificationManager) ctx
//		        .getSystemService(Context.NOTIFICATION_SERVICE);
//
//		Resources res = ctx.getResources();
//		Notification.Builder builder = new Notification.Builder(ctx);
//		
//		Bitmap profile = TailGateUtility.getUserPic(message.getFaceID());
//
//		builder.setContentIntent(contentIntent)
//		            .setSmallIcon(R.drawable.ic_launcher)
//		            .setLargeIcon(profile)
//		            .setTicker(res.getString(R.string.app_name))
//		            .setWhen(System.currentTimeMillis())
//		            .setAutoCancel(true)
//		            .setContentTitle(message.getUserName())
//		            .setContentText(message.getMessage());
//		Notification n = builder.build();
//
//		nm.notify(0, n);
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message.getMessage(), when);
		
		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context, MainActivity.class);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message.getMessage(), intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
		
	
	}

	private void parseLocationJSON(JSONObject jObj)
	{
		try
		{
			getContentResolver().delete(CONTENT_URI_LOCATION, null, null);
			JSONArray array = jObj.getJSONArray("json_array_location");

			for (int i = 0; i < array.length(); i++)
			{ // **line 2**
				JSONObject childJSONObject = array.getJSONObject(i);
				String faceID = childJSONObject.getString("faceID");
				String logn = childJSONObject.getString("lognitude");
				String lati = childJSONObject.getString("latitude");
				String name = childJSONObject.getString("user");

				ContentValues contentValues = new ContentValues();

				contentValues.put(TailGateMessagesDataBase.COLUMN_LOCATION_FACE_ID, faceID);
				contentValues.put(TailGateMessagesDataBase.COLUMN_LOCATION_FACE_NAME, name);
				contentValues.put(TailGateMessagesDataBase.COLUMN_LONGNITUDE, logn);
				contentValues.put(TailGateMessagesDataBase.COLUMN_LANITUDE, lati);
				
				getContentResolver().insert(CONTENT_URI_LOCATION, contentValues);

			}
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
