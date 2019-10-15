package com.example.lital.autotext.db;

import android.provider.BaseColumns;

/**
 * Contract of database
 */
public class DbContract {
    static final String DB_NAME = "com.example.lital.autotext.db";
    static final int DB_VERSION = 9;
    private static String TYPE_TEXT = " TEXT";
    private static String COMMA = ",";

    //Prevent instantiating the contract class
    private DbContract() {
    }

    /**
     * Messages table definition
     */
    public static class Messages implements BaseColumns {
        public static final String TABLE_NAME = "messages";
        public static final String COL_MESSAGE_TXT = "messageTxt";
        public static final String COL_PHONE_NUM = "phoneNum";
        public static final String COL_DATE = "date";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COL_MESSAGE_TXT + TYPE_TEXT + COMMA +
                        COL_PHONE_NUM + TYPE_TEXT + COMMA +
                        COL_DATE + TYPE_TEXT + ")";

        public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
