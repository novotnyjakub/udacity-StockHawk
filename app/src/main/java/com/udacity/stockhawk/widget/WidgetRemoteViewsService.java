package com.udacity.stockhawk.widget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.RemoteViewsService;

@SuppressLint("NewApi")
public class WidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        WidgetRemoteViewsFactory dataProvider = new WidgetRemoteViewsFactory(
                getApplicationContext(), intent);
        return dataProvider;
    }

}
