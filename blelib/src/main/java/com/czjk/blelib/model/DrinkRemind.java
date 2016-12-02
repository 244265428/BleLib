package com.czjk.blelib.model;

import android.os.Parcel;
import android.os.Parcelable;

/** 喝水提醒
 *
 */
public class DrinkRemind implements Parcelable {
    public String id;
    public String time;    //提醒时间  时 分
    public String intervalMinutes;    //间隔时间   分
    public String frequency;  // 0--6
    public String state;   //0 is off，1 is on


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.time);
        dest.writeString(this.intervalMinutes);
        dest.writeString(this.frequency);
        dest.writeString(this.state);
    }

    public DrinkRemind() {
    }

    protected DrinkRemind(Parcel in) {
        this.id = in.readString();
        this.time = in.readString();
        this.intervalMinutes = in.readString();
        this.frequency = in.readString();
        this.state = in.readString();
    }

    public static final Creator<DrinkRemind> CREATOR = new Creator<DrinkRemind>() {
        @Override
        public DrinkRemind createFromParcel(Parcel source) {
            return new DrinkRemind(source);
        }

        @Override
        public DrinkRemind[] newArray(int size) {
            return new DrinkRemind[size];
        }
    };
}
