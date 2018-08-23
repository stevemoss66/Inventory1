package com.example.android.inventory;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.inventory.data.InventoryContract.InventoryEntry;
import com.example.android.inventory.data.InventoryDBHelper;
import com.example.android.inventory.data.InventoryProvider;
import com.example.android.inventory.data.InventoryContract;

import static com.example.android.inventory.data.InventoryContract.InventoryEntry.COLUMN_QUANTITY;


/**
 * Displays list of items that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = InventoryDBHelper.class.getSimpleName();

    /** Identifier for the pet data loader */
    private static final int ITEM_LOADER = 0;

    /** Adapter for the ListView */
    InventoryCursorAdapter mCursorAdapter;

    /** Content URI for the existing item (null if it's a new item) */
    private Uri mCurrentItemUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        Log.v(LOG_TAG, "The CatalogActivity onCreate method has been accessed.");


        // Find the ListView which will be populated with the item data
        ListView itemListView = (ListView) findViewById(R.id.list_view_item);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        itemListView.setEmptyView(emptyView);


        // Setup an Adapter to create a list item for each row of item data in the Cursor.
        // There is no item data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new InventoryCursorAdapter(this, null);
        itemListView.setAdapter(mCursorAdapter);

        Button listItemSaleButton = (Button) findViewById(R.id.list_item_sale_button);
        //This code for get item position. I have this problem too.
      //  View parent = (View) listItemSaleButton.getParent();
     //   ListView lv = (ListView) parent.getParent();
    //    int position = lv.getPositionForView(parent);
     //   Log.v(LOG_TAG, "Position: " + position);




        // Setup the item click listener

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link ItemDisplayActivity}
                Intent intent = new Intent(CatalogActivity.this, ItemDisplayActivity.class);

                // Form the content URI that represents the specific item that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link InventoryEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.inventory/items/2"
                // if the item with ID 2 was clicked on.
               Uri currentItemUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentItemUri);

                // Launch the {@link ItemDisplayActivity} to display the data for the current item.
                startActivity(intent);
            }

        });



        // Kick off the loader
        getLoaderManager().initLoader(ITEM_LOADER, null, this);
        Log.v(LOG_TAG, "The getLoaderManager.initLoader has just been called.");



/**
 *  public void getView(final int position, View convertView, ViewGroup parent) {
 LayoutInflater buttonInListView = getLayoutInflater();
 View button = buttonInListView.inflate(R.layout.list_item, parent, false);
 // click listener to open Detailactivity
 Button detailButton = (Button) button.findViewById( R.id.detail_button );
 detailButton.setOnClickListener( new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent = new Intent( CatalogActivity.this, DetailActivity.class );
startActivity( intent );
}
} );
 }
 *
 */

//TextView quantityTextView = (TextView) findViewById(R.id.quantity_textview);
//String quantityText = quantityTextView.getText().toString();
//int quantity = Integer.parseInt(quantityText);
//if(quantity > 0) {
  //  quantity -= 1;
//}
  //      ContentValues values = new ContentValues();
//values.put(InventoryEntry.COLUMN_QUANTITY, quantity);
//String selection = InventoryEntry._ID + "=?";



   }

    /**
     * Helper method to insert hardcoded item data into the database. For debugging purposes only.
     */
    private void insertItem() {
        // Create a ContentValues object where column names are the keys,
        // and pedal felt attributes are the values.
        Log.v(LOG_TAG, "The CatalogActivity insertItem method has been called.");
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, "Pedal felt, set of seven");
        values.put(InventoryEntry.COLUMN_PRICE, 12.0);
        values.put(COLUMN_QUANTITY, 26);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, "Lyon & Healy West");
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE_AREA_CODE, "877");
                values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE_PREFIX, "621");
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE_SUFFIX, "3881");
Log.v(LOG_TAG,"ContentValues values has been populated.");
        // Insert a new row for pedal felts into the provider using the ContentResolver.
        // Use the {@link InventoryEntry#CONTENT_URI} to indicate that we want to insert
        // into the items database table.
        // Receive the new content URI that will allow us to access pedal felt data in the future.
        Log.v(LOG_TAG, "CONTENT_URI IS " + InventoryEntry.CONTENT_URI);
        Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
        Log.v(LOG_TAG, "newUri is " + newUri);
    }

    /**
     * Helper method to delete all items in the database.
     */
    private void deleteAllItems() {
        int rowsDeleted = getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from item database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertItem();
                return true;
                //Respond to a click on the "Add an Item" menu option
            case R.id.action_add_item:
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllItems();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "The CatalogActivity onCreateLoader method has been accessed.");
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRICE,
        COLUMN_QUANTITY };

    // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                InventoryEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order



    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link PetCursorAdapter} with this new cursor containing updated item data
        mCursorAdapter.swapCursor(data);
        /**
        // Get the item Uri
        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();
        if(data.moveToFirst()){
            int quantityColumnIndex = data.getColumnIndex(COLUMN_QUANTITY);
            int quantity = data.getInt(quantityColumnIndex);
            int reducedQuantity = quantity - 1;
            if(reducedQuantity < 1){
                reducedQuantity = 0;
            }
            final int finalReducedQuantity = reducedQuantity;
            Button sellFromListItem = (Button) findViewById(R.id.list_item_sale_button);
            sellFromListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_QUANTITY, finalReducedQuantity);
                    int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
                }
            });

        }

*/
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}