package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    private String mSymbol;

    @BindView(R.id.symbol) TextView mTextSymbol;
    @BindView(R.id.price) TextView mTextPrice;
    @BindView(R.id.change) TextView mTextChange;
    @BindView(R.id.chart) LineChart mChart;
    private StockDisplayUtility mDisplayUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSymbol = getIntent().getStringExtra(Intent.EXTRA_SUBJECT);

        mDisplayUtility = new StockDisplayUtility(this);

        Cursor cursor = getContentResolver().query(Contract.Quote.makeUriForStock(mSymbol), null, null, null, null);
        if (cursor.moveToFirst()) {
            mDisplayUtility.formatSymbolPriceChange(cursor, mTextSymbol, mTextPrice, mTextChange);
        }

        String historyString = cursor.getString(Contract.Quote.POSITION_HISTORY);

        StockGraph stockGraph = new StockGraph(this, mChart, mDisplayUtility);
        stockGraph.processHistory(historyString);
    }
}
