package com.gp.app.professionalpa.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.interfaces.XMLEntity;


public class ProfessionalPANote implements XMLEntity, Parcelable, Comparable<ProfessionalPANote>
{
	byte state = XMLEntity.INSERT_STATE;
	
	private boolean isParagraphNote = false;
	
	private List<NoteListItem> notes = new ArrayList<NoteListItem>();
	
	private long creationTime = 0L;
	
	private long lastEditedTime = 0L;
	

	public ProfessionalPANote(boolean isParagraphNote, List<NoteListItem> values) 
	{
		this.isParagraphNote = isParagraphNote;
		
		if(values != null)
		{
			this.notes = values;
		}
	}

	public ProfessionalPANote()
	{
	}

	public boolean isParagraphNote() 
	{
		return isParagraphNote;
	}

	public void setTypeOfNote(boolean isParagraphNote) 
	{
		this.isParagraphNote = isParagraphNote;
	}

	public void addNoteItem(NoteListItem item)
	{
		notes.add(item);
	}
	
	public List<NoteListItem> getNoteItems()
	{
		return notes;
	}
	
//	public NoteListItem getMostRecentAddedNoteItem()
//	{
//		return notes.getLast();
//	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("isParagraph note:"+isParagraphNote);
		
		sb.append("Note item :"+notes);
		
		return sb.toString();
	}

	public void setCreationTime(long creationTime)
	{
		this.creationTime = creationTime;
	}

	public void setLastEditedTime(long lastEditedTime) 
	{
		this.lastEditedTime = lastEditedTime;
	}

	public long getCreationTime() 
	{
		return creationTime;
	}

	public long getLastEditedTime() 
	{
		return lastEditedTime;
	}

	@Override
	public byte getState()
	{
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeTypedList(notes);

		dest.writeBooleanArray(new boolean[] {isParagraphNote});
		
		dest.writeLongArray(new long[]{creationTime, lastEditedTime});
		
		dest.writeByte(state);
	}

	public static final Parcelable.Creator<ProfessionalPANote> CREATOR = new Parcelable.Creator<ProfessionalPANote>() {

		@Override
		public ProfessionalPANote createFromParcel(Parcel source) {
			
			List<NoteListItem> noteItems = source.createTypedArrayList(NoteListItem.CREATOR);
			
			boolean [] attributes = new boolean[1];
			
			source.readBooleanArray(attributes);
			
			boolean isParagraphNote = attributes[0];
			
			long [] timeAttributes = new long[2];

			source.readLongArray(timeAttributes);
			
			byte state = source.readByte();
			
			ProfessionalPANote note = new ProfessionalPANote(isParagraphNote, noteItems);
			
			note.setCreationTime(timeAttributes[0]);

			note.setLastEditedTime(timeAttributes[1]);
			
			note.setState(state);
			
			return note;
		}

		@Override
		public ProfessionalPANote[] newArray(int size) {
			return new ProfessionalPANote[size];
		}
	};
	
	@Override
	public int describeContents() 
	{
		return 0;
	}

	public int hashCode()
	{
		return (int)lastEditedTime;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return this.convertToString().equals(((ProfessionalPANote)obj).convertToString());
	}
	
	@Override
	public int compareTo(ProfessionalPANote note) 
	{
		return this.convertToString().compareTo(((ProfessionalPANote)note).convertToString());
	}
	
	public String convertToString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n creationTime="+creationTime);
		
		sb.append("lastEditedTime="+lastEditedTime);
		
		for(int i = 0; i < notes.size(); i++)
		{
			sb.append("\n Note-"+i+"="+notes.get(i).convertToString());
		}
		
	return sb.toString();
	}
}
