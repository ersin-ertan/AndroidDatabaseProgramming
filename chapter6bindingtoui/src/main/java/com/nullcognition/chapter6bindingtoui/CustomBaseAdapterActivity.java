package com.nullcognition.chapter6bindingtoui;

import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class CustomBaseAdapterActivity extends android.app.ListActivity {

   private java.util.List<com.nullcognition.chapter6bindingtoui.ContactEntry> contacts;

   @Override
   public void onCreate(Bundle savedInstanceState){
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.list);

	  String[] projections = new String[]{Phone._ID, Phone.DISPLAY_NAME, Phone.NUMBER, Phone.TYPE};
	  android.database.Cursor c = getContentResolver().query(Phone.CONTENT_URI, projections, null, null, null);
	  startManagingCursor(c);

	  contacts = new java.util.ArrayList<com.nullcognition.chapter6bindingtoui.ContactEntry>();
	  while(c.moveToNext()){
		 int nameCol = c.getColumnIndex(Phone.DISPLAY_NAME);
		 int numCol = c.getColumnIndex(Phone.NUMBER);
		 int typeCol = c.getColumnIndex(Phone.TYPE);

		 String name = c.getString(nameCol);
		 String number = c.getString(numCol);
		 int type = c.getInt(typeCol);
		 contacts.add(new ContactEntry(name, number, type));
	  }

	  ContactBaseAdapter cAdapter = new ContactBaseAdapter(this, contacts);
	  this.setListAdapter(cAdapter);
   }

   @Override
   protected void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id){
	  super.onListItemClick(l, v, position, id);
	  ContactEntry c = contacts.get(position);

	  String name = c.getName();
	  String number = c.getNumber();
	  String type = c.getType();

	  System.out.println("CLICKED ON " + name + " " + number + " " + type);
   }
}
