package com.czjk.blelib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by czjk on 2016/12/3.
 */

public class HeartRate implements Parcelable{
    private int date;
    private  String time;
    private String value;

    public HeartRate(int date, String time, String value) {
        this.date = date;
        this.time = time;
        this.value = value;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.date);
        dest.writeString(this.time);
        dest.writeString(this.value);
    }

    protected HeartRate(Parcel in) {
        this.date = in.readInt();
        this.time = in.readString();
        this.value = in.readString();
    }

    public static final Creator<HeartRate> CREATOR = new Creator<HeartRate>() {
        @Override
        public HeartRate createFromParcel(Parcel source) {
            return new HeartRate(source);
        }

        @Override
        public HeartRate[] newArray(int size) {
            return new HeartRate[size];
        }
    };
}
