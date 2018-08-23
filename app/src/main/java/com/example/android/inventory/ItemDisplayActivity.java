package com.example.android.inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventory.data.InventoryContract.InventoryEntry;
import com.example.android.inventory.data.InventoryProvider;

import org.w3c.dom.Text;

public class ItemDisplayActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    /** Identifier for the item data loader */
    private static final int EXISTING_ITEM_LOADER = 0;

    /** Content URI for the existing item (null if it's a new item) */
    private Uri mCurrentItemUri;

public static final String LOG_TAG = ItemDisplayActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_display);

        // Get the item Uri
        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        // Setup button to open EditorActivity
        Button editItemButton = (Button) findViewById(R.id.item_display_edit_button);
        editItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemDisplayActivity.this, EditorActivity.class);
               // Uri currentItemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(mCurrentItemUri);
                startActivity(intent);
            }
        });


        setTitle(getString(R.string.item_display_activity_title));

            // Initialize a loader to read the item data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);

        // Find all relevant views that we will need to read user input from
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new item, hide the "Delete" menu item.
        MenuItem checkMark = menu.findItem(R.id.action_save);
        checkMark.setVisible(false);
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }




    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all item attributes, define a projection that contains
        // all columns from the item table
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_PHONE_AREA_CODE,
        InventoryEntry.COLUMN_SUPPLIER_PHONE_PREFIX,
        InventoryEntry.COLUMN_SUPPLIER_PHONE_SUFFIX};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentItemUri,         // Query the content URI for the current item
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of item attributes that we're interested in
            int productNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneAreaCodeColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE_AREA_CODE);
            int supplierPhonePrefixColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE_PREFIX);
            int supplierPhoneSuffixColumnIndex = cursor.getColumnIndex((InventoryEntry.COLUMN_SUPPLIER_PHONE_SUFFIX));

            // Extract out the value from the Cursor for the given column index
            String productName = cursor.getString(productNameColumnIndex);
            Double price = cursor.getDouble(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            final int supplierPhoneAreaCode = cursor.getInt(supplierPhoneAreaCodeColumnIndex);
            final int supplierPhonePrefix = cursor.getInt(supplierPhonePrefixColumnIndex);
            final int supplierPhoneSuffix = cursor.getInt(supplierPhoneSuffixColumnIndex);


            // Update the views on the screen with the values from the database
           TextView productNameDisplayTextView = (TextView) findViewById(R.id.product_name_display_textview);
            TextView priceDisplayTextView = (TextView) findViewById(R.id.price_display_textview);
            TextView quantityDisplayTextView = (TextView) findViewById(R.id.quantity_display_textview);
            TextView supplierNameDisplayTextView = (TextView) findViewById(R.id.supplier_name_display_textview);
            TextView supplierPhoneAreaCodeDisplayTextView = (TextView) findViewById(R.id.supplier_phone_area_code_display_textview);
            TextView supplierPhonePrefixDisplayTextView = (TextView) findViewById(R.id.supplier_phone_prefix_display_textview);
            TextView supplierPhoneSuffixDisplayTextView = (TextView) findViewById(R.id.supplier_phone_suffix_display_textview);

            //convert price to string so it can be displayed
            String priceString = Double.toString(price);
            //if there is only one digit after decimal point, add a zero
            int decimalPoint = priceString.indexOf(".");
            String cents = priceString.substring((priceString.length() - decimalPoint + 1), priceString.length());
            if(cents.length() < 2){
                priceString = priceString.concat("0");}

            productNameDisplayTextView.setText(productName);
            priceDisplayTextView.setText(priceString);
            quantityDisplayTextView.setText(Integer.toString(quantity));
            supplierNameDisplayTextView.setText(supplierName);
          supplierPhoneAreaCodeDisplayTextView.setText(Integer.toString(supplierPhoneAreaCode));
          supplierPhonePrefixDisplayTextView.setText(Integer.toString(supplierPhonePrefix));
          supplierPhoneSuffixDisplayTextView.setText(Integer.toString(supplierPhoneSuffix));

            Button sellItemFromDetails = (Button) findViewById(R.id.item_display_sale_button);

            Log.v(LOG_TAG, "mCurrentItemUri: " + mCurrentItemUri.toString());
            int reducedQuantity = quantity - 1;
            if(reducedQuantity < 1){
                reducedQuantity = 0;
            }
            final int finalReducedQuantity = reducedQuantity;
            final int increasedQuantity = quantity + 1;

            sellItemFromDetails.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ContentValues values = new ContentValues();
                    values.put(InventoryEntry.COLUMN_QUANTITY, finalReducedQuantity);
                    int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
                }
            });
            Button receiveItemFromDetails = (Button) findViewById(R.id.item_display_receive_button);
            receiveItemFromDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues values = new ContentValues();
                    values.put(InventoryEntry.COLUMN_QUANTITY, increasedQuantity);
                    int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
                }
            });

            Button orderFromSupplier = (Button) findViewById(R.id.item_display_order_button);
            orderFromSupplier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumber = Integer.toString(supplierPhoneAreaCode) + Integer.toString(supplierPhonePrefix) +
                            Integer.toString(supplierPhoneSuffix);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                    startActivity(intent);
                }
            });
        }
        /**


        Log.v(LOG_TAG, "quantityText: " + quantityText);
//int quantity = Integer.parseInt(quantityText);
//if(quantity > 0) {
        //  quantity -= 1;
//}
        //      ContentValues values = new ContentValues();
//values.put(InventoryEntry.COLUMN_QUANTITY, quantity);
//String selection = InventoryEntry._ID + "=?";
         */
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
