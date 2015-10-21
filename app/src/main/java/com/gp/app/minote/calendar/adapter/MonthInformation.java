package com.gp.app.minote.calendar.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by dell on 9/19/2015.
 */
public class MonthInformation
{
    private static final int GRIDS_OCCUPIED_BY_DAYS_NAMES = 7;
    private List<DateInformation> dateList = new ArrayList<DateInformation>();
    private Calendar cal = null;

    public MonthInformation()
    {
        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        refreshDays(cal.get(Calendar.DATE), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
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

        Calendar nextMonthCalendar = Calendar.getInstance();
        nextMonthCalendar.add(month, 1);
        int nextMonthYear = nextMonthCalendar.get(Calendar.YEAR);
        int nextMonth = nextMonthCalendar.get(Calendar.MONTH)+1;

        TimeZone timeZone = TimeZone.getDefault();
        int startingDayOfGrid = lastDayOfPreviousMonth - firstDay + 1;


        // figure size of the array

        int totalDays = -1;

        int previousMonthAndCurrentMonthGrids = GRIDS_OCCUPIED_BY_DAYS_NAMES + firstDay + lastDay;

        int totalNumberOfGrids = getTotalGridSize(previousMonthAndCurrentMonthGrids);

        int previousMonthStartDays = lastDayOfPreviousMonth - firstDay + 1;

        int currentMonthStartDate = 1;

        int nextMonthStartDate = 1;

        int rowIndex = 2;
        int columnIndex = 0;


        for(int i = 7, j = 1; i < totalNumberOfGrids; i++, j++)
        {
            if(j < firstDay)
            {
                dateList.add(new DateInformation(++previousMonthStartDays, previousYear, previousMonth,false));
            }
            else if(j < firstDay + lastDay)
            {
                dateList.add(new DateInformation(currentMonthStartDate++, year, month, true));
            }
            else
            {
                dateList.add(new DateInformation(nextMonthStartDate++, nextMonthYear, nextMonth,false));
            }

            columnIndex++;

            if(columnIndex % 7 == 0)
            {
                columnIndex = 0;
                rowIndex++;
            }
        }
    }

    public List<DateInformation> getDateList()
    {
        return dateList;
    }

    private int getTotalGridSize(int dayCount)
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
}
