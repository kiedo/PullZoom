package com.example.apple.pullzoom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class ListviewMActiviy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_mactiviy);
        ListViewPullZoom listview = (ListViewPullZoom) findViewById(R.id.listview);
        View zoomview = LayoutInflater.from(this).inflate(R.layout.zoom_view, null);


        listview.setZoomView(zoomview);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (1.0F * (mScreenHeight / 4.0F)));
        listview.setHeaderLayoutParams(localObject);

        String[] adapterData = new String[]{
                "A", "B", "C", "D", "E", "F", "G", "A",
                "B", "C", "D", "E", "F", "G", "A", "C",
                "D", "E", "F", "G", "A", "B", "C", "E",
                "F", "G", "A", "A"
                , "A", "B", "C", "D", "E", "F", "G", "A",
                "B", "C", "D", "E", "F", "G", "A", "C",
                "D", "E", "F", "G", "A", "B", "C", "E",
                "F", "G", "A", "A" ,"A", "B", "C", "D", "E", "F", "G", "A",
                "B", "C", "D", "E", "F", "G", "A", "C",
                "D", "E", "F", "G", "A", "B", "C", "E",
                "F", "G", "A", "A"

        };

        listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adapterData));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "position = " + position, 1).show();
            }
        });

    }
}
