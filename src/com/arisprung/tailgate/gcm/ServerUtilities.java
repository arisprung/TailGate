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
package com.arisprung.tailgate.gcm;

import static com.arisprung.tailgate.gcm.CommonUtilities.SERVER_URL;
import static com.arisprung.tailgate.gcm.CommonUtilities.TAG;
import static com.arisprung.tailgate.gcm.CommonUtilities.displayMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.arisprung.tailgate.R;
import com.arisprung.tailgate.TailGateSharedPreferences;
import com.arisprung.tailgate.facebook.FacebookUser;
import com.arisprung.tailgate.location.LocationUtilTailGate;
import com.google.android.gcm.GCMRegistrar;

/**
 * Helper class used to communicate with the demo server.
 */
public final class ServerUtilities
{

	private static final int MAX_ATTEMPTS = 5;
	private static final int BACKOFF_MILLI_SECONDS = 2000;
	private static final Random random = new Random();
	private static FacebookUser mUser;
	private static TailGateSharedPreferences mTailgateSharedPreferences = null;

	/**
	 * Register this account/device pair within the server.
	 * 
	 * @return whether the registration succeeded or not.
	 */
	public static boolean register(final Context context, final String regId)
	{
		
		if (mTailgateSharedPreferences == null)
			mTailgateSharedPreferences = TailGateSharedPreferences.getInstance(context);
		Log.i(TAG, "registering device (regId = " + regId + ")");
		String serverUrl = SERVER_URL + "/register";
		Map<String, String> params = new HashMap<String, String>();
		
		String strFirstName =  mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_FIRST_NAME, "");
		String strLastName =  mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_LAST_NAME, "");
		params.put("regId", regId);
		params.put("faceID", mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_ID, ""));
		params.put("location",mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_LOCATION, ""));
		params.put("usename", strFirstName+ " "+strLastName);
		params.put("email",mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_EMAIL, ""));
		params.put("team",mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.SELECTED_TEAM, ""));

		
		Location loc = LocationUtilTailGate.getUserLocation(context);

		String strLat = String.valueOf(loc.getLatitude());
		String strLong = String.valueOf(loc.getLongitude());
		params.put("longnitude",strLong);
		params.put("latitude",strLat);
		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		
		
		// Once GCM returns a registration id, we need to register it in the
		// demo server. As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++)
		{
			Log.d(TAG, "Attempt #" + i + " to register");
			try
			{
				displayMessage(context, context.getString(R.string.server_registering, i, MAX_ATTEMPTS));
				post(serverUrl, params);
				GCMRegistrar.setRegisteredOnServer(context, true);
				String message = context.getString(R.string.server_registered);
				CommonUtilities.displayMessage(context, message);
				return true;
			}
			catch (IOException e)
			{
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i, e);
				if (i == MAX_ATTEMPTS)
				{
					break;
				}
				try
				{
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				}
				catch (InterruptedException e1)
				{
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return false;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		String message = context.getString(R.string.server_register_error, MAX_ATTEMPTS);
		CommonUtilities.displayMessage(context, message);
		return false;
	}

	/**
	 * Unregister this account/device pair within the server.
	 */
	public static void unregister(final Context context, final String regId)
	{
		Log.i(TAG, "unregistering device (regId = " + regId + ")");
		String serverUrl = SERVER_URL + "/unregister";
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);
		try
		{
			post(serverUrl, params);
			GCMRegistrar.setRegisteredOnServer(context, false);
			String message = context.getString(R.string.server_unregistered);
			CommonUtilities.displayMessage(context, message);
		}
		catch (IOException e)
		{
			// At this point the device is unregistered from GCM, but still
			// registered in the server.
			// We could try to unregister again, but it is not necessary:
			// if the server tries to send a message to the device, it will get
			// a "NotRegistered" error message and should unregister the device.
			String message = context.getString(R.string.server_unregister_error, e.getMessage());
			CommonUtilities.displayMessage(context, message);
		}
	}
	
	

	/**
	 * Unregister this account/device pair within the server.
	 */
	public static void sendMessageToServer(final Context context, final String message)
	{
		if (mTailgateSharedPreferences == null)
			mTailgateSharedPreferences = TailGateSharedPreferences.getInstance(context);
		Log.i(TAG, "Sending message to device (message = " + message + ")");
		String serverUrl = SERVER_URL + "/message";
		Map<String, String> params = new HashMap<String, String>();
		String strFaceId = mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_ID, "");
		String strFirstName = mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_FIRST_NAME, "");
		String strLastName = mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_LAST_NAME, "");
		String strTeam = mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.SELECTED_TEAM, "");
		
		
		if(strFaceId.equals("") )
		{
			Toast.makeText(context,  " Message wasnt sent - Please login thru Facebook in order to send message", Toast.LENGTH_SHORT).show();
			return;
		}
		params.put("location",mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_LOCATION, ""));
		params.put("usename", mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_NAME, ""));
		params.put("email",mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_EMAIL, ""));
		params.put("message", message);
		params.put("faceID", strFaceId);
		params.put("face_first_name", strFirstName);
		params.put("face_last_name", strLastName);
		params.put("team", strTeam);
		params.put("regId", mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.REG_ID,"NO reg ID"));
		params.put("usename", strFirstName+ " "+strLastName);
		Location loc = LocationUtilTailGate.getUserLocation(context);

		String strLat = String.valueOf(loc.getLatitude());
		String strLong = String.valueOf(loc.getLongitude());
		params.put("longnitude",strLong);
		params.put("latitude",strLat);	
		
//		params.put("longnitude","1");
//		params.put("latitude","1");	
		
		try
		{
			post(serverUrl, params);
			//GCMRegistrar.setRegisteredOnServer(context, false);
			//String message = context.getString(R.string.server_unregistered);
			//CommonUtilities.displayMessage(context, message);
		}
		catch (IOException e)
		{
			// At this point the device is unregistered from GCM, but still
			// registered in the server.
			// We could try to unregister again, but it is not necessary:
			// if the server tries to send a message to the device, it will get
			// a "NotRegistered" error message and should unregister the device.
			//String message = context.getString(R.string.server_unregister_error, e.getMessage());
			//CommonUtilities.displayMessage(context, message);
		}
	}

	/**
	 * Issue a POST request to the server.
	 * 
	 * @param endpoint POST address.
	 * @param params request parameters.
	 * 
	 * @throws IOException propagated from POST.
	 */
	private static void post(String endpoint, Map<String, String> params) throws IOException
	{
		URL url;
		try
		{
			url = new URL(endpoint);
		}
		catch (MalformedURLException e)
		{
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext())
		{
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
			if (iterator.hasNext())
			{
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		Log.v(TAG, "Posting '" + body + "' to " + url);
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try
		{
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200)
			{
				throw new IOException("Post failed with error code " + status);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (conn != null)
			{
				conn.disconnect();
			}
		}
	}
	
	
	


}
