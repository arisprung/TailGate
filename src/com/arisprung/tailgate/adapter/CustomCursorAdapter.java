package com.arisprung.tailgate.adapter;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arisprung.tailgate.R;
import com.arisprung.tailgate.utilities.FacebookImageLoader;
import com.facebook.widget.ProfilePictureView;

public class CustomCursorAdapter extends CursorAdapter
{

	LayoutInflater mInflater;
	FacebookImageLoader mImageLoader;

	public CustomCursorAdapter(Context context,Cursor c)
	{

		super(context, c, false);

		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		// when the view will be created for first time,
		// we need to tell the adapters, how each item will look
		mImageLoader = new FacebookImageLoader(context);
		View retView = mInflater.inflate(R.layout.mesaage_list_item, parent, false);
		TextView textViewPersonMessage = (TextView) retView.findViewById(R.id.message);
		TextView textViewPersontime = (TextView) retView.findViewById(R.id.time);
		ImageView profilePersonImage = (ImageView) retView.findViewById(R.id.avatar);
		TextView textViewPersonName = (TextView) retView.findViewById(R.id.username);

		MessageHolder holder = new MessageHolder();
		holder.textViewPersonMessage = textViewPersonMessage;
		holder.textViewPersontime = textViewPersontime;
		holder.textViewPersonName = textViewPersonName;
		holder.profilePersonImage = profilePersonImage;

		retView.setTag(holder);

		return retView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		// here we are setting our data
		// that means, take the data from the cursor and put it in views

		// contact.setId(cursor.getLong(0));
		// contact.setFaceID(cursor.getString(1));
		// contact.setMessage(cursor.getString(2));
		// contact.setUserName(cursor.getString(3));
		// contact.setDate(cursor.getLong(4));

		MessageHolder holder = (MessageHolder) view.getTag();
		holder.textViewPersonMessage.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));

		holder.textViewPersontime.setText(getDate((cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(4))))));

		//holder.profilePersonImage.s(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));
		//
		mImageLoader.load(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))), holder.profilePersonImage);
		holder.textViewPersonName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));

	}

	private static class MessageHolder
	{
		TextView textViewPersonMessage;
		TextView textViewPersontime;
		TextView textViewPersonName;
		ImageView profilePersonImage;
	}

	private String getDate(long timeStamp)
	{

		try
		{
			DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			Date netDate = (new Date(timeStamp));
			return sdf.format(netDate);
		}
		catch (Exception ex)
		{
			return "xx";
		}
	}
}