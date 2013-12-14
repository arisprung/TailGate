package com.arisprung.tailgate.adapter;

import java.util.ArrayList;

import com.arisprung.tailgate.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;


public class TeamListAdapter extends ArrayAdapter<String>
{

	private final Context context;
	private String[] values;
	private CheckedTextView teamNameView;



	private Context mContext;


	public TeamListAdapter(Context context, int textViewResourceId, String[] objects)
	{
		super(context, textViewResourceId);
		this.context = context;
		this.values = objects;
		mContext = context;
	
		

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		

		String teamName = values[position];
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_checkbox_item, parent, false);
		
		teamNameView = (CheckedTextView) rowView.findViewById(R.id.user_name_checked);
		teamNameView.setText(teamName);
	
		
		
		
		

		return rowView;
	}

	@Override
	public int getCount()
	{

		return values.length;
	}

	@Override
	public String getItem(int position)
	{

		return values[position];
	}

	@Override
	public long getItemId(int position)
	{

		return position;
	}

	public void swapItems(String[] values)
	{
		this.values = values;
		notifyDataSetChanged();
	}

}
