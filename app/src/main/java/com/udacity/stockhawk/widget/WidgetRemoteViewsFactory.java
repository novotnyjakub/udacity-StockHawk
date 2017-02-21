package com.udacity.stockhawk.widget;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.udacity.stockhawk.ui.DetailActivity;

@SuppressLint("NewApi")
public class WidgetRemoteViewsFactory implements RemoteViewsFactory {

    List mCollections = new ArrayList();

    Context mContext = null;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mCollections.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews mView = new RemoteViews(mContext.getPackageName(),
                android.R.layout.simple_list_item_1);
        mView.setTextViewText(android.R.id.text1, (CharSequence) mCollections.get(position));
        mView.setTextColor(android.R.id.text1, Color.BLACK);

        final Intent fillInIntent = new Intent();
        fillInIntent.putExtra(Intent.EXTRA_SUBJECT, (String)mCollections.get(position));
        mView.setOnClickFillInIntent(android.R.id.text1, fillInIntent);

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
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    private void initData() {
        mCollections.clear();
        mCollections.add("AAPL");
        mCollections.add("FB");
        mCollections.add("GOOG");
        mCollections.add("YHOO");
    }

    @Override
    public void onDestroy() {

    }

}
