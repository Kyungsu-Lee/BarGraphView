package lmasi.com.baranimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BarGraphView barGraphView = findViewById(R.id.bargraph);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barGraphView.setLevelAnimation(0);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barGraphView.setLevelAnimation(1);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barGraphView.setLevelAnimation(2);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barGraphView.setLevelAnimation(3);
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barGraphView.setLevelAnimation(4);
            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barGraphView.setLevelAnimation(5);
            }
        });

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barGraphView.setLevelAnimation(6);
            }
        });

        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barGraphView.setLevelAnimation(7);
            }
        });

        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barGraphView.setLevelAnimation(8);
            }
        });

        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barGraphView.setLevelAnimation(9);
            }
        });

        findViewById(R.id.button11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barGraphView.setLevelAnimation(10);
            }
        });

        findViewById(R.id.button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barGraphView.setLevelAnimation(12);
            }
        });
    }
}
