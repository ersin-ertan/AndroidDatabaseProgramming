package com.nullcognition.chapter6bindingtoui;

import android.provider.ContactsContract.CommonDataKinds.Phone;

/**
 * Created by ersin on 07/12/14 at 9:50 PM
 */
public class CustomContactsAdapter extends android.widget.SimpleCursorAdapter {

   private int layout;

   public CustomContactsAdapter(android.content.Context context, int layout, android.database.Cursor c, String[] from, int[] to){
	  super(context, layout, c, from, to);
	  this.layout = layout;
   }

   @Override
   public android.view.View newView(android.content.Context context, android.database.Cursor cursor, android.view.ViewGroup parent){
	  final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(context);
	  android.view.View v = inflater.inflate(layout, parent, false);
	  return v;
   }

   @Override
   public void bindView(android.view.View v, android.content.Context context, android.database.Cursor c){
	  int nameCol = c.getColumnIndex(Phone.DISPLAY_NAME);
	  int numCol = c.getColumnIndex(Phone.NUMBER);
	  int typeCol = c.getColumnIndex(Phone.TYPE);

	  String name = c.getString(nameCol);
	  String number = c.getString(numCol);
	  int type = c.getInt(typeCol);

	  String numType = "";
	  switch(type){
		 case Phone.TYPE_HOME:
			numType = "HOME";
			break;
		 case Phone.TYPE_MOBILE:
			numType = "MOBILE";
			break;
		 case Phone.TYPE_WORK:
			numType = "WORK";
			break;
		 default:
			numType = "MOBILE";
			break;
	  }

	  android.widget.TextView name_text = (android.widget.TextView)v.findViewById(R.id.name_entry);
	  name_text.setText(name);

	  android.widget.TextView number_text = (android.widget.TextView)v.findViewById(R.id.number_entry);
	  number_text.setText(number);

	  android.widget.TextView type_text = (android.widget.TextView)v.findViewById(R.id.number_type_entry);
	  type_text.setText(numType);
   }
}
