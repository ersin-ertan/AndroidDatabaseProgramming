package com.nullcognition.androiddatabaseprogramming;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ActivityMain extends Activity {

  public static final String str         = "string";
  final               long   update_freq = 1000 * 60 * 60 * 24;
  String fileName = "myFilename.txt";
  private android.content.SharedPreferences sharedPreferences = null;
  private long lastUpdateTime = 0L;
  private long timeElapsed    = 0L;

  @Override
  protected void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	sharedPreferences = getSharedPreferences(str, android.content.Context.MODE_PRIVATE);
	// second parameter is just for our app - can also be MODE_WORLD_READABLE, MODE_WORLD_WRITEABLE(external attack vector), MODE_MULTI_PROCESS modifiable by multiple processes
	// which may be writing to the same shared preference instance - good for services, other app written by you (android:sharedUserID in manifest)

	android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
	editor.putString("stringKey", "myString");
	editor.putBoolean("booleanKey", true);
	editor.commit();

	String stringValue = sharedPreferences.getString("stringKey", "default");
	boolean booleanValue = sharedPreferences.getBoolean("booleanKey", false);
	// may be used to show splash screen on first opening, or perhaps some services and BroadcastReceivers

	// used for caching, syncing with regular updating.

	android.widget.Toast.makeText(this, stringValue, android.widget.Toast.LENGTH_SHORT).show();
	android.widget.Toast.makeText(this, Boolean.toString(booleanValue), android.widget.Toast.LENGTH_SHORT).show();

	internalStorage();

	readingFromInternalStorage();

	//locationCache(); // lastKnown is null for emulator location

	getStringSetForMultipleStrings();

	updateMe();

	externalStorage();

	SQLite();
  }

  private void internalStorage(){
	String message = "hello world";
	try{
	  java.io.FileOutputStream fos = openFileOutput(fileName, android.content.Context.MODE_PRIVATE); // Context.MODE_APPEND without overwiting
	  fos.write(message.getBytes());
	  fos.close();
	}
	catch(java.io.IOException e){e.printStackTrace();}

  }

  private void readingFromInternalStorage(){
	try{
	  java.io.FileInputStream fileInputStream = openFileInput(fileName);
	  java.io.InputStreamReader inputStreamReader = new java.io.InputStreamReader(fileInputStream);

	  StringBuilder stringBuilder = new StringBuilder();
	  char[] inputBUffer = new char[2048];
	  int l;

	  while((l = inputStreamReader.read(inputBUffer)) != - 1){
		stringBuilder.append(inputBUffer, 0, 1);
	  }
	  String readString = stringBuilder.toString();
	  deleteFile(fileName);
	}
	catch(java.io.IOException e){e.printStackTrace();}
  }

  private void getStringSetForMultipleStrings(){
	java.util.Set<String> mySet = new java.util.HashSet<String>();
	mySet.add("one");
	mySet.add("two");
	android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
	editor.putStringSet("mySS", mySet).commit();

	mySet = sharedPreferences.getStringSet("mySS", new java.util.HashSet<String>(0)); // may be smart to set the capacity of a default to 0
  }

  private void updateMe(){
	lastUpdateTime = sharedPreferences.getLong("lastupdatekey", 0L);
	timeElapsed = System.currentTimeMillis() - lastUpdateTime;
	if(timeElapsed > update_freq){ //update
	  android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
	  editor.putLong("lastupdatekey", System.currentTimeMillis()).commit();
	}
	// this may be used to cache a users name for a edit text, or remember the applications state ex.changed to silent mode from settings
  }

  private void externalStorage(){}

  private void SQLite(){
	SQLiteOpenHelper_ sqLiteOpenHelper = new SQLiteOpenHelper_(this, "myDB", null, 1);
	android.database.sqlite.SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

	// insert using contentvalue class
	android.content.ContentValues contentValues = new android.content.ContentValues();
	contentValues.put(com.nullcognition.androiddatabaseprogramming.SQLiteOpenHelper_.fieldName, "john d");
	sqLiteDatabase.insert(com.nullcognition.androiddatabaseprogramming.SQLiteOpenHelper_.tableName,
						  com.nullcognition.androiddatabaseprogramming.SQLiteOpenHelper_.fieldName, contentValues);

	// Query using wrapper method
	android.database.Cursor cursor = sqLiteDatabase.query(com.nullcognition.androiddatabaseprogramming.SQLiteOpenHelper_.tableName,
														  new String[]{com.nullcognition.androiddatabaseprogramming.SQLiteOpenHelper_.uid, com.nullcognition.androiddatabaseprogramming.SQLiteOpenHelper_.fieldName},
														  null, null, null, null, null);
	while(cursor.moveToNext()){
	  //get columns indices and values
	  int id = cursor.getInt(cursor.getColumnIndex(com.nullcognition.androiddatabaseprogramming.SQLiteOpenHelper_.uid));
	  String name = cursor.getString(cursor.getColumnIndex(com.nullcognition.androiddatabaseprogramming.SQLiteOpenHelper_.fieldName));
	}
	cursor.close();

	// insert using raw sql query
	String insertQuery = "INSERT INTO " + com.nullcognition.androiddatabaseprogramming.SQLiteOpenHelper_.tableName +
						 " (" + com.nullcognition.androiddatabaseprogramming.SQLiteOpenHelper_.fieldName + ") VALUES ('jack d')";
	sqLiteDatabase.execSQL(insertQuery); // don't want to mess around with the sq language, use a simpler method

	// query using sql select query
	String query = "SELECT " + com.nullcognition.androiddatabaseprogramming.SQLiteOpenHelper_.uid + ", " + com.nullcognition.androiddatabaseprogramming.SQLiteOpenHelper_.fieldName + " FROM " + com.nullcognition.androiddatabaseprogramming.SQLiteOpenHelper_.tableName;
	android.database.Cursor cursor1 = sqLiteDatabase.rawQuery(query, null);
	while(cursor1.moveToNext()){
	  int id = cursor1.getInt(cursor1.getColumnIndex(com.nullcognition.androiddatabaseprogramming.SQLiteOpenHelper_.uid));
	  String name = cursor1.getString(cursor1.getColumnIndex(SQLiteOpenHelper_.fieldName));
	}
	cursor1.close();

	sqLiteDatabase.close();
	sqLiteOpenHelper.close();

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu){
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_main, menu);
	return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item){
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();

	//noinspection SimplifiableIfStatement
	if(id == R.id.action_settings){
	  android.content.Intent sActivity = new android.content.Intent(this, ActivitySchema.class);
	  sActivity.putExtra("sActivity", "value");
	  startActivity(sActivity);
	  return true;
	}

	return super.onOptionsItemSelected(item);
  }
//							millis sec hour day

  /* Checks if external storage is available for read and write */
  public boolean isExternalStorageWritable(){
	String state = android.os.Environment.getExternalStorageState();
	if(android.os.Environment.MEDIA_MOUNTED.equals(state)){
	  return true;
	}
	return false;
  }

  /* Checks if external storage is available to at least read */
  public boolean isExternalStorageReadable(){
	String state = android.os.Environment.getExternalStorageState();
	if(android.os.Environment.MEDIA_MOUNTED.equals(state) || android.os.Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
	  return true;
	}
	return false;
  }

  private void locationCache(){
	android.location.LocationManager locationManager = (android.location.LocationManager)this
	  .getSystemService(android.content.Context.LOCATION_SERVICE);
	android.location.Location lastKnown = locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
	float lat = (float)lastKnown.getLatitude();
	float lon = (float)lastKnown.getLongitude();
	android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
	editor.putFloat("lat", lat).putFloat("lon", lon).commit();

  }


}
