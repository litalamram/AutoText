package com.example.lital.autotext.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DbMethods {
    private static SQLiteDatabase db;

    /**
     * Initialize database
     * @param context context used by DbHelper
     */
    public static void init(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * Insert a new message record
     * @param message  message text
     * @param phoneNum phone number
     * @param date date
     * @return the id of the new record inserted
     */
    public static long insertMessage(String message, String phoneNum, Calendar date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        ContentValues values = new ContentValues();
        values.put(DbContract.Messages.COL_MESSAGE_TXT, message);
        values.put(DbContract.Messages.COL_PHONE_NUM, phoneNum);
        values.put(DbContract.Messages.COL_DATE, formatter.format(date.getTime()));
        long id = db.insert(DbContract.Messages.TABLE_NAME, null, values);
        return id;
    }

    /**
     * Get all messages
     * @return cursor containing the messages
     */
    public static DbCursor getAllMessages() {
        String[] projection = {
                DbContract.Messages._ID,
                DbContract.Messages.COL_MESSAGE_TXT,
                DbContract.Messages.COL_PHONE_NUM,
                DbContract.Messages.COL_DATE
        };
        Cursor c = db.query(DbContract.Messages.TABLE_NAME, projection, null, null, null, null, DbContract.Messages.COL_DATE);
        return new DbCursor(c);
    }

    /**
     * Get a message
     * @param id message's id
     * @return cursor containing the message
     */
    public static DbCursor getMessage(long id) {
        String[] projection = {
                DbContract.Messages.COL_MESSAGE_TXT,
                DbContract.Messages.COL_PHONE_NUM
        };
        String selection = DbContract.Messages._ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor c = db.query(DbContract.Messages.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        return new DbCursor(c);
    }

    /**
     * Delete a message
     * @param id message's id
     * @return The number of rows affected
     */
    public static int deleteMessage(long id) {
        SQLiteDatabase db = DbMethods.db;
        String[] whereArgs = {String.valueOf(id)};
        String whereClause = DbContract.Messages._ID + "=?";
        int count = db.delete(DbContract.Messages.TABLE_NAME, whereClause, whereArgs);
        return count;
    }


}
