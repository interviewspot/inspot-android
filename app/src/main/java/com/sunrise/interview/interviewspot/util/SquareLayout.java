package com.sunrise.interview.interviewspot.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by donnv on 7/13/2015.
 */
public class SquareLayout extends LinearLayout {


    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }
}
