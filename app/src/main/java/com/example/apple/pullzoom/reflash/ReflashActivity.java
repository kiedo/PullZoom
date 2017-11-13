package com.example.apple.pullzoom.reflash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.apple.R;

import java.util.ArrayList;
public class ReflashActivity extends AppCompatActivity  {

    private ListView listView;
    private MyScrllerView myScrllerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflash);
        myScrllerView =(MyScrllerView) findViewById(R.id.myScrllerView);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData()));
        listView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
                layoutParams.height = listView.getChildCount()*listView.getChildAt(0).getHeight() + 50;
                listView.setLayoutParams(layoutParams);
                return true ;
            }
        });
        myScrllerView.setHeader();


    }

    public void click(View v){
        Toast.makeText(this,"onclick",Toast.LENGTH_SHORT).show();
    }

    private ArrayList<String> getData() {
        ArrayList mArrayList = new ArrayList();
        for (int i = 0; i < 20 ; i++) {
            mArrayList.add("测试数据"+(i+1));
        }

        return mArrayList;

    }





}
