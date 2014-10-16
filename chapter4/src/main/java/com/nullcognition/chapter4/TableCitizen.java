package com.nullcognition.chapter4;
/**
 * Created by ersin on 14/10/14 at 8:31 PM
 */
public class TableCitizen {

  public static final String TABLE_NAME = "citizen_table";

  public static final String COL_ID     = "_id";
  public static final String COL_NAME   = "_name";
  public static final String COL_STATE  = "_state";
  public static final String COL_INCOME = "_income";

  // content uri to provider
  public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://" + ContentProviderCitizen.AUTHORITY + "/citizen");

  // MIME TYPE FOR GROUP OF CITIZENS
  public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.jwei512.citizen";

  // MIME TYPE FOR SINGLE CITIZEN
  public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.jwei512.citizen";

  // RELATIVE POSITION OF CITIZEN SSID IN URI
  public static final int SSID_PATH_POSITION = 1;

}
