package com.arisprung.tailgate.db;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.SeekBar;

public class TailGateMessagesContentProvider extends ContentProvider
{

	// database
	private TailGateDataBaseHelper database;

	// Used for the UriMacher
	private static final int MESSAGING = 10;
	private static final int MESSAGING_ID = 20;
	
	private static final int LOCATION = 30;
	private static final int LOCATION_ID = 40;

	private static final String AUTHORITY = "com.tailgate.contentprovider";
	
	private static String BASE_PATH = "";
	private static final String BASE_PATH_MESSAGES = "messages";
	private static final String BASE_PATH_LOCATION = "location";
	public static final Uri CONTENT_URI_MESSAGES = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_MESSAGES);
	public static final Uri CONTENT_URI_LOCATION = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_LOCATION);

	public static final String CONTENT_MESSAGES_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/messages";
	public static final String CONTENT_MESSAGES_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/messages";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static
	{
		sURIMatcher.addURI(AUTHORITY, BASE_PATH_MESSAGES, MESSAGING);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH_MESSAGES + "/#", MESSAGING_ID);
		
		sURIMatcher.addURI(AUTHORITY, BASE_PATH_LOCATION, LOCATION);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH_LOCATION + "/#", LOCATION_ID);
	}

	@Override
	public boolean onCreate()
	{
		database = new TailGateDataBaseHelper(getContext());
		return false;
	}

	@Override
	public synchronized Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		Cursor cursor = null;
		
		try
		{
			
		

		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// Check if the caller has requested a column which does not exists
	//	checkColumns(projection);

		// Set the table
		

		int uriType = sURIMatcher.match(uri);
		switch (uriType)
		{
			case MESSAGING:
				queryBuilder.setTables(TailGateMessagesDataBase.TABLE_MESSAGES);
				break;
			case MESSAGING_ID:
				// Adding the ID to the original query
				
				queryBuilder.appendWhere(TailGateMessagesDataBase.COLUMN_MESSAGE_ID + "=" + uri.getLastPathSegment());
				break;
			case LOCATION:
				queryBuilder.setTables(TailGateMessagesDataBase.TABLE_LOCATION);
				break;
			case LOCATION_ID:
				// Adding the ID to the original query
				
				queryBuilder.appendWhere(TailGateMessagesDataBase.COLUMN_LOCATION_ID + "=" + uri.getLastPathSegment());
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();
		cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		

		return cursor;
	}

	@Override
	public String getType(Uri uri)
	{
		return null;
	}

	@Override
	public synchronized Uri insert(Uri uri, ContentValues values)
	{
		
		
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		long id = 0;
		switch (uriType)
		{
			case MESSAGING:
				id = sqlDB.insert(TailGateMessagesDataBase.TABLE_MESSAGES, null, values);
				BASE_PATH = BASE_PATH_MESSAGES;
				break;
			case LOCATION:
				id = sqlDB.insert(TailGateMessagesDataBase.TABLE_LOCATION, null, values);
				BASE_PATH = BASE_PATH_LOCATION;
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType)
		{
			case MESSAGING:
				rowsDeleted = sqlDB.delete(TailGateMessagesDataBase.TABLE_MESSAGES, selection, selectionArgs);
				break;
			case MESSAGING_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection))
				{
					rowsDeleted = sqlDB.delete(TailGateMessagesDataBase.TABLE_MESSAGES, TailGateMessagesDataBase.COLUMN_MESSAGE_ID + "=" + id, null);
				}
				else
				{
					rowsDeleted = sqlDB.delete(TailGateMessagesDataBase.TABLE_MESSAGES,TailGateMessagesDataBase.COLUMN_MESSAGE_ID + "=" + id + " and " + selection,
							selectionArgs);
				}
				break;
			case LOCATION:
				rowsDeleted = sqlDB.delete(TailGateMessagesDataBase.TABLE_LOCATION, selection, selectionArgs);
				break;
			case LOCATION_ID:
				String idlocation = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection))
				{
					rowsDeleted = sqlDB.delete(TailGateMessagesDataBase.TABLE_LOCATION, TailGateMessagesDataBase.COLUMN_LOCATION_ID + "=" + idlocation, null);
				}
				else
				{
					rowsDeleted = sqlDB.delete(TailGateMessagesDataBase.TABLE_LOCATION,TailGateMessagesDataBase.COLUMN_LOCATION_ID + "=" + idlocation + " and " + selection,
							selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType)
		{
			case MESSAGING:
				rowsUpdated = sqlDB.update(TailGateMessagesDataBase.TABLE_MESSAGES, values, selection, selectionArgs);
				break;
			case MESSAGING_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection))
				{
					rowsUpdated = sqlDB.update(TailGateMessagesDataBase.TABLE_MESSAGES, values,TailGateMessagesDataBase.COLUMN_MESSAGE_ID + "=" + id, null);
				}
				else
				{
					rowsUpdated = sqlDB.update(TailGateMessagesDataBase.TABLE_MESSAGES, values, TailGateMessagesDataBase.COLUMN_MESSAGE_ID  + "=" + id + " and " + selection,
							selectionArgs);
				}
				break;
			case LOCATION:
				rowsUpdated = sqlDB.update(TailGateMessagesDataBase.TABLE_LOCATION, values, selection, selectionArgs);
				break;
			case LOCATION_ID:
				String idLocation = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection))
				{
					rowsUpdated = sqlDB.update(TailGateMessagesDataBase.TABLE_LOCATION, values,TailGateMessagesDataBase.COLUMN_LOCATION_ID + "=" + idLocation, null);
				}
				else
				{
					rowsUpdated = sqlDB.update(TailGateMessagesDataBase.TABLE_LOCATION, values, TailGateMessagesDataBase.COLUMN_LOCATION_ID  + "=" + idLocation + " and " + selection,
							selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection)
	{
		 String[] available = { TailGateMessagesDataBase.COLUMN_MESSAGE_ID, TailGateMessagesDataBase.COLUMN_MESSAGE_FACE_ID,TailGateMessagesDataBase.COLUMN_MESSAGE, TailGateMessagesDataBase.COLUMN_MESSAGE_NAME, TailGateMessagesDataBase.COLUMN_MESSAGE_DATE };

		
		if (projection != null)
		{
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns))
			{
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}

}
