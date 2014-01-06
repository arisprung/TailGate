package com.arisprung.tailgate.fragments;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arisprung.tailgate.db.TailGateMessagesContentProvider;
import com.arisprung.tailgate.db.TailGateMessagesDataBase;
import com.arisprung.tailgate.location.LocationUtilTailGate;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapUserFragment extends SupportMapFragment
{

	private ArrayList<MarkerOptions> markerArray;

	public MapUserFragment()
	{
		super();
	}

	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2)
	{
		View v = super.onCreateView(arg0, arg1, arg2);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		
		super.onActivityCreated(savedInstanceState);
		UiSettings settings = getMap().getUiSettings();
		settings.setAllGesturesEnabled(true);
		settings.setMyLocationButtonEnabled(true);
		
		if(getMap()!= null)
		{
			LoadMarkersAsyncTask loadmarkers = new LoadMarkersAsyncTask();
			loadmarkers.execute();

		}
		else
		{
			Log.e("MapFragment", "getMap() is Null!!!!");
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
	
	}

	private void initMap()
	{

		Cursor curs = null;
		try
		{
			String[] projection = new String[] { TailGateMessagesDataBase.COLUMN_LOCATION_FACE_ID,TailGateMessagesDataBase.COLUMN_LOCATION_FACE_NAME, TailGateMessagesDataBase.COLUMN_LANITUDE,
					TailGateMessagesDataBase.COLUMN_LONGNITUDE };
			curs = getActivity().getApplicationContext().getContentResolver().query(TailGateMessagesContentProvider.CONTENT_URI_LOCATION, projection, null, null, null);
			int iCount = curs.getCount();

			markerArray = new ArrayList<MarkerOptions>();

			while (curs.moveToNext())
			{
				String faceId = curs.getString(0);
				String name = curs.getString(1);
				String strLant = curs.getString(2);
				String strLong = curs.getString(3);

				MarkerOptions marker = new MarkerOptions().title(name).position(new LatLng(Double.valueOf(strLant), Double.valueOf(strLong))).icon(
						BitmapDescriptorFactory.fromBitmap(getUserPic(faceId)));
				markerArray.add(marker);

			}
			
		}

		catch (Exception e)
		{
			if(curs != null)
			{
				curs.close();	
			}
			
			Log.e("MapUserFragment", "error in Location Content Provider " + e.toString());
			e.printStackTrace();

		}
		curs.close();

		// getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(lat,16));

	}

	/**
	 * Function loads the users facebook profile pic
	 * 
	 * @param userID
	 */
	public Bitmap getUserPic(String userID)
	{
		String imageURL;
		Bitmap bitmap = null;
		Bitmap halfBitmap = null;
		Log.d("MapUserFragment", "Loading Picture");
		imageURL = "http://graph.facebook.com/" + userID + "/picture?type=normal";
		try
		{
			bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageURL).getContent());
			//halfBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);

		}
		catch (Exception e)
		{
			Log.d("TAG", "Loading Picture FAILED");
			e.printStackTrace();
		}
		return bitmap;
	}

	private class LoadMarkersAsyncTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params)
		{
			initMap();
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			if(getMap()!=null)
			{
			for (int i = 0; i < markerArray.size(); i++)
			{
				
					
				 getMap().addMarker(markerArray.get(i));
				//mark.showInfoWindow();

			}
			Location loc = LocationUtilTailGate.getUserLocation(getActivity().getApplicationContext());
	
			Toast.makeText(getActivity(), "Press on profile picture to see name",Toast.LENGTH_SHORT).show();				
			CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(loc.getLatitude(), loc.getLongitude()));
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(7);

			getMap().moveCamera(center);
			getMap().animateCamera(zoom);
			}
			
			super.onPostExecute(result);

		}

	}
}