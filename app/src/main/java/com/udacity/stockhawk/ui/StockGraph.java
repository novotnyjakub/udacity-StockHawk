package com.udacity.stockhawk.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StockGraph {
    private Context mContext;
    private LineChart mChart;
    private StockDisplayUtility mDisplayUtility;

    public StockGraph(Context context, LineChart chart, StockDisplayUtility displayUtility) {
        mContext = context;
        mChart = chart;
        mDisplayUtility = displayUtility;
    }

    public void processHistory(String historyString) {
        ArrayList<Entry> entries = getEntries(historyString);

        LineDataSet dataSet = getLineDataSet(entries);

        LineData data = new LineData(dataSet);
        mChart.setData(data);

        setupChart();
        setupLeftAxis();
        setupMarkerView();

        mChart.invalidate();
    }

    private void setupMarkerView() {
        MarkerView markerView = new StockMarkerView(mContext, mDisplayUtility);
        markerView.setChartView(mChart);
        mChart.setMarker(markerView);
    }

    private void setupLeftAxis() {
        mChart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mDisplayUtility.formatPrice(value);
            }
        });
    }

    private void setupChart() {
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);

        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        mChart.getAxisRight().setEnabled(false);
        mChart.getXAxis().setEnabled(false);
        mChart.setDrawGridBackground(false);
    }

    @NonNull
    private LineDataSet getLineDataSet(ArrayList<Entry> entries) {
        LineDataSet dataSet = new LineDataSet(entries, mContext.getString(R.string.stock_timeline));
        dataSet.setDrawCircles(false);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setLineWidth(2);
        dataSet.setColor(ContextCompat.getColor(mContext, R.color.primary_light));
        dataSet.setFillColor(ContextCompat.getColor(mContext, R.color.primary_light));
        dataSet.setDrawFilled(true);
        dataSet.setHighLightColor(ContextCompat.getColor(mContext, R.color.primary));
        return dataSet;
    }

    @NonNull
    private ArrayList<Entry> getEntries(String historyString) {
        String[] historyArray = historyString.split("\n");
        List<String> historyList = Arrays.asList(historyArray);
        Collections.reverse(historyList);

        ArrayList<Entry> entries = new ArrayList<>();
        for (String history : historyList) {
            String[] historyEntry = history.split(", ");

            entries.add(new Entry(Float.valueOf(historyEntry[0]), Float.valueOf(historyEntry[1])));
        }
        return entries;
    }
}
