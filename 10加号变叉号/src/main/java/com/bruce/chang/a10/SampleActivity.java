package com.bruce.chang.a10;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;


public class SampleActivity extends ActionBarActivity {

    private CrossView mCrossView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        mCrossView = (CrossView) findViewById(R.id.sample_cross_view);
        mCrossView.setOnClickListener(mCrossViewClickListener);
        mCrossView.setColor(getResources().getColor(R.color.cross_view_stroke_color));
    }


    private final View.OnClickListener mCrossViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCrossView.toggle();
        }
    };
}
