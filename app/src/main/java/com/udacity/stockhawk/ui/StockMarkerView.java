package com.udacity.stockhawk.ui;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.udacity.stockhawk.R;

public class StockMarkerView extends MarkerView {

    private TextView content;
    private StockDisplayUtility mStockDisplayUtility;

    public StockMarkerView(Context context, StockDisplayUtility stockDisplayUtility) {
        super(context, R.layout.stock_marker_view);
        mStockDisplayUtility = stockDisplayUtility;

        content = (TextView) findViewById(R.id.content);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String formatted = mStockDisplayUtility.formatDate(e.getX()) +
                "\n" +
                mStockDisplayUtility.formatPrice(e.getY());
        content.setText(formatted);

        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {
        if (mOffset == null) {
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight() - 10);
        }

        return mOffset;
    }
}

