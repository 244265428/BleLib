package com.czjk.blelib.model;

import android.os.Parcel;
import android.os.Parcelable;

/** 吃药提醒
 *
 */
public class MedicationRemind implements Parcelable {
    public String id;
    public String time1;    //提醒时间
    public String time2;
    public String time3;
    public String repeat;  // 重复
    public String t1State;   //0 is off，1 is on
    public String t2State;
    public String t3State;
    public String state;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.time1);
        dest.writeString(this.time2);
        dest.writeString(this.time3);
        dest.writeString(this.repeat);
        dest.writeString(this.t1State);
        dest.writeString(this.t2State);
        dest.writeString(this.t3State);
        dest.writeString(this.state);
    }

    public MedicationRemind() {
    }

    protected MedicationRemind(Parcel in) {
        this.id = in.readString();
        this.time1 = in.readString();
        this.time2 = in.readString();
        this.time3 = in.readString();
        this.repeat = in.readString();
        this.t1State = in.readString();
        this.t2State = in.readString();
        this.t3State = in.readString();
        this.state = in.readString();
    }

    public static final Creator<MedicationRemind> CREATOR = new Creator<MedicationRemind>() {
        @Override
        public MedicationRemind createFromParcel(Parcel source) {
            return new MedicationRemind(source);
        }

        @Override
        public MedicationRemind[] newArray(int size) {
            return new MedicationRemind[size];
        }
    };
}
