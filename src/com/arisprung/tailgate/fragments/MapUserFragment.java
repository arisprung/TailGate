package com.arisprung.tailgate.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arisprung.tailgate.R;
import com.arisprung.tailgate.db.TailGateMessagesContentProvider;
import com.arisprung.tailgate.db.TailGateMessagesDataBase;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapUserFragment extends SupportMapFragment
{
	private GoogleMap googleMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);

		return inflater.inflate(R.layout.map_fragment_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{

		super.onActivityCreated(savedInstanceState);

		googleMap = getMap();
		if (googleMap != null)
		{

			try
			{

//				Cursor curs = null;
//				try
//				{
//					String[] projection = new String[] { TailGateMessagesDataBase.COLUMN_LOCATION_FACE_ID, TailGateMessagesDataBase.COLUMN_LANITUDE,
//							TailGateMessagesDataBase.COLUMN_LONGNITUDE };
//					curs = getActivity().getContentResolver().query(TailGateMessagesContentProvider.CONTENT_URI_LOCATION, projection, null, null,
//							null);
//					int iCount = curs.getCount();
//
//					while (curs.moveToNext())
//					{
//						String faceId = curs.getString(0);
//						String strLant = curs.getString(1);
//						String strLong = curs.getString(2);
//					}
//				}
//
//				catch (Exception e)
//				{
//					curs.close();
//					Log.e("MapUserFragment", "error in Location Content Provider " + e.toString());
//					e.printStackTrace();
//
//				}
//				curs.close();
				
	
				// create marker
				MarkerOptions marker = new MarkerOptions().position( new LatLng(31.76774787902832, 35.213191986083984)).title("Hello Maps ");

				googleMap.addMarker(marker);

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

}
