package com.example.apple.Custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.example.apple.R;

/**
 * @author apple
 */
public class LuckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luck);

        final LuckyView mlucky = (LuckyView) findViewById(R.id.lucky);


        final AppCompatEditText editText = (AppCompatEditText) findViewById(R.id.end_text);
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                try {
                    mlucky.start(Integer.parseInt(text));
                } catch (Exception e) {
                }

            }
        });

    }
}
