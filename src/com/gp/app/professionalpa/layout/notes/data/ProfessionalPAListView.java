package com.gp.app.professionalpa.layout.notes.data;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.data.ListViewItem;
import com.gp.app.professionalpa.data.ListViewItemAdapter;

public class ProfessionalPAListView extends ListFragment
{

	private BaseAdapter mAdapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		setContentView(R.layout.professional_pa_list_view_layout);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
//		View view = inflater.inflate(R.layout.composite_control_for_list_view, null);
		
		ListViewItem [] values = {new ListViewItem("")};
		
		mAdapter = new ListViewItemAdapter(getActivity(), values);
		
		setListAdapter(mAdapter);
		
		return inflater.inflate(R.layout.professional_pa_list_view_layout, container, false);
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

}
