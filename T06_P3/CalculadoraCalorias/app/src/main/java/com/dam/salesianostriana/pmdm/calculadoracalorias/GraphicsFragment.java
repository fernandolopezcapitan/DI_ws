package com.dam.salesianostriana.pmdm.calculadoracalorias;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.SineEase;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphicsFragment extends Fragment {


    private final static String[] mLabels= {"W", "I", "L", "L", "I", "A", "M"};
    private final static float[] mValues = {2.5f, 3.7f, 4f, 8f, 4.5f, 4f, 5f};


    public GraphicsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_blank, container, false);

        BarChartView chart = (BarChartView) v.findViewById(R.id.stackBar);

        BarSet dataset = new BarSet(mLabels, mValues);
        dataset.setColor(Color.GREEN);
        float start= (float) 10;
        chart.setBarSpacing(start);
        chart.addData(dataset);


        chart.setLabelsFormat(new DecimalFormat('#' + " k/cal"));


        Animation anim = new Animation(500);
        anim.setEasing(new SineEase());
        anim.setAlpha(3);

        float start1= (float) 1.0;
        anim.setStartPoint(start1, 0);
        chart.show(anim);

        return v;
    }

}
