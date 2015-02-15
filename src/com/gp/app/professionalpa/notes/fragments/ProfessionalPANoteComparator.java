package com.gp.app.professionalpa.notes.fragments;

import java.util.Comparator;

import com.gp.app.professionalpa.data.ProfessionalPANote;

public class ProfessionalPANoteComparator implements Comparator<ProfessionalPANote>
{

	@Override
	public int compare(ProfessionalPANote note1, ProfessionalPANote note2) 
	{
		return note1.compareTo(note2);
	}

}
