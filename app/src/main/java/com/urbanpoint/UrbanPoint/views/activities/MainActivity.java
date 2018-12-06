package com.urbanpoint.UrbanPoint.views.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.fragments.appLogin.LoginFragment;
import com.urbanpoint.UrbanPoint.views.fragments.main.GetStartedFragment;
import com.urbanpoint.UrbanPoint.R;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;


/**
 * Created by Anurag Sethi
 * The activity is used for handling the login screen actions
 */
public class MainActivity extends BaseActivity {


    //private VideoView mAppIntroVideo;
    private ScalableVideoView mAppIntroVideo;

    private FragmentManager mSupportFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    String stringExtra;

    public MainActivity() {
        super(R.string.app_name_with_space);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        stringExtra = getIntent().getStringExtra(Constants.DEFAULT_VALUES.LOGOUT);
        initData();

        startVideo();

        getActionBar().hide();

        getSlidingMenu().setTouchModeAbove(
                SlidingMenu.TOUCHMODE_NONE);

    }

    /**
     * Default method of activity life cycle to handle the actions required once the activity starts
     * checks if the network is available or not
     *
     * @return none
     */

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        FlurryAgent.onStartSession(this);
/*
        if (!isNetworkAvailable(this)) {
            utilObj.showAlertDialog(this, this.getResources().getString(R.string.network_service_message_title), this.getResources().getString(R.string.network_service_message), this.getResources().getString(R.string.Ok), null, Constants.ButtonTags.TAG_NETWORK_SERVICE_ENABLE, 0);
        }*/

    }

    /**
     * Default activity life cycle method
     */
    @Override
    public void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  startVideo();
    }

    /**
     * The method to handle the data when the orientation is changed
     *
     * @param outState contains Bundle data
     */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //--- USED for FACEBOOK
        // Session session = Session.getActiveSession();
        // Session.saveSession(session, outState);

    }

    /**
     * Initializes the objects
     *
     * @return none
     */
    public void initData() {

        //-- SET device ID : START
        String tokenId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        AppPreference.setSetting(this, Constants.Request.DEVICE_ID, tokenId);
        //-- SET device ID : END

        // mAppIntroVideo = (VideoView) findViewById(R.id.appIntroVideoView);
        mAppIntroVideo = (ScalableVideoView) findViewById(R.id.appIntroVideoView);


        mSupportFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mSupportFragmentManager.beginTransaction();

        if (stringExtra != null) {
            mFragmentTransaction.add(R.id.containerIntroFragments, new LoginFragment());
        } else {
            mFragmentTransaction.add(R.id.containerIntroFragments, new GetStartedFragment());
        }

        mFragmentTransaction.commit();


     /*   passwordObj = (EditText) findViewById(R.id.password);
        btnLoginObj = (Button) findViewById(R.id.btnSignIn);
        btnSignUpObj = (Button) findViewById(R.id.btnSignup);
        textViewObj = (TextView) findViewById(R.id.errorMessage);
        utilObj = new Utility(this);
        userObj = new InputUser();
        loginManagerObj = new LoginManager(this, this);
        fb_login_llObj = (LinearLayout) findViewById(R.id.fb_login_ll);
*/
    }

    private void startVideo() {
        // Load and start the movie
        // Uri video1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro_video);

      /*  mAppIntroVideo.setVideoURI(video1);
        mAppIntroVideo.start();
        mAppIntroVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
*/

        try {
            //   mAppIntroVideo.setRawData(R.raw.intro_video);
            mAppIntroVideo.setRawData(R.raw.video);
            mAppIntroVideo.setVolume(0, 0);
            mAppIntroVideo.setLooping(true);
            mAppIntroVideo.prepare(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mAppIntroVideo.start();
                }
            });
        } catch (IOException ioe) {
            ioe.printStackTrace();
            //ignore
        }
    }


    /**
     * The method handles the result from the Facebook
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

    }

    /**
     * The interface method implemented in the java files
     *
     * @param response is the response received from the Facebook
     */
   /* @Override
    public void onFacebookTaskCompleted(Response response) {
        //write your code here
        Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
    }*/


}
