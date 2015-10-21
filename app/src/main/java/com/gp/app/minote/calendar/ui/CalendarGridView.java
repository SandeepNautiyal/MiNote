package com.gp.app.minote.calendar.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gp.app.minote.R;
import com.gp.app.minote.calendar.events.database.CalendarDBManager;

/**
 * Created by dell on 9/25/2015.
 */
public class CalendarGridView extends RelativeLayout
{
    private Context context = null;

    private TextView eventTextView = null;

    private TextView dayTextView = null;

    private boolean isGridActivtated = true;

    public CalendarGridView(Context context)
    {
        super(context);

        this.context = context;

        init();
    }

    public CalendarGridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CalendarGridView(Context context, AttributeSet attrs,int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init()
    {
        int id = (int)Math.abs(Math.random()*100000);

        super.setId(id);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        addView(inflater.inflate(R.layout.day_view, null));

        eventTextView = (TextView) findViewById(R.id.eventTextView);

        dayTextView = (TextView) findViewById(R.id.dayTextView);

        eventTextView.setOnClickListener(new GridClickListener());

        dayTextView.setOnClickListener(new GridClickListener());
    }

    public TextView getEventTextView()
    {
        return eventTextView;
    }

    public TextView getDayTextView()
    {
        return dayTextView;
    }

    public void setIsActivated(boolean isGridActivtated)
    {
        this.isGridActivtated = isGridActivtated;
    }

    public boolean isGridActivtated()
    {
        return isGridActivtated;
    }

    class GridClickListener implements OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            calculateClickedGrid(v);
        }
    }

    private void calculateClickedGrid(View v)
    {
        int id = ((ViewGroup)v.getParent()).getId();

        GridCordinates cordinates = GridViewUtil.getGridCordinate(id);

        if(cordinates != null)
        {

        }
    }

    int getDay()
    {
        CharSequence dateText = dayTextView.getText();

        int day = -1;

        if(dateText != null)
        {
            String dayString = dateText.toString().trim();

            day = Integer.valueOf(dayString);
        }

        return day;
    }
}
