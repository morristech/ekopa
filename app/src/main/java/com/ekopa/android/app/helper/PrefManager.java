package com.ekopa.android.app.helper;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ekopa.android.app.activity.LoginActivity;

import java.util.HashMap;

public class PrefManager {
    // User token (make variable public to access from outside)
    public static final String KEY_USERTOKEN = "userToken";
    // User ID (make variable public to access from outside)
    public static final String KEY_USERID = "userID";
    // User's Name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    // User's image (make variable public to access from outside)
    public static final String KEY_IMAGEURL = "ImageURL";
    // User's credit limit (make variable public to access from outside)
    public static final String KEY_CREDIT_LIMIT = "creditLimit";
    // User's referral code (make variable public to access from outside)
    public static final String KEY_REFERRAL_CODE = "referralCode";
    // Sharedpref file name
    private static final String PREF_NAME = "XYZPref";
    //phone
    public static final String KEY_PHONE = "phoneNumber";
    //id number
    public static final String KEY_ID = "idNumber";
    //dob
    public static final String KEY_DOB = "dateOfBirth";

    // User Login status (make variable public to access from outside)
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    // common message sender IDs (make variable public to access from outside)
    public static final String[] MESSAGE_SENDER_ID = new String[]{"MPESA"};
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */

    public void createLoginSession(String userRefId, String name, String imgURL,
                                   String phone, String idNumber, String dob, String creditLimit) {


        // Storing userID in pref
        editor.putString(KEY_USERID, userRefId);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing imgURL in pref
        editor.putString(KEY_IMAGEURL, imgURL);
        //phone
        editor.putString(KEY_PHONE, phone);
        //id number
        editor.putString(KEY_ID, idNumber);
        //date of birth
        editor.putString(KEY_DOB, dob);

        // Storing credit limit in pref
        editor.putString(KEY_CREDIT_LIMIT, creditLimit);

        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        // username
        user.put(KEY_USERTOKEN, pref.getString(KEY_USERTOKEN, null));

        // user id
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));

        // name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // img url
        user.put(KEY_IMAGEURL, pref.getString(KEY_IMAGEURL, null));


        // credit limit
        user.put(KEY_CREDIT_LIMIT, pref.getString(KEY_CREDIT_LIMIT, null));

        // referral code
        user.put(KEY_REFERRAL_CODE, pref.getString(KEY_REFERRAL_CODE, null));

        //phone
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));
        //ID number
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        //dob
        user.put(KEY_DOB, pref.getString(KEY_DOB, null));

        // return user
        return user;
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Starting Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Clear session details
     * */
    public void setIsLogin() {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // commit changes
        editor.commit();
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick save to and read from shared preference
     * **/
    public void saveSharedSetting(String settingName, String settingValue) {
        editor.putString(settingName, settingValue);
        editor.apply();
    }
    public String readSharedSetting(String settingName, String defaultValue) {
        return pref.getString(settingName, defaultValue);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

}
