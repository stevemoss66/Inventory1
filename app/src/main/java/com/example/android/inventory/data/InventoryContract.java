package com.example.android.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public final class InventoryContract {
    public static final String LOG_TAG = InventoryContract.class.getSimpleName();

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InventoryContract() { }

    public static final String CONTENT_AUTHORITY = "com.example.android.inventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ITEMS = "items";
    /**
     * Inner class that defines constant values for the items database table.
     * Each entry in the table represents a single item.
     */
    public static final class InventoryEntry implements BaseColumns {
        // Content Uri to locate items table

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
        /**
         * Name of database table for items
         */
        public final static String TABLE_NAME = "items";

        /**
         * Unique ID number for the item (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the product.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME = "product_name";

        /**
         * Item price.
         * <p>
         * Type: REAL
         */
        public final static String COLUMN_PRICE = "price";

        /**
         * Item quantity.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_QUANTITY = "quantity";


        /**
         * Supplier Name.
         * <p>
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";

        /**
         * Supplier Phone Number. This column is removed in version 2 of database.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";

        /**
         * Supplier Phone Area Code.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_PHONE_AREA_CODE = "supplier_phone_area_code";

        /**
         * Supplier Phone Prefix.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_PHONE_PREFIX = "supplier_phone_prefix";

        /**
         * Supplier Phone Suffix.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_PHONE_SUFFIX = "supplier_phone_suffix";

    }
}
