package com.arisprung.tailgate;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

import android.app.Application;

public class TailGateApplication extends Application
{

	
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		LocationLibrary.initialiseLibrary(getBaseContext(), "com.arisprung.tailgate	");
	}
}
