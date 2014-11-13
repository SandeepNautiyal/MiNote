package com.gp.app.professionalpa.data;

public class ListViewItem 
{
     private String textViewData = null;
     
     private boolean isImportanceHigh = false;
     
     private boolean isAlarmActive = false;
     
     public ListViewItem(String textViewData)
     {
    	 this.textViewData = textViewData;
     }
     
     public ListViewItem(String textViewData, boolean isImportanceHigh)
     {
    	 
    	 this(textViewData);
    	 
    	 this.isImportanceHigh = isImportanceHigh;
     }
     
     public ListViewItem(String textViewData, boolean isImportanceHigh, boolean isAlarmActive)
     {
    	 
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
}
