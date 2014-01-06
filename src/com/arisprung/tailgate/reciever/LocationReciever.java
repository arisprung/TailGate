package com.arisprung.tailgate.reciever;

import com.arisprung.tailgate.fragments.SendMessageAsyncTaskLoader;
import com.arisprung.tailgate.gcm.ServerUtilities;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;




public class LocationReciever extends BroadcastReceiver
{
	Thread readthread;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		
		
		final LocationInfo locationInfo = (LocationInfo) intent.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
		double latitude = locationInfo.lastLat;
		double longitude = locationInfo.lastLong;
		
		Log.e("LocationReciever", "*****************************************************************");
		Log.e("LocationReciever", "********************************** GOT LOCATION!!! **************");
		Log.e("LocationReciever", "*****************************************************************");
		
		
		Toast.makeText(context, "Location Changed" + "Lat : " + latitude + " Long :" + longitude, Toast.LENGTH_LONG).show();
		
		
		SendMessageAsyncTaskLoader loadAsyncTask = new SendMessageAsyncTaskLoader(context);
		loadAsyncTask.execute("-1");
//		final LocationInfo locationInfo = (LocationInfo) intent.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
//		Location location = LocationUtilTailGate.getUserLocation(context);
//		double latitude = location.getLatitude();
//		double longitude = location.getLongitude();



//		//Toast.makeText(context, "Location Changed" + "Lat : " + latitude + " Long :" + longitude, Toast.LENGTH_LONG).show();
//		String strLatitude = String.valueOf(latitude);
//		String strLongitude = String.valueOf(longitude);

//		readthread = new Thread(new Runnable() {
//			public void run()
//			{
//				try
//				{
//					String strString = GCMRegistrar.getRegistrationId(rContext);
//					if (strString != null)
//					{
//						ServerUtilities.register(rContext, strString);
//					}
//
//				}
//				catch (Exception e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//
//		readthread.start();
//
//	}
	}
}
