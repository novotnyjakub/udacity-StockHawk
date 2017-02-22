package com.udacity.stockhawk.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.udacity.stockhawk.R;

public class ErrorReporter {
    private SharedPreferences mPrefs;
    private String mSyncErrorKey;

    public ErrorReporter(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mSyncErrorKey = context.getString(R.string.pref_sync_error_ley);
    }

    public void setError(String symbol) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(mSyncErrorKey, symbol);
        editor.commit();
    }

    public boolean hasError() {
        return mPrefs.getString(mSyncErrorKey, "").length() > 0;
    }

    public String getError() {
        return mPrefs.getString(mSyncErrorKey, "");
    }

    public void clearError() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(mSyncErrorKey);
        editor.commit();
    }
}
