package com.gp.app.minote.data;

import android.os.Parcelable;

import com.gp.app.minote.interfaces.XMLEntity;

public abstract class Note implements XMLEntity, Parcelable
{
	public static final byte LIST_NOTE = 0;
	public static final byte PARAGRAPH_NOTE = 1;
	public static final byte IMAGE_NOTE = 2;
	public static final byte EVENT_NOTE = 3;
    
    public abstract byte getType();
    
    public abstract int getId();
}
