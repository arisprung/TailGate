package com.arisprung.tailgate.fragments;



import com.arisprung.tailgate.R;
import com.arisprung.tailgate.gcm.ServerUtilities;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.AsyncTaskLoader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class SendMessageFragment extends Fragment
{

	
	
	private static final String TAG = SendMessageFragment.class.toString();
	private Button addButton;
	// EditText messageEditText;
	private  EditText messageEditText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		if (container == null)
		{
			return null;
		}
		View view = inflater.inflate(R.layout.add_message_layout, container, false);
		addButton = (Button) view.findViewById(R.id.button);
		messageEditText = (EditText) view.findViewById(R.id.editText);

		return view;

	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				if (!messageEditText.getText().toString().equals("") && (messageEditText.getText().length() < 140))
				{
					SendMessageAsyncTaskLoader loadAsyncTask = new SendMessageAsyncTaskLoader();
					loadAsyncTask.execute();
					
			
				}

			}

		});

	}
	
	private class SendMessageAsyncTaskLoader extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params)
		{
			String strMessage = messageEditText.getText().toString();
			ServerUtilities.sendMessageToServer(getActivity(),strMessage);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			messageEditText.setText("");
		}

		
		
	
	}
	
	



}