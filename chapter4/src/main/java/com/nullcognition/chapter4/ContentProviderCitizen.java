package com.nullcognition.chapter4;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class ContentProviderCitizen extends ContentProvider {

  public static final  String                                    AUTHORITY        = "com.nullcognition.chapter4.ContentProviderCitizen";
  private static final String                                    DATABASE_NAME    = "citizen.db";
  private static final int                                       DATABASE_VERSION = 1;
  private static final int                                       CITIZENS         = 1;
  private static final int                                       SSID             = 2;
  private static       android.content.UriMatcher                uriMatcher       = null;
  private static       java.util.HashMap<String, String>         projectionMap    = null;
  private              com.nullcognition.chapter4.DatabaseHelper databaseHelper   = null;

  // static init
  static{
	uriMatcher = new android.content.UriMatcher(android.content.UriMatcher.NO_MATCH);
	uriMatcher.addURI(AUTHORITY, "citizen", CITIZENS);
	uriMatcher.addURI(AUTHORITY, "citizen/#", SSID);
// PROJECTION MAP USED FOR ROW ALIAS
	projectionMap = new java.util.HashMap<String, String>();
	projectionMap.put(TableCitizen.COL_ID, TableCitizen.COL_ID);
	projectionMap.put(TableCitizen.COL_NAME, TableCitizen.COL_NAME);
	projectionMap.put(TableCitizen.COL_STATE, TableCitizen.COL_STATE);
	projectionMap.put(TableCitizen.COL_INCOME, TableCitizen.COL_INCOME);
  }

  public ContentProviderCitizen(){
  }

  @Override
  public boolean onCreate(){

	databaseHelper = new com.nullcognition.chapter4.DatabaseHelper(getContext(), null, null, - 1);
	return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){

	android.database.sqlite.SQLiteQueryBuilder sqLiteQueryBuilder = new android.database.sqlite.SQLiteQueryBuilder();
	sqLiteQueryBuilder.setTables(com.nullcognition.chapter4.TableCitizen.TABLE_NAME);

	switch(uriMatcher.match(uri)){
	  case CITIZENS:
		sqLiteQueryBuilder.setProjectionMap(projectionMap);
		break;
	  case SSID:

		String ssid = uri.getPathSegments().get(com.nullcognition.chapter4.TableCitizen.SSID_PATH_POSITION);
		sqLiteQueryBuilder.setProjectionMap(projectionMap);
		sqLiteQueryBuilder.appendWhere(com.nullcognition.chapter4.TableCitizen.COL_ID + "=" + ssid); // quering by specific ssid
		break;
	  default:
		android.util.Log.e("Switch uriMatcher.match(uri)", "Default parameter invalid");
		throw new java.security.InvalidParameterException();
	}

	android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
	Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
	// register notification listeners with given cursor, cursor knows when underlying data has changed
	cursor.setNotificationUri(getContext().getContentResolver(), uri);

	return cursor;
  }

  @Override
  public String getType(Uri uri){
	switch(uriMatcher.match(uri)){
	  case CITIZENS:
		return TableCitizen.CONTENT_TYPE;
	  case SSID:
		return TableCitizen.CONTENT_ITEM_TYPE;
	  default:
		throw new IllegalArgumentException("Unknown URI " + uri);
	}
  }

  @Override
  public Uri insert(Uri uri, ContentValues initialValues){
	//ONLY GENERAL CITIZENS URI IS ALLOWED FOR INSERTS
	// DOESN'T MAKE SENSE TO SPECIFY A SINGLE CITIZEN
	if(uriMatcher.match(uri) != CITIZENS){
	  throw new IllegalArgumentException("Unknown URI " + uri);
	}
	// PACKAGE DESIRED VALUES AS A CONTENTVALUE OBJECT
	ContentValues values;

	if(initialValues != null){
	  values = new ContentValues(initialValues);
	}
	else{
	  values = new ContentValues();
	}

	android.database.sqlite.SQLiteDatabase db = databaseHelper.getWritableDatabase();
	long rowId = db.insert(TableCitizen.TABLE_NAME, com.nullcognition.chapter4.TableCitizen.COL_NAME, values);

	if(rowId > 0){
	  Uri citizenUri = android.content.ContentUris.withAppendedId(TableCitizen.CONTENT_URI, rowId);
	  // NOTIFY CONTEXT OF THE CHANGE
	  getContext().getContentResolver().notifyChange(citizenUri, null);
	  return citizenUri;
	}
	throw new android.database.SQLException("Failed to insert row into " + uri);
  }

  @Override
  public int delete(Uri uri, String where, String[] whereArgs){
	android.database.sqlite.SQLiteDatabase db = databaseHelper.getWritableDatabase();
	int count;
	switch(uriMatcher.match(uri)){
	  case CITIZENS:
// PERFORM REGULAR DELETE
		count = db.delete(TableCitizen.TABLE_NAME, where, whereArgs);
		break;
	  case SSID:
// FROM INCOMING URI GET SSID
		String ssid = uri.getPathSegments().
		  get(TableCitizen.SSID_PATH_POSITION);
// USER WANTS TO DELETE A SPECIFIC CITIZEN
		String finalWhere = com.nullcognition.chapter4.TableCitizen.COL_ID + "=" + ssid;
// IF USER SPECIFIES WHERE FILTER THEN APPEND
		if(where != null){
		  finalWhere = finalWhere + " AND " + where;
		}
		count = db.delete(TableCitizen.TABLE_NAME, finalWhere, whereArgs);
		break;
	  default:
		throw new IllegalArgumentException("Unknown URI " + uri);
	}
	getContext().getContentResolver().notifyChange(uri, null);
	return count;

  }

  @Override
  public int update(Uri uri, ContentValues values, String where, String[] whereArgs){
	android.database.sqlite.SQLiteDatabase db = databaseHelper.getWritableDatabase();
	int count;
	switch(uriMatcher.match(uri)){
	  case CITIZENS:
// GENERAL UPDATE ON ALL CITIZENS
		count = db.update(TableCitizen.TABLE_NAME, values, where, whereArgs);
		break;
	  case SSID:
// FROM INCOMING URI GET SSID
		String ssid = uri.getPathSegments().
		  get(TableCitizen.SSID_PATH_POSITION);
// THE USER WANTS TO UPDATE A SPECIFIC CITIZEN
		String finalWhere = com.nullcognition.chapter4.TableCitizen.COL_ID + "=" + ssid;
		if(where != null){
		  finalWhere = finalWhere + " AND " + where;
		}
// PERFORM THE UPDATE ON THE SPECIFIC CITIZEN
		count = db.update(TableCitizen.TABLE_NAME, values, finalWhere, whereArgs);
		break;
	  default:
		throw new IllegalArgumentException("Unknown URI " + uri);
	}
	getContext().getContentResolver().notifyChange(uri, null);
	return count;
  }


}
