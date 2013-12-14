package com.arisprung.tailgate.adapter;


import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.arisprung.tailgate.MessageBean;
import com.arisprung.tailgate.R;
import com.facebook.widget.ProfilePictureView;




import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageListAdapter extends ArrayAdapter<MessageBean>
{

	private final Context context;
	private ArrayList<MessageBean> values;

	private Context mContext;

	//
	// public UserListAdapter(Context context, ArrayList<UserBean> values)
	// {
	//
	// this.context = context;
	// this.values = values;
	// }
	//

	public MessageListAdapter(Context context, int textViewResourceId, ArrayList<MessageBean> objects)
	{
		super(context, textViewResourceId);
		this.context = context;
		this.values = objects;
		mContext = context;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		View rowView = convertView;
		if (rowView == null)
		{
			// LAYOUT FOR PHOTOS IN CALLLOG
			// res = getLayoutInflater().inflate(R.layout.item_composer, null);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.mesaage_list_item, parent, false);
			
			ViewHolder holder = new ViewHolder();
			
			holder.useName = (TextView) rowView.findViewById(R.id.username);
			holder.message = (TextView) rowView.findViewById(R.id.message);
			holder.time = (TextView) rowView.findViewById(R.id.time);
			holder.profileview = (ProfilePictureView) rowView.findViewById(R.id.avatar);
			rowView.setTag(holder);

		}

		
		ViewHolder holder = (ViewHolder) rowView.getTag();

	
		holder.useName.setText(values.get(position).getUserName());
		holder.message.setText(values.get(position).getMessage());
		holder.time .setText(getDate(values.get(position).getDate()));
		holder.profileview.setProfileId((values.get(position).getFaceID()));
		

		return rowView;
	}
	
	static class ViewHolder
	{

		private TextView useName;
		private TextView message;
		private TextView time;
		private ProfilePictureView profileview;
		
		
	
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

	@Override
	public int getCount()
	{

		return values.size();
	}

	@Override
	public MessageBean getItem(int position)
	{

		return values.get(position);
	}

	@Override
	public long getItemId(int position)
	{

		return position;
	}

	public void swapItems(ArrayList<MessageBean> values)
	{
		this.values = values;
		notifyDataSetChanged();
	}

	
}
