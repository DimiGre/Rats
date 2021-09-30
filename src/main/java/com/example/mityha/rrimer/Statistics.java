package com.example.mityha.rrimer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import im.dacer.androidcharts.LineView;


public class Statistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        LineView lineView = (LineView)findViewById(R.id.line_view);
        lineView.setDrawDotLine(false); //optional
        lineView.setColorArray(new int[]{Color.BLACK,Color.GREEN,Color.RED});
        Intent intent = getIntent();
        ArrayList<ArrayList<Integer>> arrayList = (ArrayList<ArrayList<Integer>>)intent.getSerializableExtra("a");
        ArrayList<String> a = new ArrayList<>();
        int max = 0;
        for (int i = 0; i < arrayList.size(); i++) if(arrayList.get(i).size() > max) max = arrayList.get(i).size();
        for(int i = 1; i <= max + 1; i++) a.add(i + "");
        lineView.setBottomTextList(a);
        lineView.setColorArray(new int[]{Color.BLUE});
        lineView.setDataList(arrayList);
    }
}
