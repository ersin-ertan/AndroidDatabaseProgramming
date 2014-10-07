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
  private long                              lastUpdateTime    = sharedPreferences.getLong("lastupdatekey", 0L);
  private long                              timeElapsed       = System.currentTimeMillis() - lastUpdateTime;

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

	locationCache();

	getStringSetForMultipleStrings();

	updateMe();
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

  private void locationCache(){
	android.location.LocationManager locationManager = (android.location.LocationManager)this
	  .getSystemService(android.content.Context.LOCATION_SERVICE);
	android.location.Location lastKnown = locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
	float lat = (float)lastKnown.getLatitude();
	float lon = (float)lastKnown.getLongitude();
	android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
	editor.putFloat("lat", lat).putFloat("lon", lon).commit();

  }

  private void getStringSetForMultipleStrings(){
	java.util.Set<String> mySet = new java.util.HashSet<String>();
	mySet.add("one");
	mySet.add("two");
	android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
	editor.putStringSet("mySS", mySet).commit();

	mySet = sharedPreferences.getStringSet("mySS", new java.util.HashSet<String>(0)); // may be smart to set the capasity of a default to 0
  }
//							millis sec hour day

  private void updateMe(){
	if(timeElapsed > update_freq){ //update
	  android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
	  editor.putLong("lastupdatekey", System.currentTimeMillis()).commit();
	}
	// this may be used to cache a users name for a edit text, or remember the applications state ex.changed to silent mode from settings
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
	  return true;
	}

	return super.onOptionsItemSelected(item);
  }


}
