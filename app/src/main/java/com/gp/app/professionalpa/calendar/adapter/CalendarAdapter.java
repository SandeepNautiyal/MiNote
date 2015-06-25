package com.gp.app.professionalpa.calendar.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.calendar.events.database.CalendarDBManager;
import com.gp.app.professionalpa.calendar.ui.EventCreationGUI;
import com.gp.app.professionalpa.calendar.ui.EventModificationGUI;
import com.gp.app.professionalpa.data.Event;
import com.gp.app.professionalpa.util.ProfessionalPAUtil;

public class CalendarAdapter extends BaseAdapter
{
	private static final int GRIDS_OCCUPIED_BY_DAYS_NAMES = 7;
	private Context context;
	private Calendar cal;
	private String[] days;
	
	ArrayList<DateInformation> dateList = new ArrayList<DateInformation>();
	
	public CalendarAdapter(Context context, Calendar cal)
	{
		this.cal = cal;
		this.context = context;
		cal.set(Calendar.DAY_OF_MONTH, 1);
		refreshDays();
	}

	@Override
	public int getCount() 
	{
		return days.length;
	}

	@Override
	public Object getItem(int position) 
	{
		return dateList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}
	
	public int getPrevMonth()
	{
		if(cal.get(Calendar.MONTH) == cal.getActualMinimum(Calendar.MONTH))
		{
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR-1));
		}
		
		int month = cal.get(Calendar.MONTH);
		
		if(month == 0)
		{
			return month = 11;
		}
		
		return month-1;
	}
	
	public int getMonth()
	{
		return cal.get(Calendar.MONTH);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		View v = convertView;
		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(position >= 0 && position < 7){
			v = vi.inflate(R.layout.day_of_week, null);
			TextView day = (TextView)v.findViewById(R.id.dayTextView2);
			
			if(position == 0){
				day.setText(R.string.sunday);
			}else if(position == 1){
				day.setText(R.string.monday);
			}else if(position == 2){
				day.setText(R.string.tuesday);
			}else if(position == 3){
				day.setText(R.string.wednesday);
			}else if(position == 4){
				day.setText(R.string.thursday);
			}else if(position == 5){
				day.setText(R.string.friday);
			}else if(position == 6){
				day.setText(R.string.saturday);
			}
		}
		else
		{
	        v = vi.inflate(R.layout.day_view, null);
			
	        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
			
	        DateInformation date = dateList.get(position);
			
	        final int day = date.getDay();
			final int month = date.getMonth()+1;
			final int year = date.getYear();
			
			String formattedDate = ProfessionalPAUtil.pad(date.getDay())+"/"+ProfessionalPAUtil.pad(date.getMonth()+1)+"/"+ProfessionalPAUtil.pad(date.getYear());
			
			System.out.println("date event -> formattedDate="+formattedDate);
			
			List<Event> events = CalendarDBManager.getInstance().readEvents(formattedDate);
			
			int numberOfEvents = events.size();
			
			if(numberOfEvents > 0)
			{
				TextView dayTextView = (TextView)v.findViewById(R.id.dayTextView1);
				
				dayTextView.setText(String.valueOf(numberOfEvents));
				
				dayTextView.setVisibility(View.VISIBLE);
				
				dayTextView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) 
					{
						new EventModificationGUI().createEventModificationList(context, day, month, year);
					}
				});
			}
			
			final TextView dayTextView = (TextView)v.findViewById(R.id.dayTextView2);

			if(date.getYear() == cal.get(Calendar.YEAR) && date.getMonth() == cal.get(Calendar.MONTH) && date.getDay() == cal.get(Calendar.DAY_OF_MONTH))
			{
				dayTextView.setBackgroundResource(R.drawable.today);
			}
			
			dayTextView.setVisibility(View.VISIBLE);
			
			RelativeLayout dayRelativeLayout = (RelativeLayout)v.findViewById(R.id.dayRelativeLayout);
			
			dayRelativeLayout.setVisibility(View.VISIBLE);
			
			if(date.getDay() == 0)
			{
				dayRelativeLayout.setVisibility(View.GONE);
			}
			else
			{
				dayTextView.setVisibility(View.VISIBLE);
				dayTextView.setText(String.valueOf(day));
				dayTextView.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						dayTextView.setBackgroundResource(R.drawable.today);
						
						createPopUpMenuForDate(dayTextView, month, year, day);
					}
				});
			}
		}
		
		return v;
	}
	
	public void refreshDays()
    {
    	// clear items
    	dateList.clear();
    	
    	int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)+GRIDS_OCCUPIED_BY_DAYS_NAMES;
        int firstDay = (int)cal.get(Calendar.DAY_OF_WEEK);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        TimeZone timeZone = TimeZone.getDefault();
        
        // figure size of the array
        if(firstDay==1)
        {
        	days = new String[lastDay];
        }
        else 
        {
        	days = new String[lastDay+firstDay-1];
        }
        
        int j=0;
        
        // populate empty days before first real day
        if(firstDay>1) 
        {
	        for(j=0; j< firstDay+GRIDS_OCCUPIED_BY_DAYS_NAMES; j++) 
	        {
	        	days[j] = "";
	        	DateInformation d = new DateInformation(0,0,0);
	        	dateList.add(d);
	        }
        }
	    else 
	    {
	    	for(j=0;j<7;j++) 
	    	{
	        	days[j] = "";
	        	DateInformation d = new DateInformation(0,0,0);
	        	dateList.add(d);
	        }
	    	j=1; // sunday => 1, monday => 7
	    }
        
        System.out.println("refresh days -> daysEventsList1 = "+dateList.size());
        
        if(j>0 && dateList.size() > 0 && j != 1)
        {
        	dateList.remove(j-1);
        }
        
     // populate days
        int dayNumber = 1;
        for(int i=j-1;i<days.length;i++) 
        {
        	DateInformation dayEvents = new DateInformation(dayNumber,year,month);
        	
        	Calendar cTemp = Calendar.getInstance();
        	cTemp.set(year, month, dayNumber);
        	days[i] = ""+dayNumber;
        	dayNumber++;
        	dateList.add(dayEvents);
        }
    }
	
	public class DateInformation
	{
		private int day;
		private int year;
		private int month;
		
		public DateInformation(int day, int year, int month)
		{
			this.day = day;
			this.year = year;
			this.month = month;
			Calendar cal = Calendar.getInstance();
			cal.set(year, month-1, day);
			int end = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(year, month, end);
		}
		
		public int getMonth()
		{
			return month;
		}
		
		public int getYear()
		{
			return year;
		}
		
		public void setDay(int day)
		{
			this.day = day;
		}
		
		public int getDay()
		{
			return day;
		}
		
		/**
		 * 
		 */
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			
			sb.append("\nday="+day);
			sb.append("\nmonth="+month);
			
			return sb.toString();
		}
	}
	
	private void createPopUpMenuForDate(final TextView dayTV,
			final int month, final int year, final int day)
	{
		final PopupMenu popupMenu = new PopupMenu(context, dayTV);
		
        popupMenu.inflate(R.menu.events_pop_up_menu);
        
        popupMenu.show();
        
		popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() 
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
			    createDialogForMenuItem(month, year, day, popupMenu, item);
					
				return false;
			}
		});
	}
	
	private void createDialogForMenuItem(final int month, final int year, final int day, final PopupMenu popupMenu,
			MenuItem item) 
	{
		if(item.getItemId() == R.id.createEvent)
		{
	    	new EventCreationGUI().createGuiForEventAddition(context, day, month, year, EventCreationGUI.CREATE_GUI_IN_CREATE_MODE);
		}
		else if(item.getItemId() == R.id.editEvent)
		{
			new EventModificationGUI().createEventModificationList(context, day, month, year);
		}
		else if(item.getItemId() == R.id.exit)
		{
			popupMenu.dismiss();
		}
	}
}