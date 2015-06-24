package com.gp.app.professionalpa.activity.state;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import com.gp.app.professionalpa.layout.manager.NotesLayoutManagerActivity;

/**
 * TODO to be removed if not needed.
 */
public class ActivityStateMonitor extends Application implements ActivityLifecycleCallbacks
{
	private static boolean isInterestingActivityVisible;

    @Override
    public void onCreate() 
    {
        super.onCreate();

        registerActivityLifecycleCallbacks(this);
    }

    public boolean isInterestingActivityVisible() 
    {
        return isInterestingActivityVisible;
    }

    @Override
    public void onActivityResumed(Activity activity) 
    {
        if (activity instanceof NotesLayoutManagerActivity)
        {
             isInterestingActivityVisible = true;
        }
    }

    @Override
    public void onActivityStopped(Activity activity) 
    {
        if (activity instanceof NotesLayoutManagerActivity) 
        {
             isInterestingActivityVisible = false;
        }
    }

	@Override
	public void onActivityCreated(Activity arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityDestroyed(Activity arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityPaused(Activity arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivitySaveInstanceState(Activity arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityStarted(Activity arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static boolean isNotesLayoutManagerActivityResumed()
	{
		return isInterestingActivityVisible;
	}
}
