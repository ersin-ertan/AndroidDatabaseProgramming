package com.nullcognition.androiddatabaseprogramming;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ActivitySchema extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_schema);

	initDatabase();
  }

  private void initDatabase(){
	SchemaHelper sh = new SchemaHelper(this, null, null, - 1); // initialized in the scheme

	long sid1 = sh.addStudent("Jason Wei", "IL", 12);
	long sid2 = sh.addStudent("Du Chung", "AR", 12);
	long sid3 = sh.addStudent("George Tang", "CA", 11);
	long sid4 = sh.addStudent("Mark Bocanegra", "CA", 11);
	long sid5 = sh.addStudent("Bobby Wei", "IL", 12);

	long cid1 = sh.addCourse("Math51");
	long cid2 = sh.addCourse("CS106A");
	long cid3 = sh.addCourse("Econ1A");

	sh.enrollStudentClass((int)sid1, (int)cid1);
	sh.enrollStudentClass((int)sid1, (int)cid2);
	sh.enrollStudentClass((int)sid2, (int)cid2);
	sh.enrollStudentClass((int)sid3, (int)cid1);
	sh.enrollStudentClass((int)sid3, (int)cid2);
	sh.enrollStudentClass((int)sid4, (int)cid3);
	sh.enrollStudentClass((int)sid5, (int)cid2);

	android.database.Cursor c = sh.getStudentsForCourse((int)cid2);

	while(c.moveToNext()){
	  int colid = c.getColumnIndex(com.nullcognition.androiddatabaseprogramming.tables.TableClass.studentId);
	  int sid = c.getInt(colid);
	  System.out.println("STUDENT " + sid + " IS ENROLLED IN COURSE" + cid2);
	}

	// GET STUDENTS FOR COURSE AND FILTER BY GRADE
	java.util.Set<Integer> sids = sh.getStudentsByGradeForCourse((int)cid2, 11);
	for(Integer sid : sids){
	  System.out.println("STUDENT " + sid + " OF GRADE 11 IS ENROLLED IN COURSE " + cid2);
	}

	// TRY REMOVING A COURSE
	sh.removeCourse((int)cid1);

	// SEE IF REMOVAL KEPT SCHEMA CONSISTENT
	c = sh.getCoursesForStudent((int)sid1);
	while(c.moveToNext()){
	  int colid = c.getColumnIndex(com.nullcognition.androiddatabaseprogramming.tables.TableClass.courseId);
	  int cid = c.getInt(colid);
	  System.out.println("STUDENT " + sid1 + " IS ENROLLED IN COURSE " + cid);
	}

  }

  // android debug bridge - adb shell for sql queries and other commands

  @Override
  public boolean onCreateOptionsMenu(Menu menu){
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.menu_activity_schema, menu);
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
	  android.content.Intent basicQuery = new android.content.Intent(this, ActivityBasicQuery.class);
	  basicQuery.putExtra("basicQuery", "value");
	  startActivity(basicQuery);
	  return true;
	}

	return super.onOptionsItemSelected(item);
  }
}
