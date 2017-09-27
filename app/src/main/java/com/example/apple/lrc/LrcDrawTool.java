package com.example.apple.lrc;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;

import java.util.List;

import static com.example.apple.lrc.LrcView.playTime;

/**
 * 绘制工具
 * Created by apple on 17/4/28.
 */

public class LrcDrawTool {

    final String TAG = "LrcDrawTool";


    List<LrcBean> lrcs;//歌词
    Paint mNormalpaint;// 普通歌词文本画笔
    Paint mHighLightpaint;// 高亮歌词文本画笔
    Paint mLinePaint;
    int width;
    int height;
    int center;//绘制歌词的中心点
    int wordHight;// 歌词高度和歌词行距

    String noLrc = "暂时没有歌词";
    float nolrcX;


    public void setParameter(Paint mNormalpaint, Paint mHighLightpaint, Paint mLinePaint, int width, int height, int wordHight) {
        this.wordHight = wordHight;
        this.mHighLightpaint = mHighLightpaint;
        this.mNormalpaint = mNormalpaint;
        this.mLinePaint = mLinePaint;
        this.width = width;
        this.height = height;
        center = height / 2;
        nolrcX = (width - mNormalpaint.measureText(noLrc)) / 2;
    }


    public void setData(List<LrcBean> lrcs) {
        this.lrcs = lrcs;
        account();
        accountLineLrcDurTime();
    }

    /**
     * 计算每行歌词时间
     */
    void accountLineLrcDurTime() {
        if (lrcs == null || lrcs.isEmpty()) return;
        for (int i = lrcs.size() - 1; i > 0; i--) {
            if (i > 0) {

                LrcBean lrcBean = lrcs.get(i);
                LrcBean next = lrcs.get(i - 1);
                if (lrcBean.getBeginTime() != next.getBeginTime()) {
                    next.setEndTime(lrcBean.getBeginTime());
                } else {
                    next.setEndTime(lrcBean.getEndTime());
                }
            }

        }

    }

    /**
     * 计算歌词坐标
     */
    void account() {
        center = height / 2;
        if (lrcs != null && lrcs.size() > 0) {
            for (int i = 0, size = lrcs.size(); i < size; i++) {
                LrcBean lrcBean = lrcs.get(i);
                if (!TextUtils.isEmpty(lrcBean.getLrc())) {
                    lrcBean.setX((width - mHighLightpaint.measureText(lrcBean.getLrc())) / 2);
                    lrcBean.setY(center);
                    center += wordHight;
                }
            }

        }
    }

    /**
     * 获取当前播放的歌词的角标
     */
    public int getIndexline() {
        int index = 0;
        if (lrcs == null || lrcs.size() == 0) return 0;
        for (int i = 0, size = lrcs.size(); i < size; i++) {
            LrcBean lrcBean = lrcs.get(i);
            if (playTime >= lrcBean.getBeginTime() && playTime < lrcBean.getEndTime()) {
                index = i;
            }
        }
        return index;
    }

    /**
     * 获取当前歌词对象
     *
     * @return
     */
    public LrcBean getCurrentLrcBean() {
        if (lrcs == null || lrcs.size() == 0) return null;
        return lrcs.get(getIndexline());
    }


    public void onDraw(Canvas canvas) {


        if (lrcs == null || lrcs.size() == 0) {
            //没有歌词
            canvas.drawText(noLrc, nolrcX, center, mNormalpaint);

            return;
        }
        for (int i = 0, size = lrcs.size(); i < size; i++) {
            LrcBean lrcBean = lrcs.get(i);
            String lrc = lrcBean.getLrc();

            if (!TextUtils.isEmpty(lrc)) {
                if (playTime >= lrcBean.getBeginTime() && playTime < lrcBean.getEndTime()) {
                    canvas.drawText(lrc, lrcBean.getX(), lrcBean.getY(), mHighLightpaint);
                } else {
                    canvas.drawText(lrc, lrcBean.getX(), lrcBean.getY(), mNormalpaint);

                }
            }

        }


    }

    LrcBean getLrcBeanByY(float y) {
        for (int i = lrcs.size() - 1; i >= 0; i--) {
            LrcBean lrcBean = lrcs.get(i);
            if (lrcBean != null && y > lrcBean.getY()) {
                return lrcBean;
            }


        }
        return null;
    }


    Path mpathline;

    public void onDrawLine(Canvas canvas, LrcView lrcView) {
        float y = height / 2 + lrcView.getScrollY();
        float x = width;
        if (lrcs == null || lrcs.isEmpty()) return;
        if (mpathline == null) {
            mpathline = new Path();
        }
        mpathline.reset();
        mpathline.moveTo(30, y - 30);
        mpathline.lineTo(30, y + 30);
        mpathline.lineTo(60, y);
        mpathline.close();
        canvas.drawPath(mpathline, mLinePaint);
        LrcBean lrcBeanByY = getLrcBeanByY(y);
        if (lrcBeanByY != null) {
            String time = LrcParse.formateTime(lrcBeanByY.getBeginTime());
            canvas.drawText(time, x - 150, y + 15, mLinePaint);
        } else {
            canvas.drawText("00:00", x - 150, y + 15, mLinePaint);
        }
        canvas.drawLine(100, y, width - 200, y, mLinePaint);
    }
}
