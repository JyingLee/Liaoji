package com.jying.rainbow.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jying on 2017/9/9.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "user.db";
    public static final int DB_VERSIONN = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSIONN);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(user_id integer primary key autoincrement,username text NOT NULL,password text NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
}
