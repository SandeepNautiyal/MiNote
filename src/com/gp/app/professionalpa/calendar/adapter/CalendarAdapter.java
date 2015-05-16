package com.gp.app.professionalpa.calendar.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.calendar.events.DayEvents;
import com.gp.app.professionalpa.calendar.ui.EventCreationGUI;
import com.gp.app.professionalpa.calendar.ui.EventModificationGUI;
import com.gp.app.professionalpa.notification.ProfessionalPANotificationManager;

public class CalendarAdapter extends BaseAdapter
{
	private static final int GRIDS_OCCUPIED_BY_DAYS_NAMES = 7;
	private Context context;
	private Calendar cal;
	private String[] days;
	
	ArrayList<DayEvents> daysEventsList = new ArrayList<DayEvents>();
	
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
		return daysEventsList.get(position);
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
			TextView day = (TextView)v.findViewById(R.id.textView1);
			
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
		
			System.out.println("Calendar adapter -> getView -> inside if");
		}
		else
		{
	        v = vi.inflate(R.layout.day_view, null);
			FrameLayout today = (FrameLayout)v.findViewById(R.id.today_frame);
			Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
			DayEvents dayEvents = daysEventsList.get(position);
			
			if(dayEvents.getYear() == cal.get(Calendar.YEAR) && dayEvents.getMonth() == cal.get(Calendar.MONTH) && dayEvents.getDay() == cal.get(Calendar.DAY_OF_MONTH))
			{
				today.setVisibility(View.VISIBLE);
			}
			else
			{
				today.setVisibility(View.GONE);
			}
			
			final TextView dayTV = (TextView)v.findViewById(R.id.textView1);
			
			RelativeLayout rl = (RelativeLayout)v.findViewById(R.id.rl);
			ImageView iv = (ImageView)v.findViewById(R.id.imageView1);
			ImageView blue = (ImageView)v.findViewById(R.id.imageView2);
			ImageView purple = (ImageView)v.findViewById(R.id.imageView3);
			ImageView green = (ImageView)v.findViewById(R.id.imageView4);
			ImageView orange = (ImageView)v.findViewById(R.id.imageView5);
			ImageView red = (ImageView)v.findViewById(R.id.imageView6);	
			
			blue.setVisibility(View.VISIBLE);
			purple.setVisibility(View.VISIBLE);
			green.setVisibility(View.VISIBLE);
			purple.setVisibility(View.VISIBLE);
			orange.setVisibility(View.VISIBLE);
			red.setVisibility(View.VISIBLE);
			
			iv.setVisibility(View.VISIBLE);
			dayTV.setVisibility(View.VISIBLE);
			rl.setVisibility(View.VISIBLE);
			
			final DayEvents eventsInADay = daysEventsList.get(position);
			
			final int month = cal.get(Calendar.MONTH)+1;
			
			final int year = cal.get(Calendar.YEAR);
			
			if(eventsInADay.getNumOfEvents() > 0)
			{
//				Set<Integer> colors = eventsInADay.getColors();
//				
//				iv.setVisibility(View.INVISIBLE);
//				blue.setVisibility(View.INVISIBLE);
//				purple.setVisibility(View.INVISIBLE);
//				green.setVisibility(View.INVISIBLE);
//				purple.setVisibility(View.INVISIBLE);
//				orange.setVisibility(View.INVISIBLE);
//				red.setVisibility(View.INVISIBLE);
//				
//				if(colors.contains(0)){
//					iv.setVisibility(View.VISIBLE);
//				}
//				if(colors.contains(2)){
//					blue.setVisibility(View.VISIBLE);
//				}
//				if(colors.contains(4)){
//					purple.setVisibility(View.VISIBLE);
//				}
//				if(colors.contains(5)){
//					green.setVisibility(View.VISIBLE);
//				}
//				if(colors.contains(3)){
//					orange.setVisibility(View.VISIBLE);
//				}
//				if(colors.contains(1)){
//					red.setVisibility(View.VISIBLE);
//				}
				
			}else{
				iv.setVisibility(View.INVISIBLE);
				blue.setVisibility(View.INVISIBLE);
				purple.setVisibility(View.INVISIBLE);
				green.setVisibility(View.INVISIBLE);
				purple.setVisibility(View.INVISIBLE);
				orange.setVisibility(View.INVISIBLE);
				red.setVisibility(View.INVISIBLE);
			}
				
			if(eventsInADay.getDay() == 0)
			{
				rl.setVisibility(View.GONE);
			}
			else
			{
				dayTV.setVisibility(View.VISIBLE);
				final int day = eventsInADay.getDay();
				dayTV.setText(String.valueOf(day));
				dayTV.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						final PopupMenu popupMenu = new PopupMenu(context, dayTV);
						
                        popupMenu.inflate(R.menu.events_pop_up_menu);
				        
				        popupMenu.show();
				        
						popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() 
						{
							@Override
							public boolean onMenuItemClick(MenuItem item)
							{
							    if(item.getItemId() == R.id.createEvent)
								{
							    	new EventCreationGUI().createGuiForEventAddition(context, day, month, year);
								}
								else if(item.getItemId() == R.id.editEvent)
								{
									new EventModificationGUI().createEventModificationList(context, day, month, year);
								}
								else if(item.getItemId() == R.id.exit)
								{
									popupMenu.dismiss();
								}
									
								return false;
							}
						});
					}
				});
			}
		}
		
		return v;
	}
	
	public void refreshDays()
    {
    	// clear items
    	daysEventsList.clear();
    	
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
	        	DayEvents d = new DayEvents(0,0,0);
	        	daysEventsList.add(d);
	        }
        }
	    else 
	    {
	    	for(j=0;j<7;j++) 
	    	{
	        	days[j] = "";
	        	DayEvents d = new DayEvents(0,0,0);
	        	daysEventsList.add(d);
	        }
	    	j=1; // sunday => 1, monday => 7
	    }
        
        System.out.println("refresh days -> daysEventsList1 = "+daysEventsList.size());
        
        if(j>0 && daysEventsList.size() > 0 && j != 1)
        {
        	daysEventsList.remove(j-1);
        }
        
     // populate days
        int dayNumber = 1;
        for(int i=j-1;i<days.length;i++) 
        {
        	DayEvents dayEvents = new DayEvents(dayNumber,year,month);
        	
        	Calendar cTemp = Calendar.getInstance();
        	cTemp.set(year, month, dayNumber);
        	int startDay = Time.getJulianDay(cTemp.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(timeZone.getOffset(cTemp.getTimeInMillis())));
        	
        	dayEvents.setStartDay(startDay);
        	
        	days[i] = ""+dayNumber;
        	dayNumber++;
        	daysEventsList.add(dayEvents);
        }
        
        System.out.println("refreshDays -> dayList="+daysEventsList);
        
    }
}