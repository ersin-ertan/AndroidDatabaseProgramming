package com.nullcognition.chapter4;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class ContentProviderCitizen extends ContentProvider {

  public static final  String                                                           AUTHORITY        = "com.nullcognition.chapter4.ContentProviderCitizen";
  private static final String                                                           DATABASE_NAME    = "citizen.db";
  private static final int                                                              DATABASE_VERSION = 1;
  private static final int                                                              CITIZENS         = 1;
  private static final int                                                              SSID             = 2;
  private static       android.content.UriMatcher                                       uriMatcher       = null;
  private static       java.util.HashMap<String, String>                                projectionMap    = null;
  private              com.nullcognition.chapter4.ContentProviderCitizen.DatabaseHelper databaseHelper   = null;

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

	databaseHelper = new com.nullcognition.chapter4.ContentProviderCitizen.DatabaseHelper(getContext(), null, null, - 1);
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
	return ""; // todo fix
  }

  @Override
  public Uri insert(Uri uri, ContentValues values){
	return android.net.Uri.EMPTY; //todo fix
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs){
	return 0; // todo fix
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs){
	return 0;
  }

  private static class DatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {

	public DatabaseHelper(android.content.Context context, String name, android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version){
	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(android.database.sqlite.SQLiteDatabase db){
	  db.execSQL("CREATE TABLE " + TableCitizen.TABLE_NAME +
				 " (" + TableCitizen.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				 TableCitizen.COL_NAME + " TEXT, " +
				 TableCitizen.COL_STATE + " TEXT," +
				 TableCitizen.COL_INCOME + " INTEGER);");
	}

	@Override
	public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion){
	  db.execSQL("DROP TABLE IF EXISTS " + TableCitizen.TABLE_NAME);
// CREATE NEW INSTANCE OF SCHEMA
	  onCreate(db);
	}
  }

}
