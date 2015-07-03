package com.gp.app.minote.database.search;

import java.util.Set;

public interface NotesSearch 
{
	public Set<Integer> getMatchingNoteIds(String query);
}
