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

	private static final String DATABASE_CREATE = "create table " + TABLE_MESSAGES + "(" + COLUMN_MESSAGE_ID + " integer primary key autoincrement, "
			+ COLUMN_MESSAGE + " text not null, "+ COLUMN_MESSAGE_NAME + " text not null, " + COLUMN_MESSAGE_FACE_ID + " text not null,"+ COLUMN_MESSAGE_DATE + " integer" + ");";

	// // Database creation SQL statement
	// private static final String DATABASE_CREATE = "create table " + TABLE_STATS + "(" + COLUMN_ID + " integer primary key autoincrement, "
	// + COLUMN_TIME + " text not null, " + COLUMN_MODULE + " text not null, " + COLUMN_TYPE + " text not null," + COLUMN_VALUE
	// + " text not null, " + COLUMN_DESTINATION + " text not null, " + COLUMN_STATS_TYPE + " text not null" + ");";

	public static void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		Log.w(TailGateMessagesDataBase.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
		onCreate(database);
	}
	// // Database fields
	// private SQLiteDatabase database;
	// private TailGateDataBaseHelper dbHelper;
	// private String[] allColumns = { TailGateDataBaseHelper.COLUMN_MESSAGE_ID, TailGateDataBaseHelper.COLUMN_MESSAGE_FACE_ID,TailGateDataBaseHelper.COLUMN_MESSAGE,
	// TailGateDataBaseHelper.COLUMN_MESSAGE_NAME, TailGateDataBaseHelper.COLUMN_MESSAGE_DATE };
	//
	//
	// public TailGateMessagesDataBase(Context context)
	// {
	// dbHelper = new TailGateDataBaseHelper(context);
	// }
	//
	// public void open() throws SQLException
	// {
	// database = dbHelper.getWritableDatabase();
	// }
	//
	// public void close()
	// {
	//
	// dbHelper.close();
	// }
	//
	// public MessageBean insertItem(MessageBean message)
	// {
	//
	// open();
	// ContentValues values = new ContentValues();
	// values.put(TailGateDataBaseHelper.COLUMN_MESSAGE_FACE_ID, message.getFaceID());
	// values.put(TailGateDataBaseHelper.COLUMN_MESSAGE, message.getMessage());
	// values.put(TailGateDataBaseHelper.COLUMN_MESSAGE_NAME, message.getUserName());
	// values.put(TailGateDataBaseHelper.COLUMN_MESSAGE_DATE, System.currentTimeMillis());
	// long insertId = database.insert(TailGateDataBaseHelper.TABLE_MESSAGES, null, values);
	// Cursor cursor = database.query(TailGateDataBaseHelper.TABLE_MESSAGES, allColumns, TailGateDataBaseHelper.COLUMN_MESSAGE_ID + " = " + insertId,
	// null, null, null, null);
	// cursor.moveToFirst();
	// MessageBean contact1 = cursorToItem(cursor);
	// cursor.close();
	// close();
	// return contact1;
	// }
	//
	// public void deleteItem(String facid)
	// {
	//
	// open();
	// database.delete(TailGateDataBaseHelper.TABLE_MESSAGES, TailGateDataBaseHelper.COLUMN_MESSAGE_FACE_ID + " = " + "'" + facid + "'", null);
	// close();
	// }
	//
	// public List<MessageBean> getAllItemData()
	// {
	// List<MessageBean> items = new ArrayList<MessageBean>();
	//
	// Cursor cursor = database.query(TailGateDataBaseHelper.TABLE_MESSAGES, allColumns, null, null, null, null, null);
	//
	// cursor.moveToFirst();
	// while (!cursor.isAfterLast())
	// {
	// MessageBean item = cursorToItem(cursor);
	// items.add(item);
	// cursor.moveToNext();
	// }
	// // make sure to close the cursor
	// cursor.close();
	// return items;
	// }
	//
	// public MessageBean cursorToItem(Cursor cursor)
	// {
	// MessageBean contact = new MessageBean();
	// contact.setId(cursor.getLong(0));
	// contact.setFaceID(cursor.getString(1));
	// contact.setMessage(cursor.getString(2));
	// contact.setUserName(cursor.getString(3));
	// contact.setDate(cursor.getLong(4));
	// return contact;
	// }
	//
	//
	//
	// public int getItemDBCount()
	// {
	//
	// Cursor cursor = database.query(TailGateDataBaseHelper.TABLE_MESSAGES, allColumns, null, null, null, null, null);
	//
	// int i = cursor.getCount();
	// cursor.close();
	// return i;
	// }
	//
	//
	//
	// public Cursor getCursorforPeopleInSlide()
	// {
	//
	// Cursor cursor = database.query(TailGateDataBaseHelper.TABLE_MESSAGES, allColumns, null, null, null, null, TailGateDataBaseHelper.COLUMN_MESSAGE_DATE);
	//
	//
	// return cursor;
	// }

}