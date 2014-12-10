package com.nullcognition.chapter6bindingtoui;

import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class CustomContactsActivity extends android.app.ListActivity {

   private CustomContactsAdapter cAdapter;

   @Override
   public void onCreate(Bundle savedInstanceState){
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.list);

	  String[] projections = new String[]{Phone._ID, Phone.DISPLAY_NAME, Phone.NUMBER, Phone.TYPE};
	  android.database.Cursor c = getContentResolver().query(Phone.CONTENT_URI, projections, null, null, null);
	  startManagingCursor(c);

	  String[] columns = new String[]{Phone.DISPLAY_NAME, Phone.NUMBER, Phone.TYPE};

	  int[] to = new int[]{R.id.name_entry, R.id.number_entry, R.id.number_type_entry};

	  cAdapter = new CustomContactsAdapter(this, R.layout.list_entry, c, columns, to);

	  this.setListAdapter(cAdapter);
   }

   @Override
   public boolean onCreateOptionsMenu(android.view.Menu menu){
	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(R.menu.menu_custom_contacts, menu);
	  return true;
   }

   @Override
   public boolean onOptionsItemSelected(android.view.MenuItem item){
	  // Handle action bar item clicks here. The action bar will
	  // automatically handle clicks on the Home/Up button, so long
	  // as you specify a parent activity in AndroidManifest.xml.
	  int id = item.getItemId();

	  //noinspection SimplifiableIfStatement
	  if(id == R.id.action_settings){
		 return true;
	  }
	  if(id == R.id.action_next){
		 android.content.Intent nextActivity = new android.content.Intent(this, com.nullcognition.chapter6bindingtoui.CustomBaseAdapterActivity.class);
		 nextActivity.putExtra("nextActivity", "value");
		 startActivity(nextActivity);
		 return true;
	  }

	  return super.onOptionsItemSelected(item);
   }

   @Override
   protected void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id){
	  super.onListItemClick(l, v, position, id);
	  android.database.Cursor c = (android.database.Cursor)cAdapter.getItem(position);

	  int nameCol = c.getColumnIndex(Phone.DISPLAY_NAME);
	  int numCol = c.getColumnIndex(Phone.NUMBER);
	  int typeCol = c.getColumnIndex(Phone.TYPE);

	  String name = c.getString(nameCol);
	  String number = c.getString(numCol);
	  int type = c.getInt(typeCol);

	  System.out.println("CLICKED ON " + name + " " + number + " " + type);
   }
}
