package com.udacity.stockhawk.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class StockDisplayUtility {
    private Context mContext;

    private final DecimalFormat mDollarFormat;
    private final DecimalFormat mDollarFormatWithPlus;
    private final DecimalFormat mPercentageFormatWithPlus;
    private final DecimalFormat mPercentageFormat;
    private DateFormat mDateFormat;

    public StockDisplayUtility(Context context) {
        mContext = context;

        mDollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        mDollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        mDollarFormatWithPlus.setPositivePrefix("+$");
        mPercentageFormatWithPlus = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        mPercentageFormatWithPlus.setMaximumFractionDigits(2);
        mPercentageFormatWithPlus.setMinimumFractionDigits(2);
        mPercentageFormatWithPlus.setPositivePrefix("+");
        mPercentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        mPercentageFormat.setMaximumFractionDigits(2);
        mPercentageFormat.setMinimumFractionDigits(2);
        mDateFormat = android.text.format.DateFormat.getDateFormat(mContext);
    }

    public void formatSymbolPriceChange(Cursor cursor, TextView symbol, TextView price, TextView change) {
        Field symbolField = prepareSymbol(cursor);
        symbol.setText(symbolField.text);
        symbol.setContentDescription(symbolField.contentDescription);

        Field priceField = preparePrice(cursor);
        price.setText(priceField.text);
        price.setContentDescription(priceField.contentDescription);

        Field changeField = prepareChange(cursor);
        change.setText(changeField.text);
        change.setContentDescription(changeField.contentDescription);
        change.setTextColor(changeField.color);
    }

    public void formatSymbolPriceChange(Cursor cursor, RemoteViews views, int symbolId, int priceId, int changeId) {
        Field symbolField = prepareSymbol(cursor);
        views.setTextViewText(symbolId, symbolField.text);
        views.setContentDescription(symbolId, symbolField.contentDescription);

        Field priceField = preparePrice(cursor);
        views.setTextViewText(priceId, priceField.text);
        views.setContentDescription(priceId, priceField.contentDescription);

        Field changeField = prepareChange(cursor);
        views.setTextViewText(changeId, changeField.text);
        views.setContentDescription(changeId, changeField.contentDescription);
        views.setTextColor(changeId, changeField.color);
    }

    public String formatPrice(float price) {
        return mDollarFormat.format(price);
    }

    public String formatDate(float dateMillis) {
        return mDateFormat.format((long)dateMillis);
    }

    private Field prepareSymbol(Cursor cursor) {
        String symbolText = cursor.getString(Contract.Quote.POSITION_SYMBOL);
        String symbolCD = mContext.getString(R.string.stock_item_symbol_content_description, symbolText);

        return new Field(symbolText, symbolCD);
    }

    private Field preparePrice(Cursor cursor) {
        String priceText = formatPrice(cursor.getFloat(Contract.Quote.POSITION_PRICE));
        String priceCD = mContext.getString(R.string.stock_item_price_content_description, priceText);

        return new Field(priceText, priceCD);
    }

    private Field prepareChange(Cursor cursor) {
        int changeCDString, changeColor;
        float rawAbsoluteChange = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
        float percentageChange = cursor.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);

        if (rawAbsoluteChange > 0) {
            changeCDString = R.string.stock_item_change_up_content_description;
            changeColor = ContextCompat.getColor(mContext, R.color.material_green_700);
        } else {
            changeCDString = R.string.stock_item_change_down_content_description;
            changeColor = ContextCompat.getColor(mContext, R.color.material_red_700);
        }

        String changeCD, changeText;

        if (PrefUtils.getDisplayMode(mContext)
                .equals(mContext.getString(R.string.pref_display_mode_absolute_key))) {
            changeCD = mContext.getString(changeCDString, mDollarFormat.format(Math.abs(rawAbsoluteChange)));
            changeText = mDollarFormatWithPlus.format(rawAbsoluteChange);
        } else {
            changeCD = mContext.getString(changeCDString, mPercentageFormat.format(Math.abs(percentageChange / 100)));
            changeText = mPercentageFormatWithPlus.format(percentageChange / 100);
        }

        return new Field(changeText, changeCD, changeColor);
    }

    private class Field {
        public String text;
        public String contentDescription;
        public int color;

        public Field(String text, String contentDescription, int color) {
            this.text = text;
            this.contentDescription = contentDescription;
            this.color = color;
        }

        public Field(String text, String contentDescription) {
            this.text = text;
            this.contentDescription = contentDescription;
        }
    }
}
