package com.young.android.bleproject;

/**
 * Created by young on 2016-06-11.
 */


import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

public class MeasureChart extends AbstractDemoChart
{
    public String getName()
    {
        return "Population ";
    }
    public String getDesc()
    {
        return "The Population growth across several years (time chart)";
    }



    public GraphicalView executesView(Context context)
    {
        String[] titles = new String[] { "Pair 1 - UV", "Pair 1 - VIS", "Pair1 - IR", "Pair1 - TH1", "Pair1 - TH2"};

        List<double[]> times = new ArrayList<double[]>();
        List<double[]> values = new ArrayList<double[]>();

        double[] timevalues = MeasureService.timevaluearray;
        times.add(timevalues); // UV
        times.add(timevalues); // VIS
        times.add(timevalues); // IR
        times.add(timevalues); // TH1
        times.add(timevalues); // TH2

        if (MeasureService.UVvaluearray[0].length != MeasureService.timevaluearray.length)
            Toast.makeText(context, "different length", Toast.LENGTH_SHORT).show();
        values.add(MeasureService.UVvaluearray[0]);
        values.add(MeasureService.VISvaluearray[0]);
        values.add(MeasureService.IRvaluearray[0]);
        values.add(MeasureService.THvaluearray1[0]);
        values.add(MeasureService.THvaluearray2[0]);
        int[] colors = new int[] { Color.BLUE, Color.RED, Color.BLACK, Color.GRAY, Color.GREEN};
        PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND, PointStyle.CIRCLE, PointStyle.SQUARE, PointStyle.TRIANGLE, PointStyle.SQUARE};
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        renderer.setDisplayChartValues(true);
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.WHITE);
        renderer.setMarginsColor(Color.WHITE);
        setChartSettings(renderer, "Graph measured", "Time(ms)", "Values", timevalues[0], timevalues[timevalues.length-1], 25, 30, Color.GREEN, Color.RED);




        renderer.setYLabels(10);

        return ChartFactory.getLineChartView(context, buildDataset(titles, times, values), renderer);

    }

}
