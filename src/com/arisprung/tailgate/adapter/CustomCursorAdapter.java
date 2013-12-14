package com.arisprung.tailgate.adapter;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arisprung.tailgate.R;
import com.facebook.widget.ProfilePictureView;

public class CustomCursorAdapter extends CursorAdapter {

    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.mesaage_list_item, parent, false);

        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views
    	
//    	contact.setId(cursor.getLong(0));
//		contact.setFaceID(cursor.getString(1));
//		contact.setMessage(cursor.getString(2));
//		contact.setUserName(cursor.getString(3));
//		contact.setDate(cursor.getLong(4));
    	
        TextView textViewPersonMessage = (TextView) view.findViewById(R.id.message);
        textViewPersonMessage.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        
        TextView textViewPersontime = (TextView) view.findViewById(R.id.time);
        textViewPersontime.setText(getDate((cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(4))))));
        
        
        ProfilePictureView profilePersonImage = (ProfilePictureView) view.findViewById(R.id.avatar);
        profilePersonImage.setProfileId(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));
//        
        TextView textViewPersonName = (TextView) view.findViewById(R.id.username);
        textViewPersonName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
        
    	
        
       
    }
    
    private String getDate(long timeStamp){

	    try{
	        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	        Date netDate = (new Date(timeStamp));
	        return sdf.format(netDate);
	    }
	    catch(Exception ex){
	        return "xx";
	    }
	} 
}