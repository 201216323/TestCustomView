package com.bruce.chang.a09qq;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    private BezierView mBezierView, mBezierView1, mBezierView2, mBezierView3, mBezierView4, mBezierView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBezierView = (BezierView) findViewById(R.id.bezier);
        mBezierView1 = (BezierView) findViewById(R.id.bezier1);
        mBezierView2 = (BezierView) findViewById(R.id.bezier2);
        mBezierView3 = (BezierView) findViewById(R.id.bezier3);
        mBezierView4 = (BezierView) findViewById(R.id.bezier4);
        mBezierView5 = (BezierView) findViewById(R.id.bezier5);
        mBezierView.setText("14");          //设置未读信息数
        //mBezierView.setNewText();     //设置有新的信息状态
        mBezierView1.setText("14");
        mBezierView2.setText("14");
        mBezierView3.setText("14");
        mBezierView4.setText("14");
        mBezierView5.setText("14");

    }


}
