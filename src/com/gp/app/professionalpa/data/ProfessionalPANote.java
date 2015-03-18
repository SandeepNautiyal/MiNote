package com.gp.app.professionalpa.data;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.interfaces.XMLEntity;


public class ProfessionalPANote implements XMLEntity, Parcelable, Comparable<ProfessionalPANote>
{
	byte state = XMLEntity.INSERT_STATE;
	
	private byte noteType = ProfessionalPAConstants.LIST_NOTE;
	
	private List<NoteListItem> notes = new ArrayList<NoteListItem>();
	
	private long creationTime = 0L;
	
	private long lastEditedTime = 0L;
	

	public ProfessionalPANote(byte noteType, List<NoteListItem> values) 
	{
		this.noteType = noteType;
		
		if(values != null)
		{
			this.notes = values;
		}
	}

	public ProfessionalPANote()
	{
	}

	public byte getNoteType()
	{
		return noteType;
	}
	
	public boolean isParagraphNote() 
	{
		return noteType == ProfessionalPAConstants.PARAGRAPH_NOTE;
	}
	
	public boolean isImageNote() 
	{
		return noteType == ProfessionalPAConstants.IMAGE_NOTE;
	}
	
	public boolean isListNote() 
	{
		return noteType == ProfessionalPAConstants.LIST_NOTE;
	}

	public void setTypeOfNote(byte noteType) 
	{
		this.noteType = noteType;
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
		
		sb.append("isParagraph note:"+noteType);
		
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

		dest.writeByteArray(new byte[]{noteType, state});
		
		dest.writeLongArray(new long[]{creationTime, lastEditedTime});
	}

	public static final Parcelable.Creator<ProfessionalPANote> CREATOR = new Parcelable.Creator<ProfessionalPANote>() {

		@Override
		public ProfessionalPANote createFromParcel(Parcel source) {
			
			List<NoteListItem> noteItems = source.createTypedArrayList(NoteListItem.CREATOR);
			
			byte [] noteState = new byte[2];
			
			source.readByteArray(noteState);
			
			long [] timeAttributes = new long[2];

			source.readLongArray(timeAttributes);
			
			ProfessionalPANote note = new ProfessionalPANote(noteState[0], noteItems);
			
			note.setCreationTime(timeAttributes[0]);

			note.setLastEditedTime(timeAttributes[1]);
			
			note.setState(noteState[1]);
			
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
