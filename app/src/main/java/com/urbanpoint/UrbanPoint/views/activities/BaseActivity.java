package com.urbanpoint.UrbanPoint.views.activities;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.urbanpoint.UrbanPoint.R;


public class BaseActivity extends SlidingFragmentActivity {
    private final String TAG = " >>> BaseActivity";

    private int mTitleRes;
    TextView textViewTitle;

    public BaseActivity(int titleRes) {
        mTitleRes = titleRes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        initActionBar();

        initSlidingMenu();
    }

    private void initActionBar() {

        ActionBar actionBar = getActionBar();

        if (actionBar == null) {
            return;
        }


        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        actionBar.setCustomView(R.layout.action_bar_layout);
        View customView = actionBar.getCustomView().findViewById(
                R.id.slidingMenuButton);

        ImageView slideButton = (ImageView) customView
                .findViewById(R.id.slidingMenuButton);
        slideButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getSlidingMenu().toggle(true);
            }
        });

        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initSlidingMenu() {

        setTitle(mTitleRes);

        setBehindContentView(R.layout.menu_drawer_frame);

        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setShadowWidthRes(R.dimen.sliding_menu_shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        slidingMenu.setMode(SlidingMenu.LEFT);

        setSlidingActionBarEnabled(true);

        // getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();

                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setActionBar(String title, boolean showNavButton) {


        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getActionBar().setTitle(title);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        View customView = getLayoutInflater().inflate(
                R.layout.action_bar_layout, null);
        textViewTitle = (TextView) customView.findViewById(R.id.textViewTitle);
//        textViewTitle.setText(title);
        ImageView slideButton = (ImageView) customView
                .findViewById(R.id.slidingMenuButton);
        slideButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getSlidingMenu().toggle();
            }
        });
        if (showNavButton) {
            slideButton.setVisibility(View.VISIBLE);

            getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slideButton.setVisibility(View.GONE);

            getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_NONE);


        }
        getActionBar().setCustomView(customView);
    }

}

