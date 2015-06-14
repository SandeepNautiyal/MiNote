package com.gp.app.professionalpa.interfaces;

public interface XMLEntity 
{
    byte INSERT_STATE = 0;
    
    byte UPDATE_STATE = 1;
    
    byte DELETE_STATE = 2;
    
    byte READ_STATE = 3;
    
    byte getState();
}
