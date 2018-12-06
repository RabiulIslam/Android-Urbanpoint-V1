package com.urbanpoint.UrbanPoint.views.fragments.drawerItems;


import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.adapters.profileAdapter.NationalityListAdapter;
import com.urbanpoint.UrbanPoint.adapters.profileAdapter.SpinnerAdapter;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.Profile_WebHit_Get_BriteVerifyEmail;
import com.urbanpoint.UrbanPoint.managers.drawerItems.Profile_WebHit_POST_UpdateProfile;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.RoundedImageView;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, ServiceRedirection {


    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private ImageView mSlideButton;
    private Utility utilObj;
    private RoundedImageView rmvImg;
    private Spinner spnGender, spnNetwork;
    private SpinnerAdapter spinnerAdapter;
    private List<String> lstNationality;
    private List<String> lstGender, lstNetwork;
    private EditText edtEmail, edtNetwork;
    private TextView txvNationality, txvName, txvPercentage, txvOldPin, txvGender, txvNetwork;
    private ListView lsvNationality;
    private NationalityListAdapter nationalityListAdapter;
    private ScrollView scrollView;
    private ImageView imvNationltiyFlag, imvFlagInRounded, imvCross;
    private RelativeLayout rlChangePin, rlProgressBar;
    private LinearLayout llListContainer, llProfileContainer;
    private Button btnNationalitySave, btnSaveProfile;
    private int mSelectedPosition;
    private boolean isValidEmail;
    String[] strFlagsNames;
    Drawable drawable;
    int completedFields = 0;
    int completedPercentage = 0;

    //    ArrayList<String> listImages;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initialize();
        txvName.setText(AppInstance.profileData.getName());
        edtEmail.setText(AppInstance.profileData.getEmail());
        edtEmail.setSelection(edtEmail.length());
        txvOldPin.setText(AppPreference.getSetting(mContext, Constants.Request.PINCODE, ""));
        txvPercentage.setText(checkProfileCompletion() + " %");


        // checking where user nationality is saved or not
//        AppPreference.setSetting(mContext,"key_country_list","");
//        Log.d("dfasfsdfsad", "onCreateView length: "+AppPreference.getSetting(mContext,"key_country_list","").length());
        if (AppPreference.getSetting(mContext, "key_country_list", "").length() > 0) {
            llProfileContainer.setVisibility(View.VISIBLE);
            llListContainer.setVisibility(View.GONE);
            Log.d("dfasfsdfsad", "onCreateView length1: " + AppPreference.getSetting(mContext, "key_country_list", "").length());
            Log.d("dfasfsdfsad", "onCreateView length2: " + completedPercentage);

            if (AppInstance.profileData.getNationality().length() > 0) {
                txvNationality.setText(AppInstance.profileData.getNationality() + "");
                Log.d("dfasfsdfsad", "onCreateView length2: " + AppPreference.getSetting(mContext, "key_country_list", "").length());
                checkProfileCompletion();
                if (completedPercentage == 100) {
                    btnSaveProfile.setVisibility(View.GONE);
                    txvPercentage.setVisibility(View.GONE);
                    imvNationltiyFlag.setVisibility(View.GONE);
                }
                try {
//                Log.d("IMGESLST", "list value: " + AppInstance.profileData.getNationality() + ".png");
                    InputStream inputstream = mContext.getAssets().open("flags/" + AppInstance.profileData.getNationality() + ".png");
                    drawable = Drawable.createFromStream(inputstream, null);
                    imvFlagInRounded.setImageDrawable(drawable);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                txvPercentage.setVisibility(View.VISIBLE);
            }


        } else {
            btnNationalitySave.setVisibility(View.GONE);
            Log.d("dfasfsdfsad", "onCreateView length3: " + AppPreference.getSetting(mContext, "key_country_list", "").length());
            imvCross.setVisibility(View.VISIBLE);
            llListContainer.setVisibility(View.VISIBLE);
            llProfileContainer.setVisibility(View.GONE);
        }

        if (AppInstance.profileData.getIsOoredooCustomer().equalsIgnoreCase("1")) {
//            spnNetwork.setSelection(1);
            txvNetwork.setText(Constants.DEFAULT_VALUES.OOREDOO);
            Log.d("sdas", "PROFILE: Ooredoo");
        } else if (AppInstance.profileData.getIsVodafoneCustomer().equalsIgnoreCase("1")) {
//            spnNetwork.setSelection(0);
            Log.d("sdas", "PROFILE: Vodafone");
            txvNetwork.setText(Constants.DEFAULT_VALUES.VODAFONE);
        }
        if (AppInstance.profileData.getGender().equalsIgnoreCase("1")) {
//            spnGender.setSelection(0);
            txvGender.setText("Male");
            Log.d("sdas", "PROFILE: Male ");
        } else if (AppInstance.profileData.getGender().equalsIgnoreCase("2")) {
//            spnGender.setSelection(1);
            txvGender.setText("Female");
            Log.d("sdas", "PROFILE: Female");
        }


        lsvNationality.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        lsvNationality.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                imvNationltiyFlag.setVisibility(View.VISIBLE);
//                lsvNationality.setVisibility(View.GONE);
//                utilObj.keyboardClose(mContext, view);
                mSelectedPosition = position;
//                nationalityListAdapter = new NationalityListAdapter(mContext, mSelectedPosition, lstNationality);
//                lsvNationality.setAdapter(nationalityListAdapter);
                nationalityListAdapter.setPosition(position);
                nationalityListAdapter.notifyDataSetInvalidated();

                try {
                    Log.d("IMGESLST", "list value: " + lstNationality.get(position) + ".png");
                    InputStream inputstream = mContext.getAssets().open("flags/" + lstNationality.get(position) + ".png");
                    drawable = Drawable.createFromStream(inputstream, null);
                    imvFlagInRounded.setImageDrawable(drawable);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                btnNationalitySave.setVisibility(View.VISIBLE);
            }
        });

        edtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.d("asdfwe", "onEditorAction: ");
                    rlProgressBar.setVisibility(View.VISIBLE);
