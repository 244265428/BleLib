package com.czjk.blelib.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Step implements Parcelable {

	private String date;  //日期  2016 11 10   HH 0--23  mm 00 10 20   50
	private int step; //步数
	private int type; //类型    0 步行  4 睡眠

	public Step(String date, int step, int type) {
		this.date = date;
		this.step = step;
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.date);
		dest.writeInt(this.step);
		dest.writeInt(this.type);
	}

	protected Step(Parcel in) {
		this.date = in.readString();
		this.step = in.readInt();
		this.type = in.readInt();
	}

	public static final Creator<Step> CREATOR = new Creator<Step>() {
		@Override
		public Step createFromParcel(Parcel source) {
			return new Step(source);
		}

		@Override
		public Step[] newArray(int size) {
			return new Step[size];
		}
	};
}
