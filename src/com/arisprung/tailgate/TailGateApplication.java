package com.arisprung.tailgate;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

import android.app.AlarmManager;
import android.app.Application;

public class TailGateApplication extends Application
{

	
	@Override
	public void onCreate()
	{
		
		super.onCreate();
		LocationLibrary.showDebugOutput(false);
		LocationLibrary.initialiseLibrary(getBaseContext(),(60000)*(15),(int)AlarmManager.INTERVAL_HOUR,false, "com.arisprung.tailgate");
	}
}
