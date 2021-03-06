package com.nullcognition.chapter4;
/**
 * Created by ersin on 18/11/14 at 8:38 PM
 */
public class DatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {

  // fields
  public static final String uid             = "_id";
  public static final String fieldName       = "name";
  public static       String tableName       = "myTable";
  private static      String databaseName    = "myDB.db";
  private static      int    databaseVersion = 1; // must be an unsigned int 1,2,3...

  public DatabaseHelper(android.content.Context context, String name, android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version){
	super(context, name, factory, version);
	databaseName = name;
	databaseVersion = version;
  }

  @Override
  public void onCreate(android.database.sqlite.SQLiteDatabase db){
	db.execSQL("CREATE TABLE " + tableName + " (" + uid + " INTEGER PRIMARY KEY AUTOINCREMENT," + fieldName + " VARCHAR(255));");
  }

  @Override
  public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion){
	db.execSQL("DROP TABLE IF EXISTS " + tableName);
	onCreate(db);
  }
}
