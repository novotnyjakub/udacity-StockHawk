package com.udacity.stockhawk.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.udacity.stockhawk.R;

public class ErrorReporter {
    private Context mContext;

    public ErrorReporter(Context context) {
        mContext = context;
    }

    public void setError(String symbol) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String syncErrorKey = mContext.getString(R.string.pref_sync_error_ley);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(syncErrorKey, symbol);
        editor.commit();
    }

    public boolean hasError() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String syncErrorKey = mContext.getString(R.string.pref_sync_error_ley);

        return prefs.getString(syncErrorKey, "").length() > 0;
    }

    public String getError() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String syncErrorKey = mContext.getString(R.string.pref_sync_error_ley);

        return prefs.getString(syncErrorKey, "");
    }

    public void clearError() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String syncErrorKey = mContext.getString(R.string.pref_sync_error_ley);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(syncErrorKey);
        editor.commit();
    }
}
