package com.gp.app.minote.calendar.ui;

/**
 * Created by dell on 9/25/2015.
 */
class GridCordinates
{
    byte x;

    byte y;

    GridCordinates(byte x, byte y)
    {
        this.x = x;

        this.y  = y;
    }

    byte getX()
    {
        return x;
    }

    byte getY()
    {
        return y;
    }
}
