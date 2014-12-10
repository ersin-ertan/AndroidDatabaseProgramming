package com.nullcognition.chapter6bindingtoui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import static android.provider.ContactsContract.CommonDataKinds.Phone;

public class SimpleContactsActivity extends android.app.ListActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState){
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.list);

	  initListWithCursor();
   }

   private void initListWithCursor(){
	  String[] projection = new String[]{Phone._ID, Phone.DISPLAY_NAME, Phone.NUMBER, Phone.TYPE};
	  android.database.Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, projection, null, null, null);
	  startManagingCursor(cursor);

	  String[] columns = new String[]{Phone.DISPLAY_NAME, Phone.NUMBER, Phone.TYPE};
	  int[] to = new int[]{R.id.name_entry, R.id.number_entry, R.id.number_type_entry};

	  android.widget.SimpleCursorAdapter cAdapter = new android.widget.SimpleCursorAdapter(this, R.layout.list_entry, cursor, columns, to);
	  setListAdapter(cAdapter);
   }


   @Override
   public boolean onCreateOptionsMenu(Menu menu){
	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(R.menu.menu_simple_contacts, menu);
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
	  if(id == R.id.action_next){
		 android.content.Intent nextActivity = new android.content.Intent(this, com.nullcognition.chapter6bindingtoui.CustomContactsActivity.class);
		 nextActivity.putExtra("nextActivity", "value");
		 startActivity(nextActivity);
		 return true;
	  }

	  return super.onOptionsItemSelected(item);
   }
}
