package com.gp.app.minote.calendar.adapter;

/**
 * Created by dell on 9/19/2015.
 */
public class DateInformation
{
    private int day;
    private int year;
    private int month;

    private boolean isClickable = true;

    private boolean isMonthName;

    private boolean isTodaysDate;


    public DateInformation(int day, int year, int month, boolean isClickable)
    {
        this.day = day;

        this.year = year;

        this.month = month;

        this.isClickable = isClickable;
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
        return this.day;
    }




        public boolean isClickable(){return isClickable;}
        /**
         *
         */
        public String toString()
        {
            StringBuilder sb = new StringBuilder();

            sb.append("\nday="+day);
            sb.append("\nmonth="+month);
            sb.append("\nyear="+year);

            return sb.toString();
        }

    public void setIsMonthName(boolean isMonthName)
    {
        this.isMonthName = isMonthName;
    }

    public boolean isMonthName()
    {
        return this.isMonthName;
    }

    public void setIsTodaysDate(boolean isTodaysDate)
    {
        this.isTodaysDate = isTodaysDate;
    }

    public boolean isTodaysDate()
    {
        return isTodaysDate;
    }
}
