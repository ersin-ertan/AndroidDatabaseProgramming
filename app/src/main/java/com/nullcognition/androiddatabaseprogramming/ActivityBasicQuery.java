package com.nullcognition.androiddatabaseprogramming;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ActivityBasicQuery extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_basic_query);

	SqlQueries();
  }

  private void SqlQueries(){

	SchemaHelper sch = new SchemaHelper(this, null, null, - 1);
	android.database.sqlite.SQLiteDatabase sqdb = sch.getWritableDatabase();

	System.out.println("METHOD 1");
	// METHOD #1 - SQLITEDATABASE RAWQUERY()

	android.database.Cursor c = sqdb
	  .rawQuery("SELECT * from " + com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentTableName, null);

	while(c.moveToNext()){
	  int colid = c.getColumnIndex(com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentName);
	  String name = c.getString(colid);
	  System.out.println("GOT STUDENT " + name);
	}

	System.out.println("METHOD 2");
	// METHOD #2 - SQLITEDATABASE QUERY()

	c = sqdb.query(com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentTableName, null, null, null, null, null, null);

	while(c.moveToNext()){
	  int colid = c.getColumnIndex(com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentName);
	  String name = c.getString(colid);
	  System.out.println("GOT STUDENT " + name);
	}

	System.out.println("METHOD 3");
	// METHOD #3 - SQLITEQUERYBUILDER

	String query = android.database.sqlite.SQLiteQueryBuilder
	  .buildQueryString(false, com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentTableName, null, null, null, null, null, null);
	System.out.println(query);
	c = sqdb.rawQuery(query, null);

	while(c.moveToNext()){
	  int colid = c.getColumnIndex(com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentName);
	  String name = c.getString(colid);
	  System.out.println("GOT STUDENT " + name);
	}
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu){
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.menu_activity_basic_query, menu);
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
