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
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		UiSettings settings = getMap().getUiSettings();
		settings.setAllGesturesEnabled(true);
		settings.setMyLocationButtonEnabled(true);
		LoadMarkersAsyncTask loadmarkers = new LoadMarkersAsyncTask();
		loadmarkers.execute();

	}

	private void initMap()
	{

		Cursor curs = null;
		try
		{
			String[] projection = new String[] { TailGateMessagesDataBase.COLUMN_LOCATION_FACE_ID,TailGateMessagesDataBase.COLUMN_LOCATION_FACE_NAME, TailGateMessagesDataBase.COLUMN_LANITUDE,
					TailGateMessagesDataBase.COLUMN_LONGNITUDE };
			curs = getActivity().getContentResolver().query(TailGateMessagesContentProvider.CONTENT_URI_LOCATION, projection, null, null, null);
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
			curs.close();
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
		imageURL = "http://graph.facebook.com/" + userID + "/picture?type=small";
		try
		{
			bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageURL).getContent());
			halfBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);

		}
		catch (Exception e)
		{
			Log.d("TAG", "Loading Picture FAILED");
			e.printStackTrace();
		}
		return halfBitmap;
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
			for (int i = 0; i < markerArray.size(); i++)
			{

				Marker mark = getMap().addMarker(markerArray.get(i));
				mark.showInfoWindow();

			}
			Location loc = LocationUtilTailGate.getUserLocation(getActivity());
	
				
			CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(loc.getLatitude(), loc.getLongitude()));
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(7);

			getMap().moveCamera(center);
			getMap().animateCamera(zoom);
			super.onPostExecute(result);

		}

	}
}