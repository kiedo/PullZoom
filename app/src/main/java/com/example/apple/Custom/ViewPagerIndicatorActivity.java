package com.example.apple.Custom;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apple.pullzoom.R;

/**
 * ViewPagerIndicator指示器
 */
public class ViewPagerIndicatorActivity extends AppCompatActivity {

    String[] titles = {"我爱北京", "北京", "十九大", "国贸", "中关村", "百度", "鸟窝", "天安门", "毛爹爹"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_indicator);


        ViewPagerIndicator indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                TextView textView = new TextView(getApplication());
                textView.setText(titles[position] + " "+position);
                textView.setTextColor(Color.RED);
                textView.setBackgroundColor(Color.GRAY);
                container.addView(textView);
                textView.setGravity(Gravity.CENTER);
                return textView;
            }
        });

        indicator.setViewPager(mViewPager);

    }
}
