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

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;

import com.arisprung.tailgate.R;
import com.arisprung.tailgate.db.TailGateMessagesDataBase;
import com.arisprung.tailgate.gcm.JsonUtil;
import com.arisprung.tailgate.gcm.ServerUtilities;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

import static com.arisprung.tailgate.gcm.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.arisprung.tailgate.gcm.CommonUtilities.EXTRA_MESSAGE;
import static com.arisprung.tailgate.gcm.CommonUtilities.SENDER_ID;
import static com.arisprung.tailgate.gcm.CommonUtilities.displayMessage;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService
{

	@SuppressWarnings("hiding")
	private static final String TAG = "GCMIntentService";
	private static final String AUTHORITY = "com.tailgate.contentprovider";
	private static final String BASE_PATH = "messages";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	private TailGateSharedPreferences mTailgateSharedPreferences = null;

	public GCMIntentService()
	{
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId)
	{
		Log.i(TAG, "Device registered: regId = " + registrationId);
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

		String strJson = intent.getStringExtra("json_message");
		JSONObject jObj = null;
		try
		{
			jObj = new JSONObject(strJson);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MessageBean message = JsonUtil.parseJsonToMessageBean(jObj);

		addMessageDB(message);
		if (!message.getFaceID().equals(mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_ID, "")))
		{
			generateNotification(context, message.getMessage());
		}
	}

	@Override
	protected void onDeletedMessages(Context context, int total)
	{
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	@Override
	public void onError(Context context, String errorId)
	{
		Log.i(TAG, "Received error: " + errorId);
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	private void addMessageDB(final MessageBean message)
	{
		long currentTime = System.currentTimeMillis();

		try
		{
			ContentValues contentValues = new ContentValues();
			contentValues.put("message_date", currentTime);
			contentValues.put("message_face_id", message.getFaceID());
			contentValues.put("message", message.getMessage());
			contentValues.put("message_face_name", message.getUserName());

			getContentResolver().insert(CONTENT_URI, contentValues);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return;

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
	private static void generateNotification(Context context, String message)
	{
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context, MainActivity.class);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
	}

}
