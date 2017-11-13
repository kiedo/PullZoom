package com.example.apple.Custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.apple.R;
/**
 * 卫星导航
 */
public class PlanetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet);
        ((PlanetViewGroup) findViewById(R.id.planet_bottem)).setMenuOnClickListener(new PlanetViewGroup.MenuOnClickListener() {
            @Override
            public void menuItem(int pos) {
                Toast.makeText(getApplication(), "click" + pos, Toast.LENGTH_SHORT).show();
            }
        });
        ((PlanetViewGroup) findViewById(R.id.planet_top)).setMenuOnClickListener(new PlanetViewGroup.MenuOnClickListener() {
            @Override
            public void menuItem(int pos) {
                Toast.makeText(getApplication(), "click" + pos, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
