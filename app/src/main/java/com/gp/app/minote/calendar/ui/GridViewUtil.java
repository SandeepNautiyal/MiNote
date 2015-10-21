package com.gp.app.minote.calendar.ui;

import com.gp.app.minote.R;

/**
 * Created by dell on 9/25/2015.
 */
public class GridViewUtil
{
    public static GridCordinates getGridCordinate(int parentId)
    {
        byte x = 0;
        byte y = 0;

        switch(parentId)
        {
            case R.id.day1:
                x=0; y=0;
                break;
            case R.id.day2:
                x=0; y=1;
                break;
            case R.id.day3:
                x=0; y=2;
                break;
            case R.id.day4:
                x=0; y=3;
                break;
            case R.id.day5:
                x=0; y=4;
                break;
            case R.id.day6:
                x=0; y=5;
                break;
            case R.id.day7:
                x=0; y=6;
                break;
            case R.id.day8:
                x=1; y=0;
                break;
            case R.id.day9:
                x=1; y=1;
                break;
            case R.id.day10:
                x=1; y=2;
                break;
            case R.id.day11:
                x=1; y=3;
                break;
            case R.id.day12:
                x=1; y=4;
                break;
            case R.id.day13:
                x=1; y=5;
                break;
            case R.id.day14:
                x=1; y=6;
                break;
            case R.id.day15:
                x=2; y=0;
                break;
            case R.id.day16:
                x=2; y=1;
                break;
            case R.id.day17:
                x=2; y=2;
                break;
            case R.id.day18:
                x=2; y=3;
                break;
            case R.id.day19:
                x=2; y=4;
                break;
            case R.id.day20:
                x=2; y=5;
                break;
            case R.id.day21:
                x=2; y=6;
                break;
            case R.id.day22:
                x=3; y=0;
                break;
            case R.id.day23:
                x=3; y=1;
                break;
            case R.id.day24:
                x=3; y=2;
                break;
            case R.id.day25:
                x=3; y=3;
                break;
            case R.id.day26:
                x=3; y=4;
                break;
            case R.id.day27:
                x=3; y=5;
                break;
            case R.id.day28:
                x=3; y=6;
                break;
            case R.id.day29:
                x=4; y=0;
                break;
            case R.id.day30:
                x=4; y=1;
                break;
            case R.id.day31:
                x=4; y=2;
                break;
            case R.id.day32:
                x=4; y=3;
                break;
            case R.id.day33:
                x=4; y=4;
                break;
            case R.id.day34:
                x=4; y=5;
                break;
            case R.id.day35:
                x=4; y=6;
                break;
            case R.id.day36:
                x=5; y=0;
                break;
            case R.id.day37:
                x=5; y=1;
                break;
            case R.id.day38:
                x=5; y=2;
                break;
            case R.id.day39:
                x=5; y=3;
                break;
            case R.id.day40:
                x=5; y=4;
                break;
            case R.id.day41:
                x=5; y=5;
                break;
            case R.id.day42:
                x=5; y=6;
                break;
        }

        return new GridCordinates(x, y);
    }
}
