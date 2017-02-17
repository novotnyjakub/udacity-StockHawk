package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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

        String historyString = cursor.getString(Contract.Quote.POSITION_HISTORY);
        String[] historyArray = historyString.split("\n");
        List<String> historyList = Arrays.asList(historyArray);

//        long currentTime = System.currentTimeMillis();
//        DateFormat format = android.text.format.DateFormat.getDateFormat(this);
//        format.format(currentTime);

        ArrayList<Entry> entries = new ArrayList<>();
        int i = 1;
        for(String history: historyList) {
            String[] historyEntry = history.split(", ");

            entries.add(new Entry(i, Float.valueOf(historyEntry[1])));
            ++i;
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setDrawCircles(false);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setLineWidth(1.8f);
        dataSet.setColor(ContextCompat.getColor(this, R.color.primary_light));
        dataSet.setFillColor(ContextCompat.getColor(this, R.color.primary_light));
        dataSet.setDrawFilled(true);
        dataSet.setHighLightColor(ContextCompat.getColor(this, R.color.accent));

        LineData data = new LineData(dataSet);
        mChart.setData(data);

        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);

        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        mChart.getAxisRight().setEnabled(false);
        mChart.getXAxis().setEnabled(false);
        mChart.setDrawGridBackground(false);


        mChart.invalidate();
    }
}
