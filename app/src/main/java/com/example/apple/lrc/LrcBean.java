package com.example.apple.lrc;

import android.support.annotation.NonNull;

/**
 * Created by apple on 17/4/28.
 */

public class LrcBean implements Comparable<LrcBean> {


    private int beginTime;// 开始时间
    private int endTime;//结束时间
    private String lrc;//歌曲
    private int totleTime;//总时间
    private String songName;//歌曲名字
    private String singerName;//
    private String author;// 作曲

    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public int getTotleTime() {
        return totleTime;
    }

    public void setTotleTime(int totleTime) {
        this.totleTime = totleTime;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int compareTo(@NonNull LrcBean another) {
        return this.beginTime - another.beginTime ;

    }


    @Override
    public String toString() {
        return "LrcBean{" +
                "beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", lrc='" + lrc + '\'' +
                ", totleTime=" + totleTime +
                ", songName='" + songName + '\'' +
                ", singerName='" + singerName + '\'' +
                ", author='" + author + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
