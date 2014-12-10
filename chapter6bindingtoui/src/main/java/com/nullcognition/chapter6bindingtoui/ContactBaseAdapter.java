package com.nullcognition.chapter6bindingtoui;
/**
 * Created by ersin on 07/12/14 at 9:44 PM
 */

public class ContactBaseAdapter extends android.widget.BaseAdapter {

   private android.view.LayoutInflater mInflater;

   private java.util.List<com.nullcognition.chapter6bindingtoui.ContactEntry> mItems = new java.util.ArrayList<com.nullcognition.chapter6bindingtoui.ContactEntry>();

   public ContactBaseAdapter(android.content.Context context, java.util.List<com.nullcognition.chapter6bindingtoui.ContactEntry> items){
	  mInflater = android.view.LayoutInflater.from(context);
	  mItems = items;
   }

   public int getCount(){
	  return mItems.size();
   }

   public Object getItem(int position){
	  return mItems.get(position);
   }

   public long getItemId(int position){
	  return position;
   }

   public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent){
	  ContactViewHolder holder;

	  if(convertView == null){
		 convertView = mInflater.inflate(R.layout.list_entry, null);

		 holder = new ContactViewHolder();
		 holder.name_entry = (android.widget.TextView)convertView.findViewById(R.id.name_entry);
		 holder.number_entry = (android.widget.TextView)convertView.findViewById(R.id.number_entry);
		 holder.type_entry = (android.widget.TextView)convertView.findViewById(R.id.number_type_entry);

		 convertView.setTag(holder);
	  }
	  else{
		 holder = (ContactViewHolder)convertView.getTag();
	  }

	  ContactEntry c = mItems.get(position);
	  holder.name_entry.setText(c.getName());
	  holder.number_entry.setText(c.getNumber());
	  holder.type_entry.setText(c.getType());

	  return convertView;
   }

   static class ContactViewHolder {

	  android.widget.TextView name_entry;
	  android.widget.TextView number_entry;
	  android.widget.TextView type_entry;
   }
}
