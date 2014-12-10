package com.nullcognition.chapter6bindingtoui;
/**
 * Created by ersin on 07/12/14 at 8:40 PM
 */
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_WORK;

public class ContactEntry {

   private String name;
   private String number;
   private String type;

   public ContactEntry(String inName, String inNumber, int inType){

	  name = inName;
	  number = inNumber;
	  String numType = "";

	  switch(inType){
		 case TYPE_HOME:
			numType = "HOME";
			break;
		 case TYPE_MOBILE:
			numType = "MOBILE";
			break;
		 case TYPE_WORK:
			numType = "WORK";
		 default:
			numType = "MOBILE";
	  }

	  type = numType;
   }

   public String getName(){return name; }

   public String getType(){return type;}

   public String getNumber(){return number;}
}
