package com.gp.app.professionalpa.notes.database;

import android.net.Uri;

public interface ProfessionalPADBConstants 
{
	public static final String DATABASE_NAME = "ProfessionalPA";
	public static final int DATABASE_VERSION = 4;
	public static final String AUTHORITY = "com.gp.professionalpa";
	public static final Uri EVENT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/events");
	public static final Uri EVENT_CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + "/events/");
	public static final Uri NOTES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notes");
	public static final Uri NOTES_CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + "/notes/");
	public static final Uri NOTE_ITEMS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/noteItems");
	public static final Uri NOTE_ITEMS_CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + "/noteItems/");
}