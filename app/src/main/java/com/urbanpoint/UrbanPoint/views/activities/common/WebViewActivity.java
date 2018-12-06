package com.urbanpoint.UrbanPoint.views.activities.common;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by aparnasalve on 4/3/16.
 */
public class WebViewActivity extends Activity {
    WebView mWebView;
    private ImageView mBackButton;
    private TextView mDoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        String stringExtra = getIntent().getStringExtra(Constants.Request.PAGE);
        String actionBarHeader = getIntent().getStringExtra(Constants.DEFAULT_VALUES.ACTION_BAR_HEADER);

        if (stringExtra != null) {
            mWebView = (WebView) findViewById(R.id.webview);
            mWebView.getSettings().setJavaScriptEnabled(true);

            mWebView.loadUrl("file:///android_asset/" + stringExtra);
            mWebView.setBackgroundColor(Color.TRANSPARENT);
        }
        if (actionBarHeader != null) {
            setActionBar(actionBarHeader, true);
            if (getString(R.string.privacy_and_terms).equalsIgnoreCase(actionBarHeader)) {
                MyApplication.getInstance().trackScreenView(getResources().getString(R.string.privacy_and_terms));
            }

        } else {
            setActionBar(getString(R.string.app_name), true);
        }


    }


    public void setActionBar(String title, boolean showNavButton) {
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getLayoutInflater().inflate(R.layout.action_bar_done, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);

        mBackButton = (ImageView) customView.findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mSlideButton.toggle(true);
                finish();
            }
        });


        mDoneButton = (TextView) customView.findViewById(R.id.doneButton);
        mDoneButton.setVisibility(View.GONE);
//        mDoneButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        title1.setText(title);
        getActionBar().setCustomView(customView);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_down, 0);
    }
}
