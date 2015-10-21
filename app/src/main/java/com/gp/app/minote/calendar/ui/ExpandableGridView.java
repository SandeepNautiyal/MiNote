package com.gp.app.minote.calendar.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * Created by dell on 9/9/2015.
 */
public class ExpandableGridView extends GridView
{

    boolean expanded = false;

    public ExpandableGridView(Context context)
    {
        super(context);
    }

    public ExpandableGridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ExpandableGridView(Context context, AttributeSet attrs,
                              int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public boolean isExpanded()
    {
        return expanded;
    }

//    @Override
//    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
//    {
//        // HACK! TAKE THAT ANDROID!
//        int heightSpec;
//
//        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
//            // The great Android "hackatlon", the love, the magic.
//            // The two leftmost bits in the height measure spec have
//            // a special meaning, hence we can't use them to describe height.
//            heightSpec = MeasureSpec.makeMeasureSpec(
//                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//        }
//        else {
//            // Any other height should be respected as is.
//            heightSpec = heightMeasureSpec;
//        }
//
//        super.onMeasure(widthMeasureSpec, heightSpec);
//    }

    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded())
        {
            // Calculate entire height by providing a very large height hint.
            // View.MEASURED_SIZE_MASK represents the largest height possible.
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setListViewHeightBasedOnItems()
    {
        ListAdapter listAdapter = this.getAdapter();

        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            System.out.println("setListViewHeightBasedOnItems ->  numberOfItems="+numberOfItems);

            int totalItemsHeight = 0;

            for (int itemPos = 0; itemPos < numberOfItems; itemPos++)
            {
                View item = listAdapter.getView(itemPos, null, this);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
                System.out.println("setListViewHeightBasedOnItems ->  itemPos="+itemPos+" totalItemsHeight="+totalItemsHeight);
            }

            // Get total height of all item dividers.
            int totalDividersHeight = 0; //1 * (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = this.getLayoutParams();

            //TODO to be checked for NPE, it which case NPE can occur.
            if(params != null)
            {
                params.height = totalItemsHeight + totalDividersHeight;

                System.out.println("setListViewHeightBasedOnItems ->  params.height="+params.height);

                this.setLayoutParams(params);
                this.requestLayout();
            }
        }
    }
}
