package com.arisprung.tailgate.fragments;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arisprung.tailgate.MainActivity;
import com.arisprung.tailgate.R;
import com.arisprung.tailgate.TailGateSharedPreferences;
import com.arisprung.tailgate.gcm.ServerUtilities;


public class SendMessageFragment extends Fragment
{

	
	
	private static final String TAG = SendMessageFragment.class.toString();
	private Button addButton;
	// EditText messageEditText;
	private  EditText messageEditText;
	private TailGateSharedPreferences mTailgateSharedPreferences = null;

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
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if (mTailgateSharedPreferences == null)
			mTailgateSharedPreferences = TailGateSharedPreferences.getInstance(getActivity());
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
					String strTeam = mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.SELECTED_TEAM, "");
					String strFacID = mTailgateSharedPreferences.getStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_ID, "");
					if(strTeam.equals("") || strFacID.equals(""))
					{
						if(strTeam.equals(""))
						{
							showDialog("Select Team","Please select a team in order to send messages.");
						}
						else if(strFacID.equals(""))
						{
							showDialog("Please Login","First login in order to send messages.");
						}
						
						
						
						
					}
					else
					{
						SendMessageAsyncTaskLoader loadAsyncTask = new SendMessageAsyncTaskLoader();
						loadAsyncTask.execute();
					}
					
					
			
				}
				else
				{
					showDialog("Empty Text","Text cant be empty.");
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
	
	private void showDialog(String title,String strText)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(strText);
		
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), "Close is clicked", Toast.LENGTH_LONG).show();
				
			}
		});
		builder.show(); //To show the AlertDialog
	}



}