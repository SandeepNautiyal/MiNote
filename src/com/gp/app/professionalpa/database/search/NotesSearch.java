package com.gp.app.professionalpa.database.search;

import java.util.Set;

public interface NotesSearch 
{
	public Set<Integer> getMatchingNoteIds(String query);
}
