package com.gp.app.minote.calendar.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gp.app.minote.R;
import com.gp.app.minote.calendar.adapter.DateInformation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MiNoteCalendar extends GridLayout implements OnClickListener
{
    private static final byte PREVIOUS_MONTH =  -1;
    private static final byte CURRENT_MONTH =  0;
    private static final byte NEXT_MONTH =  1;
    protected Context context;
	protected Calendar currentVisibleMonthCalendar;

    private ImageButton previousMonthImageButton = null;
    private ImageButton nextMonthImageButton = null;
    protected int month = Calendar.getInstance().get(Calendar.MONTH);
    protected int year = Calendar.getInstance().get(Calendar.YEAR);
    protected final int todayDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    protected final int todayDateMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
    protected final int todayDateYear = Calendar.getInstance().get(Calendar.YEAR);
    protected TextView monthNameTextView = null;
	private List<DateInformation> dateList = new ArrayList<>();
    protected List<CalendarGridView> calendarGrids = new ArrayList<>();
    private CalendarDateClickListener calendarDateClickListener;
    private GridLayout gridLayout = null;

    public MiNoteCalendar(Context context)
	{
		super(context);
		this.context = context;
        currentVisibleMonthCalendar = Calendar.getInstance();

        initView();
	}
	
	public MiNoteCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
		this.context = context;
        initView();
    }
	
	public MiNoteCalendar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		initView();
    }

    private void initView()
    {
        init();

		refreshDays(CURRENT_MONTH, true);

        this.setOnTouchListener(new OnSwipeTouchListener(context));
    }

	private void init()
	{
		super.setId(R.id.minote_calendar);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        gridLayout = (GridLayout)inflater.inflate(R.layout.calendar_grid_layout, null);

		addView(gridLayout);

        previousMonthImageButton = (ImageButton) gridLayout.findViewById(R.id.previousMonthImageButton);

		previousMonthImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
                refreshDays(PREVIOUS_MONTH, false);
			}
		});

		monthNameTextView = (TextView) gridLayout.findViewById(R.id.monthNameTextView);

		nextMonthImageButton = (ImageButton) gridLayout.findViewById(R.id.nextMonthImageButton);

		nextMonthImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDays(NEXT_MONTH, false);
            }
        });

        for(int i = 10; i < ((GridLayout) gridLayout).getChildCount(); i++)
        {
            CalendarGridView gridView = (CalendarGridView)((GridLayout) gridLayout).getChildAt(i);

            calendarGrids.add(gridView);
        }
	}

	public void refreshDays(byte currentVisibleMonth, boolean isCurrentMonth)
	{
        // clear items
		dateList.clear();

        int currentMonth = currentVisibleMonthCalendar.get(Calendar.MONTH);
        int currentYear = currentVisibleMonthCalendar.get(Calendar.YEAR);

        if(currentVisibleMonth == PREVIOUS_MONTH)
		{
			currentVisibleMonthCalendar = getPreviousMonth(currentMonth, currentYear);
		}
		else if(currentVisibleMonth == NEXT_MONTH)
		{
			currentVisibleMonthCalendar = getNextMonth(currentMonth, currentYear);
		}

		currentVisibleMonthCalendar.set(Calendar.DATE, 1);

		int lastDay = currentVisibleMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int firstDay = currentVisibleMonthCalendar.get(Calendar.DAY_OF_WEEK);
        currentMonth = currentVisibleMonthCalendar.get(Calendar.MONTH);
        currentYear = currentVisibleMonthCalendar.get(Calendar.YEAR);

        month = currentMonth;
        year = currentYear;

        monthNameTextView.setText(currentVisibleMonthCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) +
                        " " + currentVisibleMonthCalendar.get(Calendar.YEAR));

		Calendar previousMonthCalendar = getPreviousMonth(currentMonth, currentYear);

		Calendar nextMonthCalendar = getNextMonth(currentMonth, currentYear);

		int lastDayOfPreviousMonth = previousMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		// figure size of the array

		int totalDays = -1;

		int previousMonthStartDays = lastDayOfPreviousMonth - firstDay + 1;

		int currentMonthDate = 1;

		int nextMonthStartDate = 1;

		for(int i = 1, j = 1; i <= calendarGrids.size(); i++, j++)
		{
			if(j < firstDay)
			{
				dateList.add(new DateInformation(++previousMonthStartDays, previousMonthCalendar.get(Calendar.YEAR),
						previousMonthCalendar.get(Calendar.MONTH)+1, false));
			}
			else if(j < firstDay + lastDay)
			{
                boolean isTodaysDate = false;

                int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                if(isCurrentMonth && currentMonthDate == date)
                {
                    isTodaysDate = true;
                }

                DateInformation dateInformation = new DateInformation(currentMonthDate++, currentYear, currentMonth+1, true);

                dateInformation.setIsTodaysDate(isTodaysDate);

				dateList.add(dateInformation);
			}
			else
			{
				dateList.add(new DateInformation(nextMonthStartDate++, nextMonthCalendar.get(Calendar.YEAR),
						nextMonthCalendar.get(Calendar.MONTH)+1, false));
			}
		}

        fillCalendarViewWithDates();
	}

	private Calendar getNextMonth(int currentMonth, int currentYear)
	{
		int nextMonth;
		int nextYear;

		if(currentMonth == 11)
		{
			nextMonth = 0;
			nextYear = currentYear + 1;
		}
		else
		{
			nextMonth = currentMonth+1;
			nextYear = currentYear;
		}

		Calendar  cal  =  Calendar.getInstance();

		cal.set(nextYear, nextMonth, 1);

		return cal;
	}

	private Calendar getPreviousMonth(int currentMonth, int currentYear)
	{
		int previousMonth;
		int previousYear;

		if(currentMonth == 0)
		{
			previousMonth = 11;
			previousYear = currentYear - 1;
		}
		else
		{
			previousMonth = currentMonth-1;
			previousYear = currentYear;
		}

		Calendar  cal  =  Calendar.getInstance();

		cal.set(previousYear,previousMonth,1);

		return cal;
	}

	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case 1:
            refreshDays(PREVIOUS_MONTH, false);
			break;
		case 3:
            refreshDays(NEXT_MONTH, false);
			break;
		default:
			break;
		}
	}

    private class OnSwipeTouchListener implements OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        public void onSwipeLeft() {
            System.out.println("swipe left");
        }

        public void onSwipeRight() {
            System.out.println("swipe right");
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();

                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }

            public void onSwipeRight()
            {
                refreshDays(PREVIOUS_MONTH, false);
            }
            public void onSwipeLeft() {
                refreshDays(NEXT_MONTH, false);
            }
        }
    }

    private void fillCalendarViewWithDates()
    {
        for(int i = 0; i < calendarGrids.size(); i++)
        {
            setDateInCalendarGrid(i, calendarGrids.get(i));
        }
    }

    private void setDateInCalendarGrid(int position, CalendarGridView calendarGridView)
    {
        if (position < dateList.size())
        {
            final DateInformation dateInformation = dateList.get(position);

            if (dateInformation.isTodaysDate())
            {
                calendarGridView.getDayTextView().setTextColor(Color.rgb(255, 0, 0));
            }

            final TextView textView = calendarGridView.getDayTextView();

            if (textView != null)
            {
                textView.setText(Integer.toString(dateInformation.getDay()));

                boolean isActivated = dateInformation.isClickable();

                calendarGridView.setIsActivated(isActivated);

                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        notifyDateClicked(dateInformation.getDay(), dateInformation.getMonth(), dateInformation.getYear());
                    }
                });
            }

            calendarGridView.getEventTextView().setVisibility(View.GONE);
        }
    }

    public void  setDateClickListener(CalendarDateClickListener calendarDateClickListener)
    {
        this.calendarDateClickListener = calendarDateClickListener;
    }

    private void notifyDateClicked(int day, int month, int year)
    {
        calendarDateClickListener.clickedDate(day, month, year);
    }
}
