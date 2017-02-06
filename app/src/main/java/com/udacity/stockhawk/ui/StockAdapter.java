package com.udacity.stockhawk.ui;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private final Context context;
    private final DecimalFormat dollarFormatWithPlus;
    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormatWithPlus;
    private final DecimalFormat percentageFormat;
    private Cursor cursor;
    private final StockAdapterOnClickHandler clickHandler;

    StockAdapter(Context context, StockAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;

        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormatWithPlus = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormatWithPlus.setMaximumFractionDigits(2);
        percentageFormatWithPlus.setMinimumFractionDigits(2);
        percentageFormatWithPlus.setPositivePrefix("+");
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
    }

    void setCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    String getSymbolAtPosition(int position) {

        cursor.moveToPosition(position);
        return cursor.getString(Contract.Quote.POSITION_SYMBOL);
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(context).inflate(R.layout.list_item_quote, parent, false);

        return new StockViewHolder(item);
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {

        cursor.moveToPosition(position);


        String symbolText = cursor.getString(Contract.Quote.POSITION_SYMBOL);
        String symbolCD = context.getString(R.string.stock_item_symbol_content_description, symbolText);
        holder.symbol.setText(symbolText);
        holder.symbol.setContentDescription(symbolCD);

        String priceText = dollarFormat.format(cursor.getFloat(Contract.Quote.POSITION_PRICE));
        String priceCD = context.getString(R.string.stock_item_price_content_description, priceText);
        holder.price.setText(priceText);
        holder.price.setContentDescription(priceCD);


        int changeCDString;
        float rawAbsoluteChange = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
        float percentageChange = cursor.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);

        if (rawAbsoluteChange > 0) {
            changeCDString = R.string.stock_item_change_up_content_description;
            holder.change.setBackgroundResource(R.drawable.percent_change_pill_green);
        } else {
            changeCDString = R.string.stock_item_change_down_content_description;
            holder.change.setBackgroundResource(R.drawable.percent_change_pill_red);
        }

        String changeCD, changeText;

        if (PrefUtils.getDisplayMode(context)
                .equals(context.getString(R.string.pref_display_mode_absolute_key))) {
            changeCD = context.getString(changeCDString, dollarFormat.format(Math.abs(rawAbsoluteChange)));
            changeText = dollarFormatWithPlus.format(rawAbsoluteChange);
        } else {
            changeCD = context.getString(changeCDString, percentageFormat.format(Math.abs(percentageChange / 100)));
            changeText = percentageFormatWithPlus.format(percentageChange / 100);
        }

        holder.change.setText(changeText);
        holder.change.setContentDescription(changeCD);

    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
        }
        return count;
    }


    interface StockAdapterOnClickHandler {
        void onClick(String symbol);
    }

    class StockViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.symbol)
        TextView symbol;

        @BindView(R.id.price)
        TextView price;

        @BindView(R.id.change)
        TextView change;

        StockViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            cursor.moveToPosition(adapterPosition);
            int symbolColumn = cursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL);
            clickHandler.onClick(cursor.getString(symbolColumn));

        }


    }
}
