package com.udacity.stockhawk.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
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
        String symbolText = cursor.getString(Contract.Quote.POSITION_SYMBOL);
        String symbolCD = mContext.getString(R.string.stock_item_symbol_content_description, symbolText);
        symbol.setText(symbolText);
        symbol.setContentDescription(symbolCD);

        String priceText = formatPrice(cursor.getFloat(Contract.Quote.POSITION_PRICE));
        String priceCD = mContext.getString(R.string.stock_item_price_content_description, priceText);
        price.setText(priceText);
        price.setContentDescription(priceCD);


        int changeCDString;
        float rawAbsoluteChange = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
        float percentageChange = cursor.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);

        if (rawAbsoluteChange > 0) {
            changeCDString = R.string.stock_item_change_up_content_description;
            change.setTextColor(ContextCompat.getColor(mContext, R.color.material_green_700));
        } else {
            changeCDString = R.string.stock_item_change_down_content_description;
            change.setTextColor(ContextCompat.getColor(mContext, R.color.material_red_700));
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

        change.setText(changeText);
        change.setContentDescription(changeCD);
    }

    public String formatPrice(float price) {
        return mDollarFormat.format(price);
    }

    public String formatDate(float dateMillis) {
        return mDateFormat.format((long)dateMillis);
    }
}
