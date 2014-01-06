package com.arisprung.tailgate.fragments;

import android.content.Context;
import android.os.AsyncTask;

import com.arisprung.tailgate.gcm.ServerUtilities;

public class SendMessageAsyncTaskLoader extends AsyncTask<String, Void, Void>
{

	private Context mContext;

	public SendMessageAsyncTaskLoader(Context context)
	{
		mContext = context;

	}

	@Override
	protected void onPreExecute()
	{
		// TODO Auto-generated method stub
		super.onPreExecute();
		// String strMessage = messageEditText.getText().toString();
		// messageEditText.setText("");
		// doInBackground(strMessage);

	}

	@Override
	protected Void doInBackground(String... params)
	{
		//
		ServerUtilities.sendMessageToServer(mContext,params[0]);
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result)
	{
		// TODO Auto-generated method stub
		super.onPostExecute(result);

	}

}