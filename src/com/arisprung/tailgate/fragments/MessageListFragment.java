package com.arisprung.tailgate.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arisprung.tailgate.R;
import com.arisprung.tailgate.TailGateSharedPreferences;
import com.arisprung.tailgate.adapter.CustomCursorAdapter;
import com.arisprung.tailgate.db.TailGateMessagesDataBase;
import com.arisprung.tailgate.utilities.TailGateUtility;
import com.google.android.gms.maps.model.LatLng;

public class MessageListFragment extends Fragment implements LoaderCallbacks<Cursor>
{

	// public static MessageListAdapter adapter;
	private static ListView mListView;

	private static final String TAG = MessageListFragment.class.toString();

	private static CustomCursorAdapter mCusrorAdapter;
	private int LOADER_ID = 1000;
	private static final String AUTHORITY = "com.tailgate.contentprovider";
	private static final String BASE_PATH = "messages";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	private Button sendButton;
	private EditText editText;
	private TextView emptyText;
	private LinearLayout sendLinearLayout;

	private TailGateSharedPreferences mTailgateSharedPreferences = null;

	private boolean bCursorEmpty;
	private static Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// setHasOptionsMenu(true);
		// mMessagesDataSource = new TailGateMessagesDataBase(getActivity());
		if (container == null)
		{
			return null;
		}
		View view = inflater.inflate(R.layout.list_view, container, false);
		mListView = (ListView) view.findViewById(R.id.list);
		sendButton = (Button) view.findViewById(R.id.button);
		editText = (EditText) view.findViewById(R.id.editText);
		emptyText = (TextView) view.findViewById(R.id.empty_text);
		sendLinearLayout = (LinearLayout) view.findViewById(R.id.send_ll);

		return view;

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();

		if (mTailgateSharedPreferences == null)
			mTailgateSharedPreferences = TailGateSharedPreferences.getInstance(getActivity().getApplicationContext());

		// String strTeam = mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.SELECTED_TEAM, "");
		// String strFacID = mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_ID, "");
		// if (strTeam.equals("") || strFacID.equals(""))
		// {
		//
		// if (strFacID.equals(""))
		// {
		//
		// TailGateUtility.showAuthenticatedDialog(getActivity(), "Please Login", "First login in order to see messages.");
		//
		// }
		// else if (strTeam.equals(""))
		// {
		// TailGateUtility.showAuthenticatedDialog(getActivity(), "Select Team", "Please select a team in order to see messages.");
		// }
		// }

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		getLoaderManager().initLoader(LOADER_ID, null, this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long l)
			{
				// Log.i(TAG, "Item Clicked");
				
				LocationManager manager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
				if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
				{
					TailGateUtility.buildAlertMessageNoGps(getActivity());
					return;
				}
				MapUserFragment mapFrag = new MapUserFragment();
				Bundle bundle = new Bundle();
				Cursor cur = (Cursor) mCusrorAdapter.getItem(position);
				cur.moveToPosition(position);
				String faceId = cur.getString(cur.getColumnIndexOrThrow("message_face_id"));
				
				LatLng latLong = TailGateUtility.getLatLongFromDB(faceId, getActivity().getApplicationContext());
				if(latLong != null)
				{
					bundle.putDouble("message_latitude", latLong.latitude);
					bundle.putDouble("message_longnitude", latLong.longitude);
					mapFrag.setArguments(bundle);

					FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
					FragmentTransaction ft = fragmentManager.beginTransaction().replace(R.id.frame_container, mapFrag, "map");
					ft.addToBackStack("map");
					ft.commit();
				}
				else
				{
					Toast.makeText(getActivity(), "Cant find Location", Toast.LENGTH_SHORT).show();
				}
			

				// update selected item and title, then close the drawer
				// mDrawerList.setItemChecked(position, true);
				// mDrawerList.setSelection(position);
				// setTitle(navMenuTitles[position]);
				// mDrawerLayout.closeDrawer(mDrawerList);
			}
		});
		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				TailGateUtility.sendMessageToServer(getActivity(), editText, mTailgateSharedPreferences);
				mListView.setSelection(0);
			}
		});
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		getActivity().getApplicationContext().registerReceiver(changeViewReciever, new IntentFilter("change_view"));
		mListView.setSelection(0);
	}

	@Override
	public void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();
		if (changeViewReciever == null)
		{
			Log.i(TAG, "Do not unregister receiver as it was never registered");
		}
		else
		{
			getActivity().getApplicationContext().unregisterReceiver(changeViewReciever);
			changeViewReciever = null;
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		// Create the Cursor that will take care of the data being displayed
		Log.d("TAG", "onCreateLoader...");
		CursorLoader cursurLoader = new CursorLoader(getActivity().getApplicationContext(), CONTENT_URI, null, null, null,
				TailGateMessagesDataBase.COLUMN_MESSAGE_DATE + " DESC");

		return cursurLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor)
	{
		// Now Bind the data to the View
		Log.d("TAG", "onLoadFinished...ARG1= " + cursor);

		mCusrorAdapter = new CustomCursorAdapter(getActivity().getApplicationContext(), cursor);
		mListView.setAdapter(mCusrorAdapter);
		mListView.setSelection(0);
		if (mListView.getCount() > 0)
		{
			emptyText.setVisibility(View.GONE);
		}
		else
		{

			if (emptyText != null && sendLinearLayout != null && mTailgateSharedPreferences != null)
			{
				TailGateUtility.initMessageListView(emptyText, sendLinearLayout, mTailgateSharedPreferences);
			}

		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		mCusrorAdapter.swapCursor(null);

	}

	public static class MessageAsyncTaskLoader extends AsyncTaskLoader<Cursor>
	{
		Cursor cursor;

		public MessageAsyncTaskLoader(Context context)
		{
			super(context);

		}

		@Override
		protected void onStartLoading()
		{
			super.onStartLoading();

			forceLoad();
		}

		@Override
		public Cursor loadInBackground()
		{

			cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, TailGateMessagesDataBase.COLUMN_MESSAGE_DATE + " DESC");

			return cursor;
		}

		@Override
		protected void onStopLoading()
		{
			// TODO Auto-generated method stub
			super.onStopLoading();
			if (cursor != null)
			{
				cursor.close();
			}

		}

	}

	private BroadcastReceiver changeViewReciever = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent)
		{

			if (mListView != null)
			{
				if (mListView.getCount() == 0)
				{
					TailGateUtility.initMessageListView(emptyText, sendLinearLayout, mTailgateSharedPreferences);
				}
			}

		}
	};

}