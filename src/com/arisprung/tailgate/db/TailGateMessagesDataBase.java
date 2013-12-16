package com.arisprung.tailgate.db;

import java.util.ArrayList;
import java.util.List;

import com.arisprung.tailgate.MessageBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TailGateMessagesDataBase
{

	
	/* People slide Table */
	public static final String TABLE_MESSAGES = "messages_table";
	public static final String COLUMN_MESSAGE_ID = "_id";
	public static final String COLUMN_MESSAGE_FACE_ID = "message_face_id";
	public static final String COLUMN_MESSAGE = "message";
	public static final String COLUMN_MESSAGE_NAME = "message_face_name";
	public static final String COLUMN_MESSAGE_DATE = "message_date";
	
	public static final String TABLE_LOCATION = "location_table";
	public static final String COLUMN_LOCATION_ID = "_id";
	public static final String COLUMN_LOCATION_FACE_ID = "location_face_id";
	public static final String COLUMN_LANITUDE = "lanitude";
	public static final String COLUMN_LONGNITUDE = "longnitude";

	private static final String DATABASE_MESSAGE_CREATE = "create table " + TABLE_MESSAGES + "(" + COLUMN_MESSAGE_ID + " integer primary key autoincrement, "
			+ COLUMN_MESSAGE + " text not null, "+ COLUMN_MESSAGE_NAME + " text not null, " + COLUMN_MESSAGE_FACE_ID + " text not null,"+ COLUMN_MESSAGE_DATE + " integer" + ");";

	
	private static final String DATABASE_LOCATION_CREATE = "create table " + TABLE_LOCATION + "(" + COLUMN_LOCATION_ID + " integer primary key autoincrement, "
			+ COLUMN_LOCATION_FACE_ID + " integer unique, "+ COLUMN_LANITUDE + " text, " + COLUMN_LONGNITUDE + " text" + ");";

	
	

	public static void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_MESSAGE_CREATE);
		database.execSQL(DATABASE_LOCATION_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		Log.w(TailGateMessagesDataBase.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
		onCreate(database);
	}


}