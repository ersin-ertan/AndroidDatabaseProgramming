package com.nullcognition.chapter4;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ContactsQueryActivity extends Activity {

   java.util.Map<String, String> lookupsCached;

   @Override
   protected void onCreate(Bundle savedInstanceState){
	  super.onCreate(savedInstanceState);

	  setContentView(R.layout.activity_contacts_query);

   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu){

	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(R.menu.menu_contacts_query, menu);
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

   private void queryExample(){

	  android.database.Cursor cursor = getContentResolver().query(android.provider.ContactsContract.Contacts.CONTENT_URI, new String[]{android.provider.ContactsContract.Contacts._ID, android.provider.ContactsContract.Contacts.DISPLAY_NAME, android.provider.ContactsContract.Contacts.LOOKUP_KEY}, android.provider.ContactsContract.Contacts.DISPLAY_NAME + " IS NOT NULL", null, null);

	  startManagingCursor(cursor);

	  int idCol = cursor.getColumnIndex(android.provider.ContactsContract.Contacts._ID);
	  int nameCol = cursor.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME);
	  int lookCol = cursor.getColumnIndex(android.provider.ContactsContract.Contacts.LOOKUP_KEY);

	  java.util.Map<String, String> lookups = new java.util.HashMap<String, String>();

	  while(cursor.moveToNext()){

		 int id = cursor.getInt(idCol);
		 String name = cursor.getString(nameCol);
		 String lookup = cursor.getString(lookCol);

		 lookups.put(name, lookup);

		 android.util.Log.e(getClass().getSimpleName(), "Got " + id + " // " + lookup + " // " + name + " FROM CONTACTS");
	  }

   }

   private void queryWithCachedLookup(){

	  android.net.Uri lookupUri = android.net.Uri.withAppendedPath(android.provider.ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupsCached.get("Vicky Wei"));

	  android.database.Cursor c3 = getContentResolver().query(lookupUri, new String[]{android.provider.ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);

	  if(c3.moveToFirst()){

		 int nameCol = c3.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME);
		 String displayName = c3.getString(nameCol);

		 System.out.println("GOT NAME " + displayName + " FOR LOOKUP KEY " + lookupsCached.get("Vicky Wei"));
	  }

	  c3.close();

   }

   private void queryForSpecificData(){

	  android.database.Cursor cursor = getContentResolver().query(android.provider.ContactsContract.Data.CONTENT_URI, new String[]{android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER, android.provider.ContactsContract.CommonDataKinds.Phone.TYPE}, android.provider.ContactsContract.Data.LOOKUP_KEY + "=?", new String[]{lookupsCached.get("Vicky Wei")}, null);

	  startManagingCursor(cursor);

	  int numberCol = cursor.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER);
	  int typeCol = cursor.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Phone.TYPE);

	  if(cursor.moveToFirst()){

		 String number = cursor.getString(numberCol);
		 int type = cursor.getInt(typeCol);
		 String strType = "";

		 switch(type){
			case android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
			   strType = "HOME";
			   break;
			case android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
			   strType = "MOBILE";
			   break;
			case android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
			   strType = "WORK";
			   break;
			default:
			   strType = "MOBILE";
			   break;
		 }

		 System.out.println("GOT NUMBER " + number + " OF TYPE " + strType + " FOR VICKY WEI");

	  }

   }

   private void insertPhoneNumber(){

	  android.content.ContentValues values = new android.content.ContentValues();
// IN THIS CASE - EACH RAW ID IS JUST THE CONTACT ID
	  values.put(android.provider.ContactsContract.Data.RAW_CONTACT_ID, 2);
	  values.put(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, android.provider.ContactsContract.RawContacts.Data.MIMETYPE);
	  values.put(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER, "555-987-1234");
	  values.put(android.provider.ContactsContract.CommonDataKinds.Phone.TYPE, android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
	  android.net.Uri contactUri = getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
	  android.database.Cursor c4 = getContentResolver().query(contactUri, new String[]{android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER, android.provider.ContactsContract.CommonDataKinds.Phone.TYPE}, null, null, null);

	  int numberCol = c4.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER);
	  int typeCol = c4.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Phone.TYPE);

	  startManagingCursor(c4);
// READ BACK THE ROW
	  if(c4.moveToFirst()){

		 String number = c4.getString(numberCol);
		 int type = c4.getInt(typeCol);
		 String strType = "";

		 switch(type){
			case android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
			   strType = "HOME";
			   break;
			case android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
			   strType = "MOBILE";
			   break;
			case android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_WORK: strType = "WORK";
			   break;
			default:
			   strType = "MOBILE";
			   break;
		 }
		 System.out.println("GOT NUMBER " + number + " OF TYPE " + strType + " FOR VICKY WEI");
	  }
   }

   private void batchInsert(){
	  // NOW INSERT USING BATCH OPERATIONS
	  java.util.ArrayList<android.content.ContentProviderOperation> ops = new java.util.ArrayList<android.content.ContentProviderOperation>();

	  ops.add(android.content.ContentProviderOperation
				.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
				.withValue(android.provider.ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 3)
				.withValue(android.provider.ContactsContract.RawContacts.Data.MIMETYPE, android.provider.ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
				.withValue(android.provider.ContactsContract.CommonDataKinds.Email.DATA, "daniel@stanford.edu")
				.withValue(android.provider.ContactsContract.CommonDataKinds.Email.TYPE, android.provider.ContactsContract.CommonDataKinds.Email.TYPE_WORK)
				.build());
	  try{
		 getContentResolver().applyBatch(android.provider.ContactsContract.AUTHORITY, ops);
	  }
	  catch(Exception e){
		 e.printStackTrace();
		 System.out.println("ERROR: BATCH TRANSACTION FAILED");
	  }
   }

   private void updateWithBatchInsert(){

	  java.util.ArrayList<android.content.ContentProviderOperation> ops = new java.util.ArrayList<android.content.ContentProviderOperation>();

	  ops.add(android.content.ContentProviderOperation
				.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
				.withSelection(android.provider.ContactsContract.RawContacts.Data.RAW_CONTACT_ID + "=? AND " + android.provider.ContactsContract.CommonDataKinds.Email.TYPE + "=?", new String[]{"7", String.valueOf(android.provider.ContactsContract.CommonDataKinds.Email.TYPE_WORK)})
				.withValue(android.provider.ContactsContract.CommonDataKinds.Email.DATA, "james@android.com")
				.build());

	  try {
		 getContentResolver().applyBatch(android.provider.ContactsContract.AUTHORITY, ops);
	  } catch (Exception e) {
		 e.printStackTrace();
		 System.out.println("ERROR: BATCH TRANSACTION FAILED");
	  }
   }


   private void queryRawContactsTable(){

	  android.database.Cursor c = getContentResolver().query(android.provider.ContactsContract.RawContacts.CONTENT_URI, new String[]{android.provider.ContactsContract.RawContacts._ID, android.provider.ContactsContract.RawContacts.ACCOUNT_NAME, android.provider.ContactsContract.RawContacts.ACCOUNT_TYPE, android.provider.ContactsContract.RawContacts.CONTACT_ID}, null, null, null);

	  startManagingCursor(c);

	  int rawIdCol = c.getColumnIndex(android.provider.ContactsContract.RawContacts._ID);
	  int accNameCol = c.getColumnIndex(android.provider.ContactsContract.RawContacts.ACCOUNT_NAME);
	  int accTypeCol = c.getColumnIndex(android.provider.ContactsContract.RawContacts.ACCOUNT_TYPE);
	  int contactIdCol = c.getColumnIndex(android.provider.ContactsContract.RawContacts.CONTACT_ID);

	  while(c.moveToNext()){

		 int rawId = c.getInt(rawIdCol);
		 String accName = c.getString(accNameCol);
		 String accType = c.getString(accTypeCol);
		 int contactId = c.getInt(contactIdCol);
		 System.out.println("GOT " + rawId + " // " + accName + " // " + accType + " REFRENCING CONTACT " + contactId);
	  }
   }
}
