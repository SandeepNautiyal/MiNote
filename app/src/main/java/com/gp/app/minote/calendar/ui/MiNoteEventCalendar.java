package com.gp.app.minote.calendar.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.gp.app.minote.R;
import com.gp.app.minote.calendar.events.EventManager;
import com.gp.app.minote.calendar.events.database.CalendarDBManager;
import com.gp.app.minote.calendar.interfaces.DBChangeListener;
import com.gp.app.minote.data.Event;
import com.gp.app.minote.util.MiNoteParameters;
import com.gp.app.minote.util.MiNoteUtil;

import java.util.Calendar;
import java.util.List;

/**
 * Created by dell on 10/7/2015.
 */
public class MiNoteEventCalendar extends MiNoteCalendar implements
        View.OnClickListener, DBChangeListener
{
        public MiNoteEventCalendar(Context context)
        {
                super(context);

                CalendarDBManager.getInstance().addDataChangeListener(this);

                init();
        }

        public MiNoteEventCalendar(Context context, AttributeSet attrs)
        {
                super(context, attrs);

                init();
        }

        public MiNoteEventCalendar(Context context, AttributeSet attrs, int defStyle)
        {
                super(context, attrs, defStyle);

                init();
        }

        private void init()
        {
            CalendarDBManager.getInstance().addDataChangeListener(this);

            for(int i = 0; i < calendarGrids.size(); i++)
            {
                initEventCalendar(calendarGrids.get(i));

                attachClickListener(calendarGrids.get(i));
            }
        }

        private void attachClickListener(final CalendarGridView calendarGridView)
        {
                final TextView textView = calendarGridView.getDayTextView();

                textView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                createPopUpMenuForDate(textView, calendarGridView.getDay(), month+1, year);
                        }
                });
        }

        private void initEventCalendar(CalendarGridView calendarGridView)
        {
                final int day = calendarGridView.getDay();

                String formattedDate = MiNoteUtil.pad(day) + "/" + MiNoteUtil.pad(month + 1) + "/" + MiNoteUtil.pad(year);

                List<Event> events = CalendarDBManager.getInstance().readEvents(formattedDate);

                int numberOfEvents = events.size();

                TextView eventTextView = calendarGridView.getEventTextView();

                if (numberOfEvents > 0)
                {
                        eventTextView.setText(String.valueOf(numberOfEvents));

                        eventTextView.setVisibility(View.VISIBLE);

                        eventTextView.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                        new EventModificationGUI().createEventModificationList(context,
                                                day, month+1, year);
                                }
                        });

                        eventTextView.setBackgroundColor(Color.rgb(120, 100, 255));
                }
        }

        @Override
        public void recieveNotification(byte command, Event event)
        {
                String startDate = event.getStartDate();

                String startTime = event.getStartTime();

                long eventTime = MiNoteUtil.createTime(startDate, startTime);

                final Calendar cal = Calendar.getInstance();

                cal.setTimeInMillis(eventTime);

                int date = cal.get(Calendar.DAY_OF_MONTH);

                List<Event> events = EventManager.getEvents(startDate);

                int numberOfEvents = events.size();


                for(int i = 0; i < calendarGrids.size(); i++)
                {
                        CalendarGridView grid = calendarGrids.get(i);

                        if(grid.getDay() == date)
                        {
                                if(numberOfEvents > 0)
                                {
                                        grid.getEventTextView().setText(String.valueOf(numberOfEvents));

                                        grid.getEventTextView().setVisibility(View.VISIBLE);

                                        grid.getEventTextView().setBackgroundColor(Color.rgb(120, 100, 255));

                                }
                                else
                                {
                                        grid.getEventTextView().setText("");

                                        grid.getEventTextView().setVisibility(View.INVISIBLE);
                                }

                                break;
                        }
                }
        }

        private void createPopUpMenuForDate(final TextView dayTV, final int month, final int year, final int day)
        {
                final int color = dayTV.getDrawingCacheBackgroundColor();

                dayTV.setBackgroundResource(R.drawable.day_selected);

                Context context = MiNoteParameters.getApplicationContext();

                final PopupMenu popupMenu = new PopupMenu(context, dayTV);

                popupMenu.inflate(R.menu.events_pop_up_menu);

                popupMenu.show();

                popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener()
                {
                        @Override
                        public void onDismiss(PopupMenu menu)
                        {
                                dayTV.setBackgroundColor(color);
                        }
                });

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {
                                createDialogForMenuItem(month, year, day, popupMenu, item);

                                dayTV.setBackgroundColor(color);

                                return false;
                        }
                });
        }

        private void createDialogForMenuItem(final int month, final int year, final int day, final PopupMenu popupMenu,
                                             MenuItem item)
        {
                if(item.getItemId() == R.id.createEvent)
                {
                        new EventCreationUI(context).createEventUI(false);
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
