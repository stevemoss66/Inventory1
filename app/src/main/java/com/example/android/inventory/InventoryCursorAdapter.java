package com.example.android.inventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.inventory.data.InventoryContract;
import com.example.android.inventory.data.InventoryContract.InventoryEntry;

/**
 * {@link InventoryCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of item data as its data source. This adapter knows
 * how to create list items for each row of item data in the {@link Cursor}.
 */
public class InventoryCursorAdapter extends CursorAdapter{
    public static final String LOG_TAG = InventoryContract.class.getSimpleName();

    private final Context mContext;

    /**
     * Constructs a new {@link InventoryCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
        mContext = context;
    }


    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the item data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current item can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView productNameTextView = (TextView) view.findViewById(R.id.product_name_textview);
        TextView priceTextView = (TextView) view.findViewById(R.id.price_textview);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity_textview);


        // Find the columns of item attributes that we're interested in
        int productNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);

        // Read the item attributes from the Cursor for the current item
        String productName = cursor.getString(productNameColumnIndex);
        double price = cursor.getDouble(priceColumnIndex);
        final int quantity = cursor.getInt(quantityColumnIndex);
        String quantityString = Integer.toString(quantity);

        //convert price to string so it can be displayed
        String priceString = Double.toString(price);
        //if there is only one digit after decimal point, add a zero
        int decimalPoint = priceString.indexOf(".");
        String cents = priceString.substring((priceString.length() - decimalPoint + 1), priceString.length());
        if(cents.length() < 2){
            priceString = priceString.concat("0");}

        // Update the TextViews with the attributes for the current item
        productNameTextView.setText(productName);
        priceTextView.setText("Price: " + priceString);
       quantityTextView.setText(quantityString);
      final  Button listItemSaleButton = (Button) view.findViewById(R.id.list_item_sale_button);
        int itemIdColumnindex = cursor.getColumnIndex(InventoryEntry._ID);
        int itemId = cursor.getInt(itemIdColumnindex);
       final Uri currentItemUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, itemId);

       listItemSaleButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //This code for get item position. I have this problem too.
                View parent = (View) listItemSaleButton.getParent();
               ListView lv = (ListView) parent.getParent();
                 int position = lv.getPositionForView(parent);
                 Log.v(LOG_TAG, "Position: " + position);
Log.v(LOG_TAG, "currentItemUri: " + currentItemUri);
               int reducedQuantity = quantity - 1;
               if(reducedQuantity < 1){
                   reducedQuantity = 0;
               }
               final int finalReducedQuantity = reducedQuantity;
               ContentValues values = new ContentValues();
               values.put(InventoryEntry.COLUMN_QUANTITY, finalReducedQuantity);
            int rowsAffected = context.getContentResolver().update(currentItemUri, values, null, null);
           }
       });

    }



}