package com.arisprung.tailgate.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arisprung.tailgate.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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

				googleMap.addMarker(new MarkerOptions().position(new LatLng(27.174840, 78.042500)).title("Hello World")
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_drawer)));

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}


}
