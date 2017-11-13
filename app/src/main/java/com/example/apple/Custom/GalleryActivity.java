package com.example.apple.Custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.R;
public class GalleryActivity extends AppCompatActivity {

    GalleryHorizontalScrollView gallery;

    int[] ids = {R.mipmap.a, R.mipmap.bg1, R.mipmap.bg2, R.mipmap.bg3, R.mipmap.ic_launcher, R.mipmap.a, R.mipmap.bg1, R.mipmap.bg2, R.mipmap.bg3, R.mipmap.ic_launcher};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        gallery = (GalleryHorizontalScrollView) findViewById(R.id.gallery);

        gallery.setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                return ids.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHodler mViewHodler = null;
                if (convertView == null) {
                    mViewHodler = new ViewHodler();
                    convertView = View.inflate(getApplicationContext(), R.layout.gallery_item, null);
                    mViewHodler.imageView = (ImageView) convertView.findViewById(R.id.bg);
                    mViewHodler.textView = (TextView) convertView.findViewById(R.id.text);
                    convertView.setTag(mViewHodler);
                } else {
                    mViewHodler = (ViewHodler) convertView.getTag();
                }
                mViewHodler.imageView.setBackgroundResource(ids[position]);
                mViewHodler.textView.setText("position = " + position);
                // TODO: 17/9/25

                return convertView;
            }
        });

    }

    class ViewHodler {

        public ImageView imageView;
        public TextView textView;
    }
}