//                    doValidateEmail(edtEmail.getText().toString());
                    hideMyKeyboard();
                }
                return true;
            }
        });
        return view;
    }

    private void hideMyKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtEmail.getWindowToken(), 0);
    }

    private void initialize() {
        MyApplication.getInstance().trackScreenView(getString(R.string.contact_us));
        lstGender = new ArrayList<>();
        lstNetwork = new ArrayList<>();
        lstNationality = new ArrayList<>();
        utilObj = new Utility(getActivity());
        isValidEmail = false;
        mSelectedPosition = -1;
        setActionBar("Hi, " + AppInstance.profileData.getName(), true);
        bindViews();
    }

    private void bindViews() {
        rlProgressBar = (RelativeLayout) mRootView.findViewById(R.id.frg_profile_rl_progrssbar);
        llListContainer = (LinearLayout) mRootView.findViewById(R.id.frg_profile_ll_list_container);
        llProfileContainer = (LinearLayout) mRootView.findViewById(R.id.frg_profile_ll);
        imvCross = (ImageView) mRootView.findViewById(R.id.frg_profile_imv_close);
        imvCross.setOnClickListener(this);
        txvPercentage = (TextView) mRootView.findViewById(R.id.frg_profile_txv_profile_percentage);
        txvOldPin = (TextView) mRootView.findViewById(R.id.frg_profile_txv_old_pin);
        txvGender = (TextView) mRootView.findViewById(R.id.frg_my_profile_txv_gender);
        txvNetwork = (TextView) mRootView.findViewById(R.id.frg_profile_txv_network);
        btnSaveProfile = (Button) mRootView.findViewById(R.id.profileSubmit);
        btnSaveProfile.setOnClickListener(this);

        btnNationalitySave = (Button) mRootView.findViewById(R.id.nationalitySave);
        btnNationalitySave.setOnClickListener(this);

        imvFlagInRounded = (ImageView) mRootView.findViewById(R.id.frg_profile_imv_flag);
        imvFlagInRounded.setImageResource(R.mipmap.nationality_0);
        rmvImg = (RoundedImageView) mRootView.findViewById(R.id.frg_profile_round__imv_pic);
        rmvImg.setImageResource(R.mipmap.nationality_2);
        txvName = (TextView) mRootView.findViewById(R.id.frg_profile_edt_name);
        edtEmail = (EditText) mRootView.findViewById(R.id.frg_profile_edt_email);
//        edtNetwork = (EditText) mRootView.findViewById(R.id.frg_profile_edt_network);
        txvNationality = (TextView) mRootView.findViewById(R.id.frg_profile_edt_nationality);
        txvNationality.setOnClickListener(this);
        spnGender = (Spinner) mRootView.findViewById(R.id.frg_my_profile_spinner_gender);
        spnNetwork = (Spinner) mRootView.findViewById(R.id.frg_my_profile_spinner_network);
        lsvNationality = (ListView) mRootView.findViewById(R.id.frg_profile_list_view_nationality_2);
//        scrollView = (ScrollView) mRootView.findViewById(R.id.frg_profile_scroll_view);
        imvNationltiyFlag = (ImageView) mRootView.findViewById(R.id.frg_profile_imv_nationality);
        rlChangePin = (RelativeLayout) mRootView.findViewById(R.id.frg_profile_rl_change_pin);
        rlChangePin.setOnClickListener(this);

        lstGender.add("Male");
        lstGender.add("Female");
        spinnerAdapter = new SpinnerAdapter(getContext(), lstGender);
        spnGender.setAdapter(spinnerAdapter);

        lstNetwork.add("Vodafone");
        lstNetwork.add("Ooredoo");
        spinnerAdapter = new SpinnerAdapter(getContext(), lstNetwork);
        spnNetwork.setAdapter(spinnerAdapter);

        lstNationality = Arrays.asList(Constants.arrFlags);
        Log.d("IMGESLST", "Unsorted: " + lstNationality);
//        Collections.sort(lstNationality);
//        Log.d("IMGESLST", "  sorted: " + lstNationality);

        nationalityListAdapter = new NationalityListAdapter(mContext, mSelectedPosition, lstNationality);
        lsvNationality.setAdapter(nationalityListAdapter);


//        try {
////            strFlagsNames = mContext.getAssets().list("images");
////            ArrayList<String> listImages = new ArrayList<String>(Arrays.asList(strFlagsNames));
//            InputStream inputstream = mContext.getAssets().open("flags/" + lstNationality.get(mSelectedPosition));
//            drawable = Drawable.createFromStream(inputstream, null);
//            imvFlagInRounded.setImageDrawable(drawable);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    public int checkProfileCompletion() {
        completedFields = 4;
        completedPercentage = 0;

//        if (AppInstance.profileData.getName().length() > 0) {
//            completedFields++;
//        }
//        if (AppInstance.profileData.getEmail().length() > 0) {
//            completedFields++;
//        }
//        if (AppInstance.profileData.getGender().length() > 0) {
//            completedFields++;
//        }
//        if (AppInstance.profileData.getIsOoredooCustomer().equalsIgnoreCase("1")) {
//            completedFields++;
//        } else if (AppInstance.profileData.getIsVodafoneCustomer().equalsIgnoreCase("1")) {
//            completedFields++;
//        }

        if (AppInstance.profileData.getNationality().length() > 0) {
            completedFields++;
        }

        completedPercentage = (completedFields) * 20;
        return completedPercentage;
    }

    public void setActionBar(String title, boolean showNavButton) {
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getActivity().getLayoutInflater().inflate(R.layout.action_bar_how_to_use, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);

        mSlideButton = (ImageView) customView.findViewById(R.id.slidingMenuHowToUseButton);
        mSlideButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //mSlideButton.toggle(true);
                View view = getActivity().getCurrentFocus();
                utilObj.keyboardClose(mContext, view);
                ((BaseActivity) getActivity()).getSlidingMenu().toggle();
            }
        });


        title1.setText(title);

        if (showNavButton) {
            mSlideButton.setVisibility(View.VISIBLE);

            ((BaseActivity) getActivity()).getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            mSlideButton.setVisibility(View.GONE);

            ((BaseActivity) getActivity()).getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_NONE);
        }


        getActivity().getActionBar().setCustomView(customView);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.frg_profile_rl_change_pin:
                navToChangePinFragment();
                break;

            case R.id.frg_profile_imv_close:
                llProfileContainer.setVisibility(View.VISIBLE);
                llListContainer.setVisibility(View.GONE);
                imvCross.setVisibility(View.GONE);
                mSelectedPosition = -1;
                nationalityListAdapter.setPosition(mSelectedPosition);
                nationalityListAdapter.notifyDataSetInvalidated();

                try {
//            strFlagsNames = mContext.getAssets().list("images");
//            ArrayList<String> listImages = new ArrayList<String>(Arrays.asList(strFlagsNames));
                    if (txvNationality.getText().length() > 0) {
                        InputStream inputstream = mContext.getAssets().open("flags/" + txvNationality.getText() + ".png");
                        drawable = Drawable.createFromStream(inputstream, null);
                        imvFlagInRounded.setImageDrawable(drawable);
                    } else {
                        imvFlagInRounded.setImageResource(R.mipmap.nationality_0);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;

            case R.id.nationalitySave:
                llProfileContainer.setVisibility(View.VISIBLE);
                llListContainer.setVisibility(View.GONE);
                imvCross.setVisibility(View.GONE);

                if (mSelectedPosition > -1) {
//                    AppInstance.profileData.setNationality(lstNationality.get(mSelectedPosition));
                    txvNationality.setText(lstNationality.get(mSelectedPosition));
//                    txvPercentage.setVisibility(View.GONE);
                    imvNationltiyFlag.setVisibility(View.VISIBLE);
                    try {
                        Log.d("IMGESLST", "list value: " + lstNationality.get(mSelectedPosition) + ".png");
                        InputStream inputstream = mContext.getAssets().open("flags/" + lstNationality.get(mSelectedPosition) + ".png");
                        drawable = Drawable.createFromStream(inputstream, null);
                        imvNationltiyFlag.setImageDrawable(drawable);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
                nationalityListAdapter.setPosition(-1);
                nationalityListAdapter.notifyDataSetInvalidated();

                break;
            case R.id.frg_profile_edt_nationality:
                if (AppInstance.profileData.getNationality().length() < 1) {
                    btnNationalitySave.setVisibility(View.GONE);
                    imvCross.setVisibility(View.VISIBLE);
                    llListContainer.setVisibility(View.VISIBLE);
                    llProfileContainer.setVisibility(View.GONE);
                }
                break;
            case R.id.profileSubmit:

//                if (isValidEmail) {
                if (NetworkUtils.isConnected(mContext)) {
                    utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);

//                    doValidateEmail(edtEmail.getText().toString());

                    int gender, voda, ooredoo;
                    if (txvGender.getText().toString().equals("Male")) {
                        gender = 1;
                    } else {
                        gender = 2;
                    }

                    if (AppInstance.profileData.getIsOoredooCustomer().equalsIgnoreCase("1")) {
                        voda = 0;
                        ooredoo = 1;
                    } else if (AppInstance.profileData.getIsVodafoneCustomer().equalsIgnoreCase("1")) {
                        voda = 1;
                        ooredoo = 0;
                    } else {
                        voda = 0;
                        ooredoo = 0;
                    }
                    doSaveUserProfile(txvName.getText().toString(), edtEmail.getText().toString(),
                            voda,
                            ooredoo, gender, txvNationality.getText().toString());
//                    isValidEmail = true;

                } else {
                    utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                }
//                }
//                else {
//                    utilObj.showToast(mContext, getString(R.string.invalid_email), Toast.LENGTH_LONG);
//
//                }
                break;
        }
    }

    private void navToChangePinFragment() {
        boolean addToBackStack = false;
        Fragment frg = new ChangePinFragment();
        DashboardActivity mainActivity = (DashboardActivity) getActivity();
        mainActivity.switchContent(frg, null, addToBackStack, null);

    }

    private void doSaveUserProfile(String _name, String _email, int _voda, int _ooredoo, int _gender, String _nationality) {
        if (NetworkUtils.isConnected(mContext)) {
//            utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
//            mUpdateUserProfileManager.doUpdateUserProfile(_name, _email, _nationality);
            Profile_WebHit_POST_UpdateProfile profile_webHit_post_updateProfile = new Profile_WebHit_POST_UpdateProfile();
            profile_webHit_post_updateProfile.updateProfile(getActivity(), this, _name, _email, _voda, _ooredoo, _gender, _nationality);
        } else {
            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
        }
    }

    private void doValidateEmail(String _email) {
        if (NetworkUtils.isConnected(mContext)) {
            //            mUpdateUserProfileManager.doUpdateUserProfile(_name, _email, _nationality);
            Profile_WebHit_Get_BriteVerifyEmail profile_webHit_get_BriteVerify_email = new Profile_WebHit_Get_BriteVerifyEmail();
            profile_webHit_get_BriteVerify_email.checkValidation(getActivity(), this, _email);
        } else {
            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
        }
    }


    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();
        if (taskID == Constants.TaskID.DO_UPDATE_PROFILE) {


        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.showToast(mContext, errorMessage, Toast.LENGTH_LONG);

    }

    public void showResult(boolean b, String message) {
        utilObj.stopiOSLoader();
        if (b) {
            AppInstance.profileData.setName(txvName.getText().toString());
            AppInstance.profileData.setNationality(AppPreference.getSetting(getContext(), Constants.Request.NATIONALITY, ""));
            setActionBar("Hi, " + AppInstance.profileData.getName(), true);
            if (AppPreference.getSetting(getContext(), Constants.Request.NATIONALITY, "").length() > 0) {
                checkProfileCompletion();
                if (completedPercentage == 100) {
                    btnSaveProfile.setVisibility(View.GONE);
                    imvNationltiyFlag.setVisibility(View.GONE);
                    txvPercentage.setVisibility(View.GONE);
                }
            }
            utilObj.showCustomAlertDialog(mActivity, null, "Profile Updated", null, null, false, null);
        } else {
            utilObj.showCustomAlertDialog(mActivity, null, message, null, null, false, null);
        }
    }

    public void showResultEmailValidation(boolean b, String s) {
//        rlProgressBar.setVisibility(View.GONE);
        if (b) {
            int gender, voda, ooredoo;
            if (txvGender.getText().toString().equals("Male")) {
                gender = 1;
            } else {
                gender = 2;
            }

            if (AppInstance.profileData.getIsOoredooCustomer().equalsIgnoreCase("1")) {
                voda = 0;
                ooredoo = 1;
            } else if (AppInstance.profileData.getIsVodafoneCustomer().equalsIgnoreCase("1")) {
                voda = 1;
                ooredoo = 0;
            } else {
                voda = 0;
                ooredoo = 0;
            }
            doSaveUserProfile(txvName.getText().toString(), edtEmail.getText().toString(),
                    voda,
                    ooredoo, gender, txvNationality.getText().toString());
            isValidEmail = true;
        } else {
            utilObj.stopiOSLoader();
            utilObj.showCustomAlertDialog(mActivity, null, getResources().getString(R.string.invalid_email), null, null, false, null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        AppPreference.setSetting(mContext, "key_country_list", "showed");

    }
}
