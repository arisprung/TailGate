//package com.arisprung.tailgate.reciever;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.location.Location;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.arisprung.tailgate.gcm.ServerUtilities;
//import com.arisprung.tailgate.location.LocationUtilTailGate;
//import com.google.android.gcm.GCMRegistrar;
//import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
//import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;
//
//
//public class LocationReciever extends BroadcastReceiver
//{
//	Thread readthread;
//
//	@Override
//	public void onReceive(Context context, Intent intent)
//	{
//		
//		final Context rContext = context;
////		final LocationInfo locationInfo = (LocationInfo) intent.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
////		Location location = LocationUtilTailGate.getUserLocation(context);
////		double latitude = location.getLatitude();
////		double longitude = location.getLongitude();
//
//		Log.e("LocationReciever", "*****************************************************************");
//		Log.e("LocationReciever", "********************************** GOT LOCATION!!! **************");
//		Log.e("LocationReciever", "*****************************************************************");
//
////		//Toast.makeText(context, "Location Changed" + "Lat : " + latitude + " Long :" + longitude, Toast.LENGTH_LONG).show();
////		String strLatitude = String.valueOf(latitude);
////		String strLongitude = String.valueOf(longitude);
//
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
//
//}
