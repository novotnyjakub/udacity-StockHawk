package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class DetailActivity extends AppCompatActivity {
    private String mSymbol;

    @BindView(R.id.symbol) TextView mTextSymbol;
    @BindView(R.id.price) TextView mTextPrice;
    @BindView(R.id.change) TextView mTextChange;
    @BindView(R.id.chart) LineChart mChart;

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
        Timber.d("Launching detail for %s", mSymbol);


        Cursor cursor = getContentResolver().query(Contract.Quote.makeUriForStock(mSymbol), null, null, null, null);
        if (cursor.moveToFirst()) {
            StockDisplayUtility utility = new StockDisplayUtility(this);
            utility.formatSymbolPriceChange(cursor, mTextSymbol, mTextPrice, mTextChange);
        }

        ArrayList<Entry> entries = new ArrayList<Entry>();
        for(int i = 1; i <= 10; ++i) {
            entries.add(new Entry(i, i * 3));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(R.color.accent);

        LineData data = new LineData(dataSet);
        mChart.setData(data);
        mChart.invalidate();
    }
}
