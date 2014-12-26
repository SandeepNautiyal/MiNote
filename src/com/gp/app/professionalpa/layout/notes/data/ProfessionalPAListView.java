package com.gp.app.professionalpa.layout.notes.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.data.ListViewItem;
import com.gp.app.professionalpa.data.ListViewItemAdapter;

public class ProfessionalPAListView extends ListFragment
{

	private ArrayAdapter<ListViewItem> mAdapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		setContentView(R.layout.professional_pa_list_view_layout);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = (View)inflater.inflate(R.layout.professional_pa_list_view_layout, container, false);
		
		System.out.println("view height ="+view.getHeight()+" view width ="+view.getWidth());
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		Bundle bundle = this.getArguments();
		
		if(bundle != null)
		{
			Parcelable[] parceables = bundle.getParcelableArray("LIST_ITEMS");
			
			List<ListViewItem> values = new ArrayList<ListViewItem>();
			
			for(int i = 0, size = parceables == null ? 0 : parceables.length; i < size; i++)
			{
				values.add((ListViewItem)parceables[i]);
			}
			
			mAdapter = new ListViewItemAdapter(getActivity(), values);
			
			setListAdapter(mAdapter);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}
	
	public void addEmptyItemInList()
	{
        mAdapter.add(new ListViewItem(""));
        
        mAdapter.notifyDataSetChanged();
	}

}
