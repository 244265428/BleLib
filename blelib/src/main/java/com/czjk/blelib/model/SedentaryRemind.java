package com.czjk.blelib.model;

import android.os.Parcel;
import android.os.Parcelable;

/** 久坐提醒
 *
 */
public class SedentaryRemind implements Parcelable {
    public String id;
    public String startTime;    //开始时间  时
    public String endTime;    //结束时间  时
    public String detectionTime;    //检测时间  时
    public String state;   //0 is off，1 is on
    public String repeat;   //


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.detectionTime);
        dest.writeString(this.state);
        dest.writeString(this.repeat);
    }

    public SedentaryRemind() {
    }

    protected SedentaryRemind(Parcel in) {
        this.id = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.detectionTime = in.readString();
        this.state = in.readString();
        this.repeat = in.readString();
    }

    public static final Creator<SedentaryRemind> CREATOR = new Creator<SedentaryRemind>() {
        @Override
        public SedentaryRemind createFromParcel(Parcel source) {
            return new SedentaryRemind(source);
        }

        @Override
        public SedentaryRemind[] newArray(int size) {
            return new SedentaryRemind[size];
        }
    };
}
