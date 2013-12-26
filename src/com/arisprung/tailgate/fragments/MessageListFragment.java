package com.arisprung.tailgate.fragments;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.arisprung.tailgate.MessageBean;
import com.arisprung.tailgate.R;
import com.arisprung.tailgate.TailGateSharedPreferences;
import com.arisprung.tailgate.TailGateUtility;
import com.arisprung.tailgate.adapter.CustomCursorAdapter;
import com.arisprung.tailgate.adapter.MessageListAdapter;
import com.arisprung.tailgate.db.TailGateMessagesDataBase;

import com.arisprung.tailgate.gcm.ServerUtilities;

public class MessageListFragment extends Fragment implements LoaderCallbacks<Cursor>
{

	public static MessageListAdapter adapter;
	private ListView mListView;
	private CustomCursorAdapter cursorAdapter;
	private ArrayList<MessageBean> mMessageList;
	private static final String TAG = MessageListFragment.class.toString();
	private TailGateMessagesDataBase mMessagesDataSource;
	private CustomCursorAdapter mCusrorAdapter;
	private int LOADER_ID = 1000;
	private static final String AUTHORITY = "com.tailgate.contentprovider";
	private static final String BASE_PATH = "messages";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	private TailGateSharedPreferences mTailgateSharedPreferences = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//setHasOptionsMenu(true);
		// mMessagesDataSource = new TailGateMessagesDataBase(getActivity());
		if (container == null)
		{
			return null;
		}
		View view = inflater.inflate(R.layout.list_view, container, false);

		return view;

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getLoaderManager().initLoader(LOADER_ID, null, this);
		
		if (mTailgateSharedPreferences == null)
			mTailgateSharedPreferences = TailGateSharedPreferences.getInstance(getActivity());

		
		String strTeam = mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.SELECTED_TEAM, "");
		String strFacID = mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_ID, "");
		if(strTeam.equals("") || strFacID.equals(""))
		{

			 if(strFacID.equals(""))
			{
				
				TailGateUtility.showAuthenticatedDialog(getActivity(),"Please Login","First login in order to see messages.");
				
			}
			 else if(strTeam.equals(""))
			{
				TailGateUtility.showAuthenticatedDialog(getActivity(),"Select Team","Please select a team in order to see messages.");
			}
		}
			
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		//mMessageList = new ArrayList<MessageBean>();

	//	adapter = new MessageListAdapter(getActivity().getApplicationContext(), R.layout.mesaage_list_item, mMessageList);
		mListView = (ListView) getView().findViewById(R.id.list);
//		mListView.setAdapter(adapter);
//		mListView.setTextFilterEnabled(true);
//		LoadMessageListAsyncTask loadAsyncTask = new LoadMessageListAsyncTask();
//		loadAsyncTask.execute();

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int i, long l)
			{
				Log.i(TAG, "Item Clicked");

			}
		});

	}

//	private BroadcastReceiver updatGroupListReciever = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent)
//		{
//
//			adapter.notifyDataSetChanged();
//			Log.d("updatGroupListReciever", "Got message: ");
//		}
//	};
//
//	@Override
//	public void onResume()
//	{
//
//		getActivity().registerReceiver(updatGroupListReciever, new IntentFilter("presence_change"));
//		super.onResume();
//	}
//
//	@Override
//	public void onPause()
//	{
//		getActivity().unregisterReceiver(updatGroupListReciever);
//		super.onPause();
//	}
//
//	private class LoadMessageListAsyncTask extends AsyncTask<Void, Void, Void>
//	{
//
//		@Override
//		protected void onProgressUpdate(Void... values)
//		{
//
//			super.onProgressUpdate(values);
//			adapter.notifyDataSetChanged();
//		}
//
//		@Override
//		protected Void doInBackground(Void... params)
//		{
//
//			// mMessagesDataSource = new TailGateMessagesDataBase(getActivity());
//			// mMessagesDataSource.open();
//			// Cursor c = mMessagesDataSource.getCursorforPeopleInSlide();
//			// int index = 0;
//			// while (c.moveToNext())
//			// {
//			// index++;
//			// MessageBean message = mMessagesDataSource.cursorToItem(c);
//			// mMessageList.add(message);
//			// // if (index == 20)
//			// // {
//			// // publishProgress();
//			// // index = 0;
//			// // }
//			// }
//			// mMessagesDataSource.close();
//			cursorAdapter = new CustomCursorAdapter(getActivity(), mMessagesDataSource.getCursorforPeopleInSlide());
//			mListView.setAdapter(cursorAdapter);
//
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result)
//		{
//
//			super.onPostExecute(result);
//			adapter.notifyDataSetChanged();
//		}
//
//	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		// Create the Cursor that will take care of the data being displayed
		Log.d("TAG", "onCreateLoader...");
		return new CursorLoader(getActivity(),CONTENT_URI, null, null, null, TailGateMessagesDataBase.COLUMN_MESSAGE_DATE+ " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor)
	{
		// Now Bind the data to the View
		Log.d("TAG", "onLoadFinished...ARG1= " + cursor);
		mCusrorAdapter = new CustomCursorAdapter(getActivity(), cursor);
		mListView.setAdapter(mCusrorAdapter);
		

	}
	

	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		// TODO Auto-generated method stub

	}

}