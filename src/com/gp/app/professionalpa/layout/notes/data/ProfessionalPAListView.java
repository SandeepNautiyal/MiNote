package com.gp.app.professionalpa.layout.notes.data;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.data.ListViewItem;
import com.gp.app.professionalpa.data.ListViewItemAdapter;

public class ProfessionalPAListView extends ListFragment
{

	private ArrayList<ListViewItem> values = new ArrayList<ListViewItem>();
	
	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
	    super.onSaveInstanceState(outState);
	    
	    outState.putParcelableArrayList("Values", values);  
	 }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		System.out.println("onActivityCreated ->");
		
		super.onActivityCreated(savedInstanceState);
		
		Bundle bundle = this.getArguments();
		
		if(bundle != null)
		{
			Parcelable[] parceables = bundle.getParcelableArray("LIST_ITEMS");
			
			for(int i = 0, size = parceables == null ? 0 : parceables.length; i < size; i++)
			{
				values.add((ListViewItem)parceables[i]);
			}
			
			System.out.println("onActivityCreated -> values ="+values);

			ListViewItemAdapter mAdapter = new ListViewItemAdapter(getActivity(), values);
			
			setListAdapter(mAdapter);
			
			getListView().setDivider(null);
			
			getListView().setDividerHeight(0);			
			
			mAdapter.notifyDataSetChanged();
		}
		
		System.out.println("onActivityCreated <- return");
	}
}
