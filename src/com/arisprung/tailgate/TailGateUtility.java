package com.arisprung.tailgate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class TailGateUtility
{
	
	public static void showAuthenticatedDialog(Context context,String title,String text)
	{
		
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(title);
			builder.setMessage(text);
			
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
