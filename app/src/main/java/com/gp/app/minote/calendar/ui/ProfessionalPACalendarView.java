package com.gp.app.minote.calendar.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gp.app.minote.R;
import com.gp.app.minote.calendar.adapter.CalendarAdapter;
import com.gp.app.minote.calendar.adapter.CalendarAdapter.DateInformation;
import com.gp.app.minote.calendar.events.database.CalendarDBManager;
import com.gp.app.minote.calendar.interfaces.DBChangeListener;
import com.gp.app.minote.data.Event;

import java.util.Calendar;
import java.util.Locale;

public class ProfessionalPACalendarView extends RelativeLayout implements OnItemClickListener,
	OnClickListener, DBChangeListener
{
	private Context context;
	private OnDayClickListener dayListener;
	private GridView calendar;
	private CalendarAdapter mAdapter;
	private Calendar cal;
	private TextView month;
	private RelativeLayout base;
	private ImageView next,prev;
	private boolean isWithoutEvents = false;

	public interface OnDayClickListener
	{
		public void onDayClicked(AdapterView<?> adapter, View view, int position, long id, DateInformation day);
	}

	public ProfessionalPACalendarView(Context context, boolean isWithoutEvents)
	{
		super(context);
		this.context = context;

		if(!isWithoutEvents)
		{
			CalendarDBManager.getInstance().addDataChangeListener(this);
		}

		this.isWithoutEvents = isWithoutEvents;

		init();
	}
	
	public ProfessionalPACalendarView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		this.context = context;
		init();
	}
	
	public ProfessionalPACalendarView(Context context, AttributeSet attrs,int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	
	private void init()
	{
		cal = Calendar.getInstance();
		base = new RelativeLayout(context);
        base.setBackgroundColor(Color.rgb(123,231,123));
		base.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		base.setMinimumHeight(30);
		base.setId(4);
		
		//Previous month image "<"

        RelativeLayout layout = new RelativeLayout(context);

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

		if(!isWithoutEvents)
		{
			params.leftMargin = 16;
			params.topMargin = 50;
		}
		else
		{
			params.leftMargin = 10;
			params.topMargin = 30;
		}

		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		prev = new ImageView(context);
		prev.setId(1);
		prev.setLayoutParams(params);
		prev.setImageResource(R.drawable.navigation_previous_item);
		prev.setOnClickListener(this);
        layout.addView(prev);
		
		//Month name at the top
		params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		month = new TextView(context);
		month.setId(2);
		month.setLayoutParams(params);
		month.setTextAppearance(context, android.R.attr.textAppearanceLarge);
		month.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + cal.get(Calendar.YEAR));
		if(!isWithoutEvents)
		{
			month.setTextSize(25);
		}
		else
		{
            month.setTextSize(20);
		}
        layout.addView(month);
		
		//Next month image ">"
		params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		if(!isWithoutEvents)
		{
			params.leftMargin = 16;
			params.topMargin = 50;
		}
		else
		{
			params.leftMargin = 10;
			params.topMargin = 30;
		}
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		next = new ImageView(context);
		next.setImageResource(R.drawable.navigation_next_item);
		next.setLayoutParams(params);
		next.setId(3);
		next.setOnClickListener(this);

        layout.addView(next);

        layout.setBackgroundColor(Color.rgb(120, 100, 255));

        base.addView(layout);
		
		addView(base);
		
		//Days and name of days "Sun, Mon  1,2"
		params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		if(!isWithoutEvents)
		{
			params.bottomMargin = 20;
		}
		else
		{
			params.bottomMargin = 5;

		}
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.BELOW, base.getId());
		
		calendar = new GridView(context);
        calendar.setOnTouchListener(new OnSwipeTouchListener(context));
		calendar.setLayoutParams(params);

		if(!isWithoutEvents)
		{
			calendar.setVerticalSpacing(4);
			calendar.setHorizontalSpacing(4);
		}
		else
		{
			calendar.setVerticalSpacing(2);
			calendar.setHorizontalSpacing(2);
		}

		calendar.setNumColumns(7);


		calendar.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
		calendar.setDrawSelectorOnTop(true);
		
		mAdapter = new CalendarAdapter(context,cal, isWithoutEvents);
		calendar.setAdapter(mAdapter);
		
		addView(calendar);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		if(dayListener != null)
		{
			DateInformation d = (DateInformation) mAdapter.getItem(arg2);
			
			if(d.getDay() != 0)
			{
				dayListener.onDayClicked(arg0, arg1, arg2, arg3,d);
			}
		}
	}
	
	/**
	 * 
	 * @param listener
	 * 
	 * Set a listener for when you press on a day in the month
	 */
	public void setOnDayClickListener(OnDayClickListener listener)
	{
		if(calendar != null)
		{
			dayListener = listener;
			calendar.setOnItemClickListener(this);
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case 1:
			previousMonth();
			break;
		case 3:
			nextMonth();
			break;
		default:
			break;
		}
	}
	
	private void previousMonth()
	{
		if(cal.get(Calendar.MONTH) == cal.getActualMinimum(Calendar.MONTH)) 
		{				
			cal.set((cal.get(Calendar.YEAR)-1),cal.getActualMaximum(Calendar.MONTH),1);
		}
		else
		{
			cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-1);
		}
		
		rebuildCalendar();
	}
	
	private void nextMonth()
	{
		if(cal.get(Calendar.MONTH) == cal.getActualMaximum(Calendar.MONTH)) 
		{				
			cal.set((cal.get(Calendar.YEAR)+1),cal.getActualMinimum(Calendar.MONTH),1);
		}
		else
		{
			cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)+1);
		}

		rebuildCalendar();
	}
	
	private void rebuildCalendar()
	{
		if(month != null)
		{
			month.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+" "+cal.get(Calendar.YEAR));

			refreshCalendar();
		}
	}
	
	/**
	 * Refreshes the month
	 */
	public void refreshCalendar()
	{
		mAdapter.refreshDays(cal.get(Calendar.DATE), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));

		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 
	 * @param color
	 * 
	 * Sets the background color of the month bar
	 */
	public void setMonthTextBackgroundColor(int color)
	{
		base.setBackgroundColor(color);
	}
	
	@SuppressLint("NewApi")
	/**
	 * 
	 * @param drawable
	 * 
	 * Sets the background color of the month bar. Requires at least API level 16
	 */
	public void setMonthTextBackgroundDrawable(Drawable drawable)
	{
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
		{
			base.setBackground(drawable);
		}
		
	}
	
	/**
	 * 
	 * @param resource
	 * 
	 * Sets the background color of the month bar
	 */
	public void setMonehtTextBackgroundResource(int resource)
	{
		base.setBackgroundResource(resource);
	}
	
	/**
	 * 
	 * @param recource
	 * 
	 * change the image of the previous month button
	 */
	public void setPreviousMonthButtonImageResource(int recource)
	{
		prev.setImageResource(recource);
	}
	
	/**
	 * 
	 * @param bitmap
	 * 
	 * change the image of the previous month button
	 */
	public void setPreviousMonthButtonImageBitmap(Bitmap bitmap)
	{
		prev.setImageBitmap(bitmap);
	}
	
	/**
	 * 
	 * @param drawable
	 * 
	 * change the image of the previous month button
	 */
	public void setPreviousMonthButtonImageDrawable(Drawable drawable)
	{
		prev.setImageDrawable(drawable);
	}
	
	/**
	 * 
	 * @param recource
	 * 
	 * change the image of the next month button
	 */
	public void setNextMonthButtonImageResource(int recource)
	{
		next.setImageResource(recource);
	}
	
	/**
	 * 
	 * @param bitmap
	 * 
	 * change the image of the next month button
	 */
	public void setNextMonthButtonImageBitmap(Bitmap bitmap)
	{
		next.setImageBitmap(bitmap);
	}
	
	/**
	 * 
	 * @param drawable
	 * 
	 * change the image of the next month button
	 */
	public void setNextMonthButtonImageDrawable(Drawable drawable)
	{
		next.setImageDrawable(drawable);
	}

	@Override
	public void recieveNotification(Event event)
	{
		mAdapter.notifyDataSetChanged();
	}

    public class OnSwipeTouchListener implements OnTouchListener {

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
                previousMonth();
            }
            public void onSwipeLeft() {
                nextMonth();
            }
        }
    }
}
