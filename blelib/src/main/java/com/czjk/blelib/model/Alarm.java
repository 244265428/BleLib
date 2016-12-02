package com.czjk.blelib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 闹钟
 */
public class Alarm implements Parcelable {
	public String id;
	public String time;
	public String state;
	public String name;
	public String repeat;


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.time);
		dest.writeString(this.state);
		dest.writeString(this.name);
		dest.writeString(this.repeat);
	}

	public Alarm() {
	}

	protected Alarm(Parcel in) {
		this.id = in.readString();
		this.time = in.readString();
		this.state = in.readString();
		this.name = in.readString();
		this.repeat = in.readString();
	}

	public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
		@Override
		public Alarm createFromParcel(Parcel source) {
			return new Alarm(source);
		}

		@Override
		public Alarm[] newArray(int size) {
			return new Alarm[size];
		}
	};
}
