package com.gp.app.professionalpa.views.listeners;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gp.app.professionalpa.R;

public class NotesActionMode implements ActionMode.Callback 
{
    // Called when the action mode is created; startActionMode() was called
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) 
    {
        MenuInflater inflater = mode.getMenuInflater();
    
        inflater.inflate(R.menu.contextual_menu, menu);
        
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu)
    {
        return true; 
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_discard_notes:
                mode.finish(); // Action picked, so close the CAB
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) 
    {
    }
}