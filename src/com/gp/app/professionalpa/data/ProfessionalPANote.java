package com.gp.app.professionalpa.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.interfaces.XMLEntity;


public class ProfessionalPANote implements XMLEntity, Parcelable
{
	//<!DOCTYPE notes [
//<!ELEMENT notes (note+) #REQUIRED>
//<!ELEMENT note (noteItem+)>
//<!ATTLIST note noteId PCDATA #REQUIRED>
//<!ATTLIST note isParagraphNote PCDATA #REQUIRED>
//<!ATTLIST note creationTime PCDATA #REQUIRED>
//<!ATTLIST note lastEditedTime PCDATA #REQUIRED>
//<!ELEMENT noteItem (data, imageName)>
//<!ELEMENT data (#PCDATA)>
//<!ELEMENT imageName (#PCDATA)>
    public static final String NOTE_ID = "noteId";
    public static final String	NOTE_CREATION_TIME = "creationTime";
    public static final String	NOTE_MODIFIED_TIME ="lastEditedTime";
    public static final String	NOTE_TYPE="isParagraphNote";

	
	private int noteId = -1;
	
	private byte state = XMLEntity.INSERT_STATE;
	
	private byte noteType = ProfessionalPAConstants.LIST_NOTE;
	
	private List<NoteListItem> notes = new ArrayList<NoteListItem>();
	
	private long creationTime = 0L;
	
	private long lastEditedTime = 0L;
	
	private List<String> imageNames = new ArrayList<String>();
	

	public ProfessionalPANote(int noteId, byte noteType, List<NoteListItem> values) 
	{
		this.noteId = noteId;
		
		this.noteType = noteType;
		
		if(values != null)
		{
			this.notes = values;
		}
		
		System.out.println("ProfessionalPANote  -> values="+values+" size="+values.size());
		
		for(int i = 0, size = values != null ? values.size() : 0; i < size; i++)
		{
			NoteListItem noteItem = values.get(i);
			
			if(noteItem.getImageName() != null && !noteItem.getImageName().equals(""))
			{
				imageNames.add(noteItem.getImageName());
			}
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
		
		dest.writeInt(noteId);
	}

	public int getNoteId()
	{
		return noteId;
	}

	public void setNoteId(int noteId) 
	{
		this.noteId = noteId;
	}

	public static final Parcelable.Creator<ProfessionalPANote> CREATOR = new Parcelable.Creator<ProfessionalPANote>() {

		@Override
		public ProfessionalPANote createFromParcel(Parcel source) {
			
			List<NoteListItem> noteItems = source.createTypedArrayList(NoteListItem.CREATOR);
			
			byte [] noteState = new byte[2];
			
			source.readByteArray(noteState);
			
			long [] timeAttributes = new long[2];

			source.readLongArray(timeAttributes);
			
			int id = source.readInt();
			
			ProfessionalPANote note = new ProfessionalPANote(id, noteState[0], noteItems);
			
			note.setCreationTime(timeAttributes[0]);

			note.setLastEditedTime(timeAttributes[1]);
			
			note.setState(noteState[1]);
			
			note.setNoteId(id);
			
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
		return (int)creationTime;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return this.creationTime == ((ProfessionalPANote)obj).getCreationTime()
				&& this.lastEditedTime == ((ProfessionalPANote)obj).getLastEditedTime();
	}
	
	public static class NotePropertyValues 
	{
		private Map<String, String> properties = new HashMap<String, String>();
		
		private boolean isValidProperty(String key)
		{
			if(key.equals(NOTE_ID) || key.equals(NOTE_CREATION_TIME)
					|| key.equals(NOTE_MODIFIED_TIME)
					|| key.equals(NOTE_TYPE))
			{
				return true;
			}
			
			return false;
		}
		
		public void addProperties(String property, String propertyValue)
		{
			if(isValidProperty(property))
			{
				properties.put(property, propertyValue);
			}
		}
		
		public String getValue(String property)
		{
			if(isValidProperty(property))
			{
				return properties.get(property);
			}
			
			return null;
		}
	}

	public List<String> getImageNames()
	{
		return imageNames;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("isParagraph note:"+noteType);
		
		sb.append("Note item :"+notes);
		
		return sb.toString();
	}
}
