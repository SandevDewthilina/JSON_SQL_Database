package com.example.jsonsqldatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "JSON_DATABASE";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        updateMyDatabse(db, 0, DB_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        updateMyDatabse(db, oldVersion, newVersion);

    }

    private void updateMyDatabse(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 1) {

            db.execSQL("CREATE TABLE COSTUMERS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "CODE TEXT, "
                    + "ADDRESS TEXT, "
                    + "PHONE INTEGER);");

        }

    }

    private static void insertDrink(SQLiteDatabase db, String name,
                                    String code, String address, String phone) {
        ContentValues costumerValues = new ContentValues();
        costumerValues.put("NAME", name);
        costumerValues.put("CODE", code);
        costumerValues.put("ADDRESS", address);
        costumerValues.put("PHONE", phone);
        db.insert("COSTUMERS", null, costumerValues);
    }

}

