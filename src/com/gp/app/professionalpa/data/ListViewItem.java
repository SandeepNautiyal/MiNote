package com.gp.app.professionalpa.data;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class ListViewItem implements Parcelable 
{
	private static final byte HIGH_IMPORTANCE_INDEX = 0;
	
	private static final byte ALARM_ACTIVE_INDEX = 1;
	
	private String textViewData = null;

	private boolean isImportanceHigh = false;

	private boolean isAlarmActive = false;

	public ListViewItem(String textViewData) {
		this.textViewData = textViewData;
	}

	public ListViewItem(String textViewData, boolean isImportanceHigh) {

		this(textViewData);

		this.isImportanceHigh = isImportanceHigh;
	}

	public ListViewItem(String textViewData, boolean isImportanceHigh,
			boolean isAlarmActive) {

		this(textViewData, isImportanceHigh);

		this.isAlarmActive = isAlarmActive;
	}

	public String getTextViewData() {
		return textViewData;
	}

	public void setTextViewData(String textViewData) {
		this.textViewData = textViewData;
	}

	public boolean isImportanceHigh() {
		return isImportanceHigh;
	}

	public void setImportanceHigh(boolean isImportanceHigh) {
		this.isImportanceHigh = isImportanceHigh;
	}

	public boolean isAlarmActive() {
		return isAlarmActive;
	}

	public void setAlarmActive(boolean isAlarmActive) {
		this.isAlarmActive = isAlarmActive;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		System.out.println("writeToParcel -> textViewData="+textViewData);
		
		dest.writeString(textViewData);

		dest.writeBooleanArray(new boolean[] { isImportanceHigh, isAlarmActive });
	}

	public static final Parcelable.Creator<ListViewItem> CREATOR = new Parcelable.Creator<ListViewItem>() {

		@Override
		public ListViewItem createFromParcel(Parcel source) {
			
			String textViewData = source.readString();
			
			boolean [] attributes = new boolean[2];
			
			source.readBooleanArray(attributes);
			
			boolean isImportanceHigh = attributes[HIGH_IMPORTANCE_INDEX];
			
			boolean isAlarmActive = attributes[ALARM_ACTIVE_INDEX];
			
			System.out.println("createFromParcel -> textViewData="+textViewData);

			return new ListViewItem(textViewData, isImportanceHigh, isAlarmActive);
		}

		@Override
		public ListViewItem[] newArray(int size) {
			return new ListViewItem[size];
		}
	};
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("textViewData="+textViewData);
		
		return builder.toString();
	}
}
