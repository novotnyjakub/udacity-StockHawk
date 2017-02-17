package com.udacity.stockhawk.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        Timber.d("Launching detail for %s", mSymbol);

        mDisplayUtility = new StockDisplayUtility(this);

        Cursor cursor = getContentResolver().query(Contract.Quote.makeUriForStock(mSymbol), null, null, null, null);
        if (cursor.moveToFirst()) {
            mDisplayUtility.formatSymbolPriceChange(cursor, mTextSymbol, mTextPrice, mTextChange);
        }

        String historyString = cursor.getString(Contract.Quote.POSITION_HISTORY);
        String[] historyArray = historyString.split("\n");
        List<String> historyList = Arrays.asList(historyArray);
        Collections.reverse(historyList);

        ArrayList<Entry> entries = new ArrayList<>();
        int i = 1;
        for(String history: historyList) {
            String[] historyEntry = history.split(", ");

            entries.add(new Entry(Float.valueOf(historyEntry[0]), Float.valueOf(historyEntry[1])));
            ++i;
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setDrawCircles(false);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setLineWidth(2);
        dataSet.setColor(ContextCompat.getColor(this, R.color.primary_light));
        dataSet.setFillColor(ContextCompat.getColor(this, R.color.primary_light));
        dataSet.setDrawFilled(true);
        dataSet.setHighLightColor(ContextCompat.getColor(this, R.color.primary));

        LineData data = new LineData(dataSet);
        mChart.setData(data);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);

        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        mChart.getAxisRight().setEnabled(false);
        mChart.getXAxis().setEnabled(false);
        mChart.setDrawGridBackground(false);

        mChart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mDisplayUtility.formatPrice(value);
            }
        });

        MarkerView markerView = new StockMarkerView(this, mDisplayUtility);
        markerView.setChartView(mChart);
        mChart.setMarker(markerView);

        mChart.invalidate();
    }
}
