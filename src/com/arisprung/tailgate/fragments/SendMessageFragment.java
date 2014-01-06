package com.arisprung.tailgate.fragments;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.arisprung.tailgate.R;
import com.arisprung.tailgate.TailGateSharedPreferences;
import com.arisprung.tailgate.utilities.TailGateUtility;
import com.facebook.Session;



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
			mTailgateSharedPreferences = TailGateSharedPreferences.getInstance(getActivity().getApplicationContext());
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				TailGateUtility.sendMessageToServer(getActivity(),messageEditText, mTailgateSharedPreferences);
			}

		});

	}
	
//	private class SendMessageAsyncTaskLoader extends AsyncTask<String, Void, Void>
//	{
//		
//		@Override
//		protected void onPreExecute()
//		{
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//			String strMessage = messageEditText.getText().toString();
//			messageEditText.setText("");
//			//doInBackground(strMessage);
//			
//				
//		}
//
//		@Override
//		protected Void doInBackground(String... params)
//		{
//			//
//			ServerUtilities.sendMessageToServer(getActivity(),params[0]);
//			
//			return null;
//		}
//		
//		@Override
//		protected void onPostExecute(Void result)
//		{
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			
//		}
//
//		
//		
//	
//	}
	
//	private void showDialog(String title,String strText)
//	{
//		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//		builder.setTitle(title);
//		builder.setMessage(strText);
//		
//		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				// TODO Auto-generated method stub
//				//Toast.makeText(getActivity(), "Close is clicked", Toast.LENGTH_LONG).show();
//				
//			}
//		});
//		builder.show(); //To show the AlertDialog
//	}
//


}