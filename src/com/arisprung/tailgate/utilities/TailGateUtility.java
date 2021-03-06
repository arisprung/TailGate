package com.arisprung.tailgate.utilities;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import com.arisprung.tailgate.MessageBean;
import com.arisprung.tailgate.TailGateSharedPreferences;
import com.arisprung.tailgate.db.TailGateMessagesContentProvider;
import com.arisprung.tailgate.db.TailGateMessagesDataBase;
import com.arisprung.tailgate.fragments.SendMessageAsyncTaskLoader;
import com.facebook.Session;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TailGateUtility
{
	private static final String AUTHORITY = "com.tailgate.contentprovider";
	private static final String BASE_PATH_MESSAGES = "messages";
	private static final String BASE_PATH_LOCATION = "location";
	public static final Uri CONTENT_URI_MESSAGES = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_MESSAGES);

	/**
	 * Function loads the users facebook profile pic
	 * 
	 * @param userID
	 */
	public static Bitmap getUserPic(String userID)
	{
		String imageURL;
		Bitmap bitmap = null;
		// Log.d(TAG, "Loading Picture");
		imageURL = "http://graph.facebook.com/" + userID + "/picture?type=small";
		try
		{
			bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageURL).getContent());
		}
		catch (Exception e)
		{
			// Log.d("TAG", "Loading Picture FAILED");
			e.printStackTrace();
		}
		return bitmap;
	}

	public static void showAuthenticatedDialog(Context context, String title, String text)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(text);

		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				// Toast.makeText(getActivity(), "Close is clicked", Toast.LENGTH_LONG).show();

			}
		});
		builder.show(); // To show the AlertDialog
	}

	public static void buildAlertMessageNoGps(final Context context)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?").setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id)
					{
						context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id)
					{
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	public static void showMessageDialog(Context context, String title, String strText)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(strText);

		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				// Toast.makeText(getActivity(), "Close is clicked", Toast.LENGTH_LONG).show();

			}
		});
		builder.show(); // To show the AlertDialog
	}

	public static void initMessageListView(TextView text, LinearLayout linearlayout, TailGateSharedPreferences prefs)
	{
		String strText = "";
		text.setVisibility(View.VISIBLE);
		String strFaceID = prefs.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_ID, "");
		String strTeam = prefs.getStringSharedPreferences(TailGateSharedPreferences.SELECTED_TEAM, "");

		if (strFaceID.equals("") || strTeam.equals(""))
		{

			if (strFaceID.equals("") && strTeam.equals(""))
			{
				strText = "Please Login thru FaceBook above and select a team";
			}
			else if (strFaceID.equals(""))
			{
				strText = "Please Login thru FaceBook above";
			}
			else if (strTeam.equals(""))
			{
				strText = "Please select a team to recieve messages";
			}

			linearlayout.setVisibility(View.GONE);

		}
		else
		{
			strText = "Waiting for messages....";
		}
		text.setText(strText);

	}

	public static void sendMessageToServer(Context context, EditText edittext, TailGateSharedPreferences pref)
	{
		if (!edittext.getText().toString().equals("") && (edittext.getText().length() < 140))
		{
			String strTeam = pref.getStringSharedPreferences(TailGateSharedPreferences.SELECTED_TEAM, "");
			String strFacID = pref.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_ID, "");
			String strFacFirstName = pref.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_FIRST_NAME, "");
			String strFaceLastName = pref.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_LAST_NAME, "");
			if (strTeam.equals("") || strFacID.equals(""))
			{
				if (strTeam.equals(""))
				{
					TailGateUtility.showMessageDialog(context, "Select Team", "Please select a team in order to send messages.");
				}
				else if (strFacID.equals(""))
				{
					TailGateUtility.showMessageDialog(context, "Please Login", "First login in order to send messages.");
				}

			}
			else
			{

				Session ses = Session.getActiveSession();
				if (ses.isOpened())
				{
					if (haveNetworkConnection(context))
					{

						// May return null if a EasyTracker has not yet been initialized with a
						// property ID.
						EasyTracker easyTracker = EasyTracker.getInstance(context);
						if (easyTracker != null)
						{
							// MapBuilder.createEvent().build() returns a Map of event fields and values
							// that are set and sent with the hit.
							easyTracker.send(MapBuilder.createEvent("Message_Sent", // Event category (required)
									strTeam, // Event action (required)
									edittext.getText().toString(), // Event label
									null) // Event value
									.build());
						}

						MessageBean message = new MessageBean(strFacID, edittext.getText().toString(), strFacFirstName + " " + strFaceLastName,strTeam);
						addMessageDB(message, context);
						SendMessageAsyncTaskLoader loadAsyncTask = new SendMessageAsyncTaskLoader(context);
						loadAsyncTask.execute(edittext.getText().toString());
						edittext.setText("");
						InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
					}
					else
					{
						showAuthenticatedDialog(context, "No Connection", "Please check connection and try agian");
					}

				}
				else
				{
					TailGateUtility.showMessageDialog(context, "Please Login", "First login in order to send messages.");

				}

			}

		}
		else
		{
			TailGateUtility.showMessageDialog(context, "Empty Text", "Text cant be empty.");
		}

	}

	private static boolean haveNetworkConnection(Context context)
	{
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo)
		{
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	public static void addMessageDB(final MessageBean message, Context context)
	{
		long currentTime = System.currentTimeMillis();

		try
		{
			ContentValues contentValues = new ContentValues();
			contentValues.put("message_date", currentTime);
			contentValues.put("message_face_id", message.getFaceID());
			contentValues.put("message", message.getMessage());
			contentValues.put("message_face_name", message.getUserName());
			contentValues.put("message_team", message.getTeam());

			context.getContentResolver().insert(CONTENT_URI_MESSAGES, contentValues);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public static LatLng getLatLongFromDB(String id,Context context)
	{
		
		Cursor curs = null;
		String lan = null ;
		String longni = null;

		String[] projection = new String[] { TailGateMessagesDataBase.COLUMN_LANITUDE,
				TailGateMessagesDataBase.COLUMN_LONGNITUDE };
		String where  = TailGateMessagesDataBase.COLUMN_LOCATION_FACE_ID + " = '" + id + "'";
		curs = context.getApplicationContext().getContentResolver()
				.query(TailGateMessagesContentProvider.CONTENT_URI_LOCATION, projection, where,null , null);
		int iCount = curs.getCount();

		
		while (curs.moveToNext())
		{
			 lan = curs.getString(0);
			 longni = curs.getString(1);
		

			

		}
		if(lan != null && longni!= null)
		{
			LatLng latlong = new LatLng(Double.valueOf(lan),Double.valueOf(longni));
			return latlong;
		}
		return null;
		
		
	
		
	}
	
	public static Bitmap drawWhiteFrame(Bitmap bitmap)
	{

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int radius = Math.min(h / 2, w / 2);
		Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Config.ARGB_8888);

		Paint p = new Paint();
		p.setAntiAlias(true);

		Canvas c = new Canvas(output);
		c.drawARGB(0, 0, 0, 0);
		p.setStyle(Style.FILL);

		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

		p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		c.drawBitmap(bitmap, 4, 4, p);
		p.setXfermode(null);
		p.setStyle(Style.STROKE);
		p.setColor(Color.WHITE);
		p.setStrokeWidth(3);
		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

		return output;

	}
}
