package com.nullcognition.androiddatabaseprogramming;
/**
 * Created by ersin on 10/10/14 at 7:45 PM
 */
public class SchemaHelper extends android.database.sqlite.SQLiteOpenHelper {

  private static final String databaseName    = "advDatabase.db";
  private static final int    databaseVersion = 1;

  public SchemaHelper(android.content.Context context, String name, android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version){
	// may or may not take in params, do for generic schema of SQLiteOpenHelper
	super(context, databaseName, null, databaseVersion);
  }

  public long addStudent(String name, String state, int grade){

	android.content.ContentValues cv = new android.content.ContentValues();
	cv.put(com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentName, name);
	cv.put(com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentState, state);
	cv.put(com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentGrade, grade);

	android.database.sqlite.SQLiteDatabase sd = getWritableDatabase();
	long result = sd.insert(com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentTableName,
							com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentName, cv);
	return result;
  }

  @Override
  public void onCreate(android.database.sqlite.SQLiteDatabase inSQLiteDatabase){

	inSQLiteDatabase.execSQL("CREATE TABLE " + com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentTableName +
							 " (" + com.nullcognition.androiddatabaseprogramming.tables.TableStudent.id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
							 com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentName + " TEXT, " +
							 com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentState + " TEXT, " +
							 com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentGrade + " INTEGER); ");

	inSQLiteDatabase.execSQL("CREATE TABLE " + com.nullcognition.androiddatabaseprogramming.tables.TableCourse.courseTableName +
							 " (" + com.nullcognition.androiddatabaseprogramming.tables.TableCourse.id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
							 com.nullcognition.androiddatabaseprogramming.tables.TableCourse.courseName + " TEXT);");

	inSQLiteDatabase.execSQL("CREATE TABLE " + com.nullcognition.androiddatabaseprogramming.tables.TableClass.classTableName +
							 " (" + com.nullcognition.androiddatabaseprogramming.tables.TableClass.id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
							 com.nullcognition.androiddatabaseprogramming.tables.TableClass.studentId + " INTEGER," +
							 com.nullcognition.androiddatabaseprogramming.tables.TableClass.courseId + " INTEGER);");
  }

  public long addCourse(String name){

	android.content.ContentValues cv = new android.content.ContentValues();
	cv.put(com.nullcognition.androiddatabaseprogramming.tables.TableCourse.courseName, name);

	android.database.sqlite.SQLiteDatabase sd = getWritableDatabase();
	long result = sd.insert(com.nullcognition.androiddatabaseprogramming.tables.TableCourse.courseTableName,
							com.nullcognition.androiddatabaseprogramming.tables.TableCourse.courseName, cv);
	return result;
  }

