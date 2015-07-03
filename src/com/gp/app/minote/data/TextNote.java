package com.gp.app.minote.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.gp.app.minote.interfaces.XMLEntity;


public class TextNote extends Note
{
    public static final String NOTE_TABLE_NAME = "Note";
    public static final String NOTE_ID = "noteId";
    public static final String	NOTE_CREATION_TIME = "creationTime";
    public static final String	NOTE_MODIFIED_TIME ="lastEditedTime";
    public static final String	NOTE_TYPE="isParagraphNote";
    public static final String	NOTE_COLOR = "noteColor";

	private int noteId = -1;
	
	private byte state = XMLEntity.INSERT_STATE;
	
	private byte noteType = Note.LIST_NOTE;
	
	private List<NoteItem> noteItems = new ArrayList<NoteItem>();
	
	private long creationTime = 0L;
	
	private long lastEditedTime = 0L;
	
	private int noteColor = 0;

	public TextNote(int noteId, byte noteType, List<NoteItem> values) 
	{
		this.noteId = noteId;
		
		this.noteType = noteType;
		
		if(values != null)
		{
			this.noteItems = values;
		}
	}

	public TextNote()
	{
	}

	public TextNote(int noteId, byte noteType, int noteColor,
			long creationTime, long lastEditedTime) 
	{
        this.noteId = noteId;
		
		this.noteType = noteType;
		
		this.noteColor = noteColor;
		
		this.creationTime = creationTime;
		
		this.lastEditedTime = lastEditedTime;
	}

	public byte getType()
	{
		return noteType;
	}
	
	public boolean isListNote() 
	{
		return noteType == Note.LIST_NOTE;
	}

	public void setTypeOfNote(byte noteType) 
	{
		this.noteType = noteType;
	}

	public void addNoteItem(NoteItem item)
	{
		noteItems.add(item);
	}
	
	public List<NoteItem> getNoteItems()
	{
		return noteItems;
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
		dest.writeTypedList(noteItems);

		dest.writeByteArray(new byte[]{noteType, state});
		
		dest.writeLongArray(new long[]{creationTime, lastEditedTime});
		
		dest.writeIntArray(new int[]{noteId, noteColor});
	}

	public int getId()
	{
		return noteId;
	}

	public void setNoteId(int noteId) 
	{
		this.noteId = noteId;
	}

	public static final Parcelable.Creator<TextNote> CREATOR = new Parcelable.Creator<TextNote>() {

		@Override
		public TextNote createFromParcel(Parcel source) {
			
			List<NoteItem> noteItems = source.createTypedArrayList(NoteItem.CREATOR);
			
			byte [] noteState = new byte[2];
			
			source.readByteArray(noteState);
			
			long [] timeAttributes = new long[2];

			source.readLongArray(timeAttributes);
			
			int [] noteAttributes = new int[2];
			
			source.readIntArray(noteAttributes);
			
			TextNote note = new TextNote(noteAttributes[0], noteState[0], noteItems);
			
			note.setCreationTime(timeAttributes[0]);

			note.setLastEditedTime(timeAttributes[1]);
			
			note.setState(noteState[1]);
			
			note.setNoteColor(noteAttributes[1]);
			
			return note;
		}

		@Override
		public TextNote[] newArray(int size) {
			return new TextNote[size];
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
		return this.creationTime == ((TextNote)obj).getCreationTime()
				&& this.lastEditedTime == ((TextNote)obj).getLastEditedTime();
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("isParagraph note:"+noteType);
		
		sb.append("note color:"+noteColor);

		sb.append("Note item :"+noteItems);
		
		return sb.toString();
	}

	public void setNoteColor(int noteColor)
	{
		this.noteColor = noteColor;
	}
	
	public int getNoteColor() 
	{
		return noteColor;
	}
	
	public int getLength()
	{
		int length = 0;
		
		for(int i = 0; i < noteItems.size(); i++)
		{
			NoteItem item = noteItems.get(0);
			
			length = length + item.getLength();
		}
		
		return length;
	}

	public void setNoteItems(List<NoteItem> noteItems) 
	{
		if(noteItems != null && noteItems.size() > 0)
		{
			this.noteItems = noteItems;
		}
	}
}
