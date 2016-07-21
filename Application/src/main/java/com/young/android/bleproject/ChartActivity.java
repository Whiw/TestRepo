package com.young.android.bleproject;

/**
 * Created by young on 2016-06-11.
 */
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.achartengine.GraphicalView;

public class ChartActivity extends Activity {
    public GraphicalView y;
    public GraphicalView z;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chart);

        LinearLayout x = (LinearLayout) findViewById(R.id.chart2);
        y = new MeasureChart().executesView(getApplicationContext());
        x.addView(y, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TableLayout k = (TableLayout) findViewById(R.id.table);
        TableRow[] tablerow = new TableRow[6];
        TextView[][] textviews = new TextView[6][MeasureService.timevaluearray.length+1];

       textviews[0][0] = new TextView(this);
        textviews[1][0] = new TextView(this);
        textviews[1][0].setText("UV(µW/cm²) : ");
        textviews[2][0] = new TextView(this);
        textviews[2][0].setText("VIS(mW/cm²) : ");
        textviews[3][0] = new TextView(this);
        textviews[3][0].setText("IR(µW/cm²) : ");
        textviews[4][0] = new TextView(this);
        textviews[4][0].setText("TH1(ºC) : ");
        textviews[5][0] = new TextView(this);
        textviews[5][0].setText("TH2(ºC) : ");

        for (int i=0; i<MeasureService.timevaluearray.length; i++) {
            textviews[0][i+1] = new TextView(this);
            textviews[1][i+1] = new TextView(this);
            textviews[2][i+1] = new TextView(this);
            textviews[3][i+1] = new TextView(this);
            textviews[4][i+1] = new TextView(this);
            textviews[5][i+1] = new TextView(this);
            textviews[0][i+1].setText(String.valueOf(MeasureService.timevaluearray[i]) +" ");
            textviews[1][i+1].setText(String.valueOf(MeasureService.UVvaluearray[0][i]) +" ");
            textviews[2][i+1].setText(String.valueOf(MeasureService.VISvaluearray[0][i]) +" ");
            textviews[3][i+1].setText(String.valueOf(MeasureService.IRvaluearray[0][i]) +" ");
            textviews[4][i+1].setText(String.valueOf(MeasureService.THvaluearray1[0][i]) +" ");
            textviews[5][i+1].setText(String.valueOf(MeasureService.THvaluearray2[0][i]) +" ");
        }

        for(int i=0; i < 6; i++) {
            tablerow[i] = new TableRow(this);
            for(int j =0; j < MeasureService.timevaluearray.length+1; j++) {
                tablerow[i].addView(textviews[i][j]);
            }
        }

        for (int i=0; i < 6; i++) {
            k.addView(tablerow[i]);
        }



    }
}