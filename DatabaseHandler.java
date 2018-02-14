package com.android_examples.Connect2ControlHome;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

;import java.util.ArrayList;

/**
 * Created by Asha.Devaraja on 11/3/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "asha67.db";
    private static final String TABLE_CONTENTS = "login18";
    private static final String Notification_History = "NotificationTable";
    private static final String KEY_IP = "IP";
    private static final String KEY_PORT = "port";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_CLIENTID = "clientid";
    private static final String KEY_PHONENUMBER = "PhNo";
    private static final String KEY_REGISTERSTATUS = "RegStatus";
    private static final String KEY_COUNTRYCODE = "CountryCode";
    private static final String KEY_ADMINSTATUS = "AdminStatus";
    private static final String KEY_NOTIFICATION = "Notification";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(this.getClass().getCanonicalName(), "creating db ");
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTENTS_TABLE = "CREATE TABLE " + TABLE_CONTENTS + "("
                + KEY_IP + " INTEGER," + KEY_PORT + " INTEGER," + KEY_NAME + " TEXT,"
                + KEY_PASSWORD + " TEXT," + KEY_REGISTERSTATUS + " BOOLEAN," + KEY_ADMINSTATUS + " BOOLEAN," + KEY_PHONENUMBER + " TEXT," + KEY_CLIENTID + " TEXT,"
                + KEY_COUNTRYCODE + " TEXT" + ")";
        Log.i(this.getClass().getCanonicalName(), "creating table ");
        db.execSQL(CREATE_CONTENTS_TABLE);

        String CREATE_CONTENTS_TABLE1 = "CREATE TABLE " + Notification_History  +"(" + KEY_NOTIFICATION + " TEXT"
                +  ")";
        Log.i(this.getClass().getCanonicalName(), "creating table ");
        db.execSQL(CREATE_CONTENTS_TABLE1);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTENTS);
        onCreate(db);
    }


    // Deleting
    public void deleteContent(DatabaseParameters content) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTENTS, null, null );
        db.close();
    }


    // adding
    void addContent(DatabaseParameters content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, content._name); // GlobalData Name
        values.put(KEY_PASSWORD, content._Password); // GlobalData PASSWORD
        values.put(KEY_IP, content._IP);
        values.put(KEY_PORT, content._port);
        values.put(KEY_CLIENTID, content._clientid);
        values.put(KEY_PHONENUMBER, content._Phone_no);
        values.put(KEY_REGISTERSTATUS, content._RegStatus);
        values.put(KEY_ADMINSTATUS, content._isAdmin);
        values.put(KEY_COUNTRYCODE, content._countryCode);

        // Inserting Row
        db.insert(TABLE_CONTENTS, null, values);
        db.close(); // Closing database connection
    }


    //counting
    Integer getNotificationHistoryCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+Notification_History, null).getCount();
    }

    public boolean saveNotificationToDb(String text )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOTIFICATION, text); // GlobalData Name
        // Inserting Row
        db.insert(Notification_History, null, values);
        db.close(); // Closing database connection

         return true;
    }

    public ArrayList<String> readNotificationsFromDb( )
    {

        ArrayList<String> List = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + Notification_History;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                List.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return List;
    }

    public void deleteNotificationHistory()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Notification_History, null, null );
        db.close();
    }
   // SQLiteDatabase db = this.getReadableDatabase();
   // long taskCount = DatabaseUtils.queryNumEntries(db, KEY_NOTIFICATION);
  //  long taskCount = DatabaseUtils.longForQuery(db, "SELECT COUNT (*) FROM " + KEY_NOTIFICATION );



    // Updating
    public int updateContact(DatabaseParameters content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, content._name);
        values.put(KEY_PASSWORD, content._Password);
        values.put(KEY_IP, content._IP);
        values.put(KEY_PORT, content._port);
        values.put(KEY_CLIENTID, content._clientid);
        values.put(KEY_REGISTERSTATUS, content._RegStatus);
        values.put(KEY_ADMINSTATUS, content._isAdmin);
        values.put(KEY_COUNTRYCODE, content._countryCode);

        // updating row
        return db.update(TABLE_CONTENTS, values, KEY_IP + " = ?",
                new String[] { String.valueOf(content._IP) });
    }


    // Getting All Contents
    public DatabaseParameters getAllContents() {

        DatabaseParameters content = new DatabaseParameters();

        String selectQuery = "SELECT  * FROM " + TABLE_CONTENTS;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                content._IP = "172.25.2.39";
                content._port = "8883";
                content._name = cursor.getString(2);
                content._Password = cursor.getString(3);
                content._RegStatus = ((cursor.getInt(4)) == 1);
                content._isAdmin   = ((cursor.getInt(5)) == 1);
                content._Phone_no = cursor.getString(6);
                content._clientid = cursor.getString(7);
                content._countryCode = cursor.getString(8);
                break;
            } while (cursor.moveToNext());
        }

        return content;
    }
}
