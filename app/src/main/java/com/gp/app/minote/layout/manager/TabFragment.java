package com.gp.app.minote.layout.manager;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.gp.app.minote.R;


/**
 * Created by dell on 10/16/2015.
 */
public class TabFragment extends Fragment
{
    public static final String ARG_OBJECT = "object";

    LinearLayout layout = null;

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        layout = (LinearLayout)inflater.inflate(R.layout.tab_fragment_layout, null);

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }
}
