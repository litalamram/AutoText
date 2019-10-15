package com.example.lital.autotext.db;

import android.database.Cursor;
import android.database.CursorWrapper;

public class DbCursor extends CursorWrapper {

    DbCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the message text field from the cursor
     */
    public String getMessageTxt() {
        Cursor cursor = getWrappedCursor();
        if (cursor.moveToFirst() && cursor.getCount() >= 1) {
            return cursor.getString(cursor.getColumnIndex(DbContract.Messages.COL_MESSAGE_TXT));
        }
        return null;
    }

    /**
     * Get phone number field from the cursor
     */
    public String getMessagePhoneNum() {
        Cursor cursor = getWrappedCursor();
        if (cursor.moveToFirst() && cursor.getCount() >= 1) {
            return cursor.getString(cursor.getColumnIndex(DbContract.Messages.COL_PHONE_NUM));
        }
        return null;
    }
}
