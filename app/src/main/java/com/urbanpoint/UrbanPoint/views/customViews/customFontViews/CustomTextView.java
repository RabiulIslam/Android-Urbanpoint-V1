package com.urbanpoint.UrbanPoint.views.customViews.customFontViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

/**
 * Created by riteshpandhurkar on 3/3/16.
 */
public class CustomTextView extends TextView {

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface font = MyApplication.getInstance().getFont();
        if (font != null) {
      this.setTypeface(font);
        }
    }
}
