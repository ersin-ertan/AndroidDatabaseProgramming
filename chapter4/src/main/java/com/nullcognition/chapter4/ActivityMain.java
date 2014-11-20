package com.nullcognition.chapter4;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ActivityMain extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	android.content.ContentResolver cr = getContentResolver();
	android.content.ContentValues contentValue = new android.content.ContentValues();

	contentValue.put(com.nullcognition.chapter4.TableCitizen.COL_NAME, "Jason Wei");
	contentValue.put(com.nullcognition.chapter4.TableCitizen.COL_STATE, "CA");
	contentValue.put(com.nullcognition.chapter4.TableCitizen.COL_INCOME, 100000);
	cr.insert(TableCitizen.CONTENT_URI, contentValue);

	contentValue = new android.content.ContentValues();
	contentValue.put(com.nullcognition.chapter4.TableCitizen.COL_NAME, "James Lee");
	contentValue.put(com.nullcognition.chapter4.TableCitizen.COL_STATE, "NY");
	contentValue.put(com.nullcognition.chapter4.TableCitizen.COL_INCOME, 120000);
	cr.insert(TableCitizen.CONTENT_URI, contentValue);

	contentValue = new android.content.ContentValues();
	contentValue.put(com.nullcognition.chapter4.TableCitizen.COL_NAME, "Daniel Lee");
	contentValue.put(com.nullcognition.chapter4.TableCitizen.COL_STATE, "NY");
	contentValue.put(com.nullcognition.chapter4.TableCitizen.COL_INCOME, 80000);
	cr.insert(TableCitizen.CONTENT_URI, contentValue);
// QUERY TABLE FOR ALL COLUMNS AND ROWS
	android.database.Cursor c = cr.query(TableCitizen.CONTENT_URI, null, null, null, com.nullcognition.chapter4.TableCitizen.COL_INCOME + " ASC");
// LET THE ACTIVITY MANAGE THE CURSOR
	startManagingCursor(c);
	int idCol = c.getColumnIndex(com.nullcognition.chapter4.TableCitizen.COL_ID);
	int nameCol = c.getColumnIndex(com.nullcognition.chapter4.TableCitizen.COL_NAME);
	int stateCol = c.getColumnIndex(com.nullcognition.chapter4.TableCitizen.COL_STATE);
	int incomeCol = c.getColumnIndex(com.nullcognition.chapter4.TableCitizen.COL_INCOME);

	while(c.moveToNext()){
	  int id = c.getInt(idCol);
	  String name = c.getString(nameCol);
	  String state = c.getString(stateCol);
	  int income = c.getInt(incomeCol);
	  System.out.println("RETRIEVED ||" + id + "||" + name +
						 "||" + state + "||" + income);
	}
	System.out.println("-------------------------------");
// QUERY BY A SPECIFIC ID
	android.net.Uri myC = android.net.Uri.withAppendedPath(TableCitizen.CONTENT_URI, "2");
	android.database.Cursor c1 = cr.query(myC, null, null, null, null);
// LET THE ACTIVITY MANAGE THE CURSOR
	startManagingCursor(c1);
	while(c1.moveToNext()){
	  int id = c1.getInt(idCol);
	  String name = c1.getString(nameCol);
	  String state = c1.getString(stateCol);
	  int income = c1.getInt(incomeCol);
	  System.out.println("RETRIEVED ||" + id + "||" + name +
						 "||" + state + "||" + income);
	}
  }




  @Override
  public boolean onCreateOptionsMenu(Menu menu){
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.menu_activity_main, menu);
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
