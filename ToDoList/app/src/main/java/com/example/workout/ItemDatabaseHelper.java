package com.example.workout;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDatabaseHelper extends SQLiteOpenHelper {
    // name of the database
    private static final String DB_NAME = "database";
    private static final int DB_VERSION = 1;

    /**
     * Call the constructor of the SQLiteOpenHelper superclass,
     * and passing it the database name and version
     * @param context
     */
    ItemDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    /**
     * A contentValues object describes a set of data.
     * Usually create a new ContentValues object for each row of data we want to create
     * @param db
     * @param item
     * @param description
     */
    public static void AddItem(SQLiteDatabase db, String item, String description, String status) {
        ContentValues itemValues = new ContentValues();
        itemValues.put("ITEM", item);
        itemValues.put("DESCRIPTION", description);
        itemValues.put("STATUS", status);
        // Insert a single row into the table
        db.insert("ITEM_LIST", null, itemValues);
    }

    /**
     * Create the table "ITEM_LIST"
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE ITEM_LIST (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ITEM TEXT, "
                    + "DESCRIPTION TEXT, "
                    + "STATUS TEXT);");
        }
    }
}
