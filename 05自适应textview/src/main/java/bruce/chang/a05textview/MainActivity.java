package bruce.chang.a05textview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bruce.chang.a05textview.widget.AutofitTextView;

public class MainActivity extends AppCompatActivity {
    CustomTextView one,two,three,four,five;
    AutofitTextView onea,twoa,threea,foura,fivea,sixa,sevena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        one = (CustomTextView) findViewById(R.id.one);
        two = (CustomTextView) findViewById(R.id.two);
        three = (CustomTextView) findViewById(R.id.three);
        four = (CustomTextView) findViewById(R.id.four);
        five = (CustomTextView) findViewById(R.id.five);
        one.setText("力量训练");
        two.setText("力量训练力量训练力量训练");
        three.setText("力量训练力量训练力量训练力量训练");
        four.setText("力量训练力量训练力量训练力量训练力量训练");
        five.setText("力量训练力量训练力量训练力量训练力量训练力量训练力量训练");
        onea = (AutofitTextView) findViewById(R.id.onea);
        twoa = (AutofitTextView) findViewById(R.id.twoa);
        threea = (AutofitTextView) findViewById(R.id.threea);
        foura = (AutofitTextView) findViewById(R.id.foura);
        fivea = (AutofitTextView) findViewById(R.id.fivea);
        sixa = (AutofitTextView) findViewById(R.id.sixa);
        sevena = (AutofitTextView) findViewById(R.id.sevena);
        onea.setText("力量训练");
        twoa.setText("力量训练力量训练力量训练");
        threea.setText("力量训练力量训练力量训练力量训练");
        foura.setText("力量训练力量训练力量训练力量训练力量训练");
        fivea.setText("力量训练力量训练力量训练力量训练力量训练力量训练力量训练");
        sixa.setText("力量训练力量训练力量训练力量训练力量训练力量训练力量训练力量训练");
        sevena.setText("力量训练力量训练力量训练力量训练力量训练力量训练力量训练力量训练力量训练");
        sevena.setText("力量训练力量训练力量训练力量训练力量训练力量训练力量训练力量训练力量训练");
    }
}