  @Override
  public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion){

	db.execSQL("DROP TABLE IF EXISTS " + com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentTableName);
	db.execSQL("DROP TABLE IF EXISTS " + com.nullcognition.androiddatabaseprogramming.tables.TableClass.classTableName);
	db.execSQL("DROP TABLE IF EXISTS " + com.nullcognition.androiddatabaseprogramming.tables.TableCourse.courseTableName);

	onCreate(db);
  }

  public boolean enrollStudentClass(int studentId, int courseId){

	android.content.ContentValues cv = new android.content.ContentValues();
	cv.put(com.nullcognition.androiddatabaseprogramming.tables.TableClass.studentId, studentId);
	cv.put(com.nullcognition.androiddatabaseprogramming.tables.TableClass.courseId, courseId);

	android.database.sqlite.SQLiteDatabase sd = getWritableDatabase();
	long result = sd.insert(com.nullcognition.androiddatabaseprogramming.tables.TableClass.classTableName,
							com.nullcognition.androiddatabaseprogramming.tables.TableClass.studentId, cv);
	return (result >= 0);
  }

  public android.database.Cursor getStudentsForCourse(int courseId){

	android.database.sqlite.SQLiteDatabase sd = getWritableDatabase();
	String[] cols = new String[]{com.nullcognition.androiddatabaseprogramming.tables.TableClass.studentId};
	String[] selectionArgs = new String[]{String.valueOf(courseId)};

	android.database.Cursor c = sd.query(com.nullcognition.androiddatabaseprogramming.tables.TableClass.classTableName, cols,
										 com.nullcognition.androiddatabaseprogramming.tables.TableClass.courseId + "= ?", selectionArgs, null, null,
										 null);
	return c;
  }

  public android.database.Cursor getCoursesForStudent(int studentId){

	android.database.sqlite.SQLiteDatabase sd = getWritableDatabase();

	String[] cols = new String[]{com.nullcognition.androiddatabaseprogramming.tables.TableClass.courseId};
	String[] selectionArgs = new String[]{String.valueOf(studentId)};

	android.database.Cursor c = sd.query(com.nullcognition.androiddatabaseprogramming.tables.TableClass.classTableName, cols,
										 com.nullcognition.androiddatabaseprogramming.tables.TableClass.studentId + "= ?", selectionArgs, null, null,
										 null);
	return c;
  }

  public java.util.Set<Integer> getStudentsByGradeForCourse(int courseId, int grade){

	android.database.sqlite.SQLiteDatabase sd = getWritableDatabase();

	String[] cols = new String[]{com.nullcognition.androiddatabaseprogramming.tables.TableClass.studentId};
	String[] selectionArgs = new String[]{String.valueOf(courseId)};

	android.database.Cursor c = sd.query(com.nullcognition.androiddatabaseprogramming.tables.TableClass.classTableName, cols,
										 com.nullcognition.androiddatabaseprogramming.tables.TableClass.courseId + "= ?", selectionArgs, null, null,
										 null);

	java.util.Set<Integer> returnIds = new java.util.HashSet<Integer>();

	while(c.moveToNext()){

	  int id = c.getInt(c.getColumnIndex(com.nullcognition.androiddatabaseprogramming.tables.TableClass.studentId));
	  returnIds.add(id);
	}

	cols = new String[]{com.nullcognition.androiddatabaseprogramming.tables.TableStudent.id};
	selectionArgs = new String[]{String.valueOf(grade)};
	c = sd.query(com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentTableName, cols,
				 com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentGrade + "= ?", selectionArgs, null, null, null);

	java.util.Set<Integer> gradeIds = new java.util.HashSet<Integer>();

	while(c.moveToNext()){
	  int id = c.getInt(c.getColumnIndex(com.nullcognition.androiddatabaseprogramming.tables.TableStudent.id));
	  gradeIds.add(id);
	}

	returnIds.retainAll(gradeIds); // returns values that union return and grade
	return returnIds;
  }

  public boolean removeStudent(int studentId){

	android.database.sqlite.SQLiteDatabase sd = getWritableDatabase();

	String[] whereArgs = new String[]{String.valueOf(studentId)};
	sd.delete(com.nullcognition.androiddatabaseprogramming.tables.TableClass.classTableName,
			  com.nullcognition.androiddatabaseprogramming.tables.TableClass.studentId + "= ? ", whereArgs);

	int result = sd.delete(com.nullcognition.androiddatabaseprogramming.tables.TableStudent.studentTableName,
						   com.nullcognition.androiddatabaseprogramming.tables.TableStudent.id + "= ? ", whereArgs);
	return (result > 0);
  }

  public boolean removeCourse(int courseId){

	android.database.sqlite.SQLiteDatabase sd = getWritableDatabase();
	String[] whereArgs = new String[]{String.valueOf(courseId)};
	// MAKE SURE YOU REMOVE COURSE FROM ALL STUDENTS ENROLLED

	sd.delete(com.nullcognition.androiddatabaseprogramming.tables.TableClass.classTableName,
			  com.nullcognition.androiddatabaseprogramming.tables.TableClass.courseId + "= ? ", whereArgs);

	int result = sd.delete(com.nullcognition.androiddatabaseprogramming.tables.TableCourse.courseTableName,
						   com.nullcognition.androiddatabaseprogramming.tables.TableCourse.id + "= ? ", whereArgs);
	return (result > 0);
  }


}


