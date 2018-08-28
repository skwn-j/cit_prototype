package com.example.minim.cit_prototype;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ChartDrawer {
    private BarChart barChart;
    /*
    last:10, // 지남
    memory:memory_score:2, //기억
    attention:score.attention:5, //주의
    language:3, //언어
    composition:4, //구성
    judge:2 //판단
    */
    public ChartDrawer() {
        //TODO: connect barChart to chart model in xml file.
    }

    public void drawBarChart(JSONObject info) throws JSONException {
        Iterator<String> keys = info.keys();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=1; keys.hasNext(); i++) {
            String key = keys.next();
            labels.add(key);
            entries.add(new BarEntry(i, info.getInt(key), key));
        }
        BarDataSet barDataSet = new BarDataSet(entries, "scores");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

    }

}
