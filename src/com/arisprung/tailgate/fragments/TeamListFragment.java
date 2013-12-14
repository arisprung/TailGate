package com.arisprung.tailgate.fragments;

import java.util.ArrayList;

import com.arisprung.tailgate.MainActivity;
import com.arisprung.tailgate.MessageBean;
import com.arisprung.tailgate.R;
import com.arisprung.tailgate.TailGateSharedPreferences;
import com.arisprung.tailgate.TailgateConstants;
import com.arisprung.tailgate.adapter.TeamListAdapter;
import com.arisprung.tailgate.db.TailGateMessagesDataBase;

import com.arisprung.tailgate.gcm.ServerUtilities;
import com.google.android.gcm.GCMRegistrar;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

public class TeamListFragment extends Fragment
{

	private TeamListAdapter adapter;

	private ListView listView;
	private String[] mTeamList;
	private static final String TAG = LeagueListFragment.class.toString();
	private static TailGateSharedPreferences mTailgateSharedPreferences = null;
	String strTeamSelected = "";
	String strLeagueSelected = "";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (mTailgateSharedPreferences == null)
			mTailgateSharedPreferences = TailGateSharedPreferences.getInstance(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		if (MainActivity.mLeagueSelected.equals(TailgateConstants.NFL))
		{
			mTeamList = TailgateConstants.NFL_LIST;
		}
		else if (MainActivity.mLeagueSelected.equals(TailgateConstants.NBA))
		{
			mTeamList = TailgateConstants.NBA_LIST;
		}
		else if (MainActivity.mLeagueSelected.equals(TailgateConstants.NHL))
		{
			mTeamList = TailgateConstants.NHL_LIST;
		}
		else if (MainActivity.mLeagueSelected.equals(TailgateConstants.MLB))
		{
			mTeamList = TailgateConstants.MLB_LIST;
		}

		try
		{
			View view = inflater.inflate(R.layout.list_view, container, false);

			return view;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return container;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		// loadParticipantsList();
		// adapter = new TeamListAdapter(getActivity().getApplicationContext(), R.layout.list_checkbox_item, mTeamList);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
				android.R.layout.simple_list_item_multiple_choice, mTeamList);
		listView = (ListView) getView().findViewById(R.id.list);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setTextFilterEnabled(true);
		strLeagueSelected = mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.SELECTED_LEAGUE, "");
		strTeamSelected = mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.SELECTED_TEAM, "");

		if (!strTeamSelected.equals(""))
		{
			for (int i = 0; i < mTeamList.length; i++)
			{
				if (mTeamList[i].equals(strTeamSelected))
				{
					listView.setItemChecked(i, true);
				}
			}
		}

		// TODO NEED to checkshared prefrence
		// datasource.open();
		// ArrayList<String> listteams = datasource.getAllTeamBeans();
		// datasource.close();

		// if (listteams.size() > 0)
		// {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long l)
			{

				if (strLeagueSelected.equals(MainActivity.mLeagueSelected) || strTeamSelected.equals("") )
				{
					if (strTeamSelected.equals(""))
					{
						Toast.makeText(getActivity(), strTeamSelected + " Selected....", Toast.LENGTH_SHORT).show();
						mTailgateSharedPreferences.putStringSharedPreferences(TailGateSharedPreferences.SELECTED_TEAM, mTeamList[position]);
						mTailgateSharedPreferences
								.putStringSharedPreferences(TailGateSharedPreferences.SELECTED_LEAGUE, MainActivity.mLeagueSelected);
						strTeamSelected = mTeamList[position];
						strLeagueSelected = MainActivity.mLeagueSelected;
						sendDataToServer(position);
					}
					else
					{

						if (mTeamList[position].equals(strTeamSelected))
						{
							listView.setItemChecked(position, false);
							mTailgateSharedPreferences.putStringSharedPreferences(TailGateSharedPreferences.SELECTED_TEAM, "");
							mTailgateSharedPreferences.putStringSharedPreferences(TailGateSharedPreferences.SELECTED_LEAGUE, "");
							strTeamSelected = "";
							strLeagueSelected = "";
							Toast.makeText(getActivity(), strTeamSelected + " Unselected....", Toast.LENGTH_SHORT).show();
						}
						else
						{
							mTailgateSharedPreferences.putStringSharedPreferences(TailGateSharedPreferences.SELECTED_TEAM, mTeamList[position]);
							mTailgateSharedPreferences.putStringSharedPreferences(TailGateSharedPreferences.SELECTED_LEAGUE,
									MainActivity.mLeagueSelected);
							strTeamSelected = mTeamList[position];
							strLeagueSelected = MainActivity.mLeagueSelected;
							Toast.makeText(getActivity(), strTeamSelected + " Selected....", Toast.LENGTH_SHORT).show();
							sendDataToServer(position);

						}

					}
				}
				else
				{
					listView.setItemChecked(position, false);
					Toast.makeText(getActivity(), "You can only select one team. Please unselect " + strTeamSelected + " the try again",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}
	
	private class SendDataAsyncTask extends AsyncTask<Void, Void, Void>
	{

		

		@Override
		protected Void doInBackground(Void... params)
		{

	
			ServerUtilities.register(getActivity(),GCMRegistrar.getRegistrationId(getActivity()));
			return null;
		}

	

	}
	
	private void sendDataToServer(int position)
	{
		
		String strRegisterID = GCMRegistrar.getRegistrationId(getActivity());
		if(strRegisterID != null)
		{
			
			SendDataAsyncTask loadAsyncTask = new SendDataAsyncTask();
			loadAsyncTask.execute();
			
		}
		else
		{
			Toast.makeText(getActivity(), "Please login first and then Select Team",
					Toast.LENGTH_LONG).show();
			listView.setItemChecked(position, false);
			mTailgateSharedPreferences.putStringSharedPreferences(TailGateSharedPreferences.SELECTED_TEAM, "");
			mTailgateSharedPreferences.putStringSharedPreferences(TailGateSharedPreferences.SELECTED_LEAGUE, "");
			strTeamSelected = "";
			strLeagueSelected = "";
		}
		
	}
}
