package com.example.android.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.inventory.data.InventoryContract.InventoryEntry;

/**
 * Database helper for the Inventory app. Manages database creation and version management.
 */

public class InventoryDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = InventoryDBHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "inventory.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 2;

    /**
     * Constructs a new instance of {@link InventoryDBHelper}.
     *
     * @param context of the app
     */
    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.v(LOG_TAG, "Hello from inside the InventoryDBHelper constructor");
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(LOG_TAG, "The DBHelper onCreate method was accessed.");
        // Create a String that contains the SQL statement to create the items table
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + InventoryEntry.TABLE_NAME + " ("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL DEFAULT \"enter prduct name\", "
                + InventoryEntry.COLUMN_PRICE + " REAL NOT NULL DEFAULT 0.0, "
                + InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL DEFAULT \"enter supplier name\", "
                + InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT NOT NULL DEFAULT \"enter phone number\");";
               // + InventoryEntry.COLUMN_SUPPLIER_PHONE_AREA_CODE  + " INTEGER NOT NULL DEFAULT 000, "
              //  + InventoryEntry.COLUMN_SUPPLIER_PHONE_PREFIX + " INTEGER NOT NULL DEFAULT 000, "
             //   + InventoryEntry.COLUMN_SUPPLIER_PHONE_SUFFIX + " INTEGER NOT NULL DEFAULT 0000);";


        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ITEMS_TABLE);
        Log.v(LOG_TAG, "The items table has been created.");
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE items");
            // Create a String that contains the SQL statement to create the items table
            String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + InventoryEntry.TABLE_NAME + " ("
                    + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL DEFAULT \"enter prduct name\", "
                    + InventoryEntry.COLUMN_PRICE + " REAL NOT NULL DEFAULT 0.0, "
                    + InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                    + InventoryEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL DEFAULT \"enter supplier name\", "
                    + InventoryEntry.COLUMN_SUPPLIER_PHONE_AREA_CODE  + " INTEGER NOT NULL DEFAULT 000, "
                    + InventoryEntry.COLUMN_SUPPLIER_PHONE_PREFIX + " INTEGER NOT NULL DEFAULT 000, "
                    + InventoryEntry.COLUMN_SUPPLIER_PHONE_SUFFIX + " INTEGER NOT NULL DEFAULT 0000);";


            // Execute the SQL statement
            db.execSQL(SQL_CREATE_ITEMS_TABLE);
            Log.v(LOG_TAG, "The items table has been created.");
        }
    }
}
