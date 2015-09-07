package com.gp.app.minote.calendar.adapter;

import android.content.Context;
import android.graphics.Color;
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

import com.gp.app.minote.R;
import com.gp.app.minote.calendar.events.database.CalendarDBManager;
import com.gp.app.minote.calendar.ui.CalendarGestureListener;
import com.gp.app.minote.calendar.ui.EventCreationGUI;
import com.gp.app.minote.calendar.ui.EventModificationGUI;
import com.gp.app.minote.data.Event;
import com.gp.app.minote.util.MiNoteUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarAdapter extends BaseAdapter
{
	private static final int GRIDS_OCCUPIED_BY_DAYS_NAMES = 7;
	private Context context;
	private Calendar cal;
	private boolean isWithoutEvent = false;
	List<DateInformation> dateList = new ArrayList<DateInformation>();

	public CalendarAdapter(Context context, Calendar cal, boolean isWithoutEvent)
	{
		this.cal = cal;
		this.context = context;
		cal.set(Calendar.DAY_OF_MONTH, 1);
		this.isWithoutEvent = isWithoutEvent;
		refreshDays(cal.get(Calendar.DATE), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
	}

	@Override
	public int getCount()
	{
		return dateList.size();
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
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR -1));
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

			String formattedDate = MiNoteUtil.pad(date.getDay())+"/"+MiNoteUtil.pad(date.getMonth()+1)+"/"+MiNoteUtil.pad(date.getYear());

			TextView dayTextView1 = (TextView)v.findViewById(R.id.dayTextView1);

			if(!isWithoutEvent)
			{
				List<Event> events = CalendarDBManager.getInstance().readEvents(formattedDate);

				int numberOfEvents = events.size();

				if(numberOfEvents > 0)
				{
					dayTextView1.setText(String.valueOf(numberOfEvents));

					dayTextView1.setVisibility(View.VISIBLE);

					dayTextView1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v)
						{
							new EventModificationGUI().createEventModificationList(context, day, month, year);
						}
					});
				}
			}
			else
			{
				dayTextView1.setVisibility(View.GONE);
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

				if(!isWithoutEvent)
				{
					dayTextView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dayTextView.setBackgroundResource(R.drawable.today);

							createPopUpMenuForDate(dayTextView, month, year, day);
						}
					});
				}
				else
				{
					//TODO height to be read from drawable file
					dayTextView.setBackgroundResource(R.drawable.calendar_readonly_edittext);

					dayRelativeLayout.setMinimumHeight(0);

					dayRelativeLayout.setMinimumWidth(0);

				}
			}
		}

		return v;
	}

	public void refreshDays(int day, int month, int year)
    {
    	// clear items
    	dateList.clear();

        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = (int)cal.get(Calendar.DAY_OF_WEEK);


        System.out.println("refreshTable -> year=" + year + " month=" + month +" day="+day+ " firstDay=" + firstDay + " lastDay=" + lastDay);

        Calendar previousMonthCalendar = Calendar.getInstance();

        int previousYear = year;
        int previousMonth = month;

        if(month == 1)
        {
            previousYear = previousYear - 1;

            previousMonth = 11;
        }
        else
        {
            previousMonth--;
        }

        previousMonthCalendar.set(previousYear, previousMonth, 1);
        int lastDayOfPreviousMonth = previousMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        System.out.println("refreshTable -> previousMonthYear="+previousYear+" previousMonth="+previousMonth+" lastDayOfPreviousMonth="+lastDayOfPreviousMonth);

        Calendar nextMonthCalendar = Calendar.getInstance();
        nextMonthCalendar.add(month, 1);
        int nextMonthYear = nextMonthCalendar.get(Calendar.YEAR);
        int nextMonth = nextMonthCalendar.get(Calendar.MONTH)+1;

        System.out.println("refreshTable -> nextMonthYear="+nextMonthYear+" nextMonth="+nextMonth);

        TimeZone timeZone = TimeZone.getDefault();
        int startingDayOfGrid = lastDayOfPreviousMonth - firstDay + 1;


        // figure size of the array

        int totalDays = -1;

        int previousMonthAndCurrentMonthGrids = GRIDS_OCCUPIED_BY_DAYS_NAMES + firstDay + lastDay;

        int totalNumberOfGrids = getTotalGridSize(previousMonthAndCurrentMonthGrids);

        for(int i = 0 ; i < GRIDS_OCCUPIED_BY_DAYS_NAMES; i++)
        {
            dateList.add(new DateInformation(0, 0, 0));
        }

        int previousMonthStartDays = lastDayOfPreviousMonth - firstDay + 1;

        int currentMonthStartDate = 1;

        int nextMonthStartDate = 1;

        for(int i = 7, j = 1; i < totalNumberOfGrids; i++, j++)
        {
            if(j < firstDay)
            {
                dateList.add(new DateInformation(++previousMonthStartDays, previousYear, previousMonth));
            }
            else if(j < firstDay + lastDay)
            {
                dateList.add(new DateInformation(currentMonthStartDate++, year, month));
            }
            else
            {
                dateList.add(new DateInformation(nextMonthStartDate++, nextMonthYear, nextMonth));
            }
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

    public int getTotalGridSize(int dayCount)
    {
        int gridCount = 0;

        if(dayCount <= 28)
        {
            gridCount = 28;
        }
        else if(dayCount <= 35)
        {
            gridCount = 35;
        }
        else if(dayCount <= 42)
        {
            gridCount = 42;
        }
        else if(dayCount <= 49)
        {
            gridCount = 49;
        }

        return gridCount;
    }
}