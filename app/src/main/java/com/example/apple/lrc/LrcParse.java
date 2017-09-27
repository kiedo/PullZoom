package com.example.apple.lrc;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 歌词解析
 * Created by apple on 17/4/28.
 */
public class LrcParse {


    static final String TAG = "LrcParse";

    static List<LrcBean> mlrcs = new ArrayList<>();

    double aDouble = 0.8;

    static LrcParse mLrcParse = new LrcParse();

    Paint mPaint;
    int tagetWidth;

    private LrcParse() {
    }

    public static LrcParse getLrcParse() {
        return mLrcParse;
    }


    public List<LrcBean> getLrcs() {
        return mlrcs;
    }

    public void readFile(Context context, Paint mPaint) {
        this.mPaint = mPaint;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        tagetWidth = (int) (metrics.widthPixels * aDouble);
        mlrcs.clear();
        BufferedReader bufferedReader = null;
        InputStream open = null;
        try {
            open = context.getAssets().open("2line");
            bufferedReader = new BufferedReader(new InputStreamReader(open));
            String lien = null;
            StringBuilder sbuid = new StringBuilder();
            while ((lien = bufferedReader.readLine()) != null) {
                sbuid.append(lien).append("\r\n");
            }
            parseLrcTitle(sbuid.toString());

            Collections.sort(mlrcs);


        } catch (Exception e) {
            Log.d(TAG, "readFile: 解析失败 " + e.getMessage());
        } finally {
            IOutil.close(open);
            IOutil.close(bufferedReader);
        }

    }


    /**
     * 歌曲名字和作者解析
     *
     * @param lrc
     */
    void parseLrcTitle(String lrc) {
        if (!TextUtils.isEmpty(lrc)) {
            String[] split = lrc.split("\r\n");
            for (int i = 0, size = split.length; i < size; i++) {
                String s = split[i];
                if (!TextUtils.isEmpty(s)) {
//                    Pattern pattern = Pattern.compile("\\[ti:(.+?)\\]");
//                    Matcher matcher = pattern.matcher(s);
//                    if (matcher.find()) {
//                        String group = matcher.group(1);
//                        LrcBean mtitle = new LrcBean();
//                        mtitle.setSongName(group);
//                        mlrcs.add(mtitle);
//                        Log.d(TAG, group + " \r\n");
//                        continue;
//
//                    }
//                    pattern = Pattern.compile("\\[ar:(.+?)\\]");
//                    matcher = pattern.matcher(s);
//                    if (matcher.find()) {
//                        String group = matcher.group(1);
//                        LrcBean mtitle = new LrcBean();
//                        mtitle.setAuthor(group);
//                        mlrcs.add(mtitle);
//                        Log.d(TAG, group + " \r\n");
//                        continue;
//                    }
                    parseLine(s);

                } else {
                    continue;
                }
            }
        }
    }

    /**
     * 每一句歌词解析
     *
     * @param s
     */
    private void parseLine(String s) {

        if (s.contains("]")) {
            String[] split = s.split("]");//时间和歌词，可能有多个时间 [02:06.22 [04:00.12  也不需要魅力的长发
            List<String> tempTimes = new ArrayList<>();//临时保存时间
            String templrc = null;
            for (int i = 0, size = split.length; i < size; i++) {
                String timelrc = split[i];
                if (timelrc.contains("[")) {
                    tempTimes.add(timelrc.replace("[", "").trim());
                } else {
                    templrc = timelrc;
                }
            }
            if (tempTimes.size() > 0 && !TextUtils.isEmpty(templrc)) {// 必须保证时间和歌词同时存在才有意义显示歌词
                for (int i = 0, size = tempTimes.size(); i < size; i++) {
                    String time = tempTimes.get(i);
                    checkLrcLength(time, templrc);

                }
            }

        }
    }

    /**
     * 检查，每句的歌词长度，如果超出屏幕宽就折行显示
     * <p>
     * ＊
     *
     * @param time
     * @param templrc
     */
    private void checkLrcLength(String time, String templrc) {

        float measureText = mPaint.measureText(templrc);
        int count = (int) (measureText / tagetWidth) + 1;
        if (count >= 2) {//多行显示
            int lenth = templrc.length() / count;
            for (int i = 1; i <= count; i++) {
                LrcBean mLrcBean = new LrcBean();
                mLrcBean.setLrc(templrc.substring(lenth * (i - 1), lenth * i));
                mLrcBean.setBeginTime(formatTime(time));
                mlrcs.add(mLrcBean);
            }
        } else {

            LrcBean mLrcBean = new LrcBean();
            mLrcBean.setLrc(templrc);
            mLrcBean.setBeginTime(formatTime(time));
            mlrcs.add(mLrcBean);
        }


    }

    int formatTime(String time) {
        time = time.replace('.', ':');
        String[] times = time.split(":");
        return Integer.parseInt(times[0]) * 60 * 1000
                + Integer.parseInt(times[1]) * 1000
                + Integer.parseInt(times[2]);

    }

    static StringBuilder stringBuilder = new StringBuilder();

    public static String formateTime(long time) {

        if (stringBuilder.length() > 0) stringBuilder.setLength(0);
        int m = (int) (time / (60 * 1000));
        time = time - m * (60 * 1000);
        int s = (int) (time / (1000));
        time = time - s * 1000;
        int ss = (int) (time);
        if (m < 10) {
            stringBuilder.append("0").append(m);
        } else {
            stringBuilder.append(m);
        }
        stringBuilder.append(":");
        if (s < 10) {
            stringBuilder.append("0").append(s);
        } else {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }


}
