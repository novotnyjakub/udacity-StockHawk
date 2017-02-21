package com.udacity.stockhawk.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

@SuppressLint("NewApi")
public class WidgetRemoteViewsFactory implements RemoteViewsFactory {
    Context mContext = null;
    private Cursor mCursor;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        if (mCursor.moveToPosition(position))
            return mCursor.getLong(Contract.Quote.POSITION_ID);
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        String symbol = mCursor.getString(Contract.Quote.POSITION_SYMBOL);

        RemoteViews mView = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        mView.setTextViewText(R.id.symbol, symbol + " REST");
        mView.setTextColor(R.id.symbol, Color.BLACK);
        mView.setTextViewText(R.id.price, "$100.09");
        mView.setTextViewText(R.id.change, "+4%");

        final Intent fillInIntent = new Intent();
        fillInIntent.putExtra(Intent.EXTRA_SUBJECT, symbol);
        mView.setOnClickFillInIntent(R.id.symbol, fillInIntent);

        return mView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        mCursor = mContext.getContentResolver().query(Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                null, null, Contract.Quote.COLUMN_SYMBOL);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
    }

}
