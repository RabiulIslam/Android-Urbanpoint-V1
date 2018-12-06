package com.urbanpoint.UrbanPoint.views.customViews.customFontViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

/**
 * Created by riteshpandhurkar on 3/3/16.
 */
public class ButtonView extends Button {
    public ButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface font = MyApplication.getInstance().getFont();
        if (font != null) {
          this.setTypeface(font);
        }
    }

}
