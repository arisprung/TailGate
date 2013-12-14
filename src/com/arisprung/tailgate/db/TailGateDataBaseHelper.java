package com.arisprung.tailgate.db;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TailGateDataBaseHelper extends SQLiteOpenHelper
{
	
	private static final String DATABASE_NAME = "messages.db";
	private static final int DATABASE_VERSION = 1;

	public TailGateDataBaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		TailGateMessagesDataBase.onCreate(database);
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		TailGateMessagesDataBase.onUpgrade(database, oldVersion, newVersion);
	}

	/*People slide Table*/
//	public static final String TABLE_MESSAGES = "messages_table";
//	public static final String COLUMN_MESSAGE_ID = "_id";
//	public static final String COLUMN_MESSAGE_FACE_ID = "message_face_id";
//	public static final String COLUMN_MESSAGE = "message";
//	public static final String COLUMN_MESSAGE_NAME = "message_face_name";
//	public static final String COLUMN_MESSAGE_DATE = "message_date";
//	
	

//
//	private static final String DATABASE_NAME = "tailgate.db";
//	private static final int DATABASE_VERSION = 1;
//
////	// Database creation sql statement
////	private static final String DATABASE_PEOPLE_SLIDE_CREATE = "create table " + TABLE_MESSAGES + "( " + COLUMN_MESSAGE_ID + " integer primary key autoincrement, "
////			+ COLUMN_MESSAGE_FACE_ID + " text, " + COLUMN_MESSAGE + " text, "+ " text, " + COLUMN_MESSAGE_NAME + " text, " + COLUMN_MESSAGE_DATE + " integer);";
//	
//	// Database creation sql statement
//		private static final String DATABASE_PEOPLE_SLIDE_CREATE = "create table " + TABLE_MESSAGES + "( " + COLUMN_MESSAGE_ID + " integer primary key autoincrement, "+  COLUMN_MESSAGE_NAME + " text, " +COLUMN_MESSAGE_FACE_ID + " text not null);";
//	
//
//
//	public TailGateDataBaseHelper(Context context)
//	{
//		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase database)
//	{
//		database.execSQL(DATABASE_PEOPLE_SLIDE_CREATE);
//
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
//	{
//
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
//		onCreate(db);
//	}

}