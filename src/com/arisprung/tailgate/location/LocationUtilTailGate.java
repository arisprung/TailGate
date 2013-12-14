package com.arisprung.tailgate.location;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

import android.content.Context;
import android.location.Location;

public class LocationUtilTailGate
{

	public static Location getUserLocation(Context context)
	{

		LocationLibrary.forceLocationUpdate(context);
		LocationInfo location = new LocationInfo(context);
		float lat = location.lastLat;
		float longin = location.lastLong;


		Location targetLocation = new Location("");//provider name is unecessary
	    targetLocation.setLatitude(lat);//your coords of course
	    targetLocation.setLongitude(longin);
		return targetLocation;

	}

}
