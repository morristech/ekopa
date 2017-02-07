package com.ekopa.android.app;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ekopa.android.app.activity.ApplyForLoanActivity;
import com.ekopa.android.app.activity.FeedbackActivity;
import com.ekopa.android.app.activity.HelpFAQActivity;
import com.ekopa.android.app.activity.MyLoans;
import com.ekopa.android.app.activity.UserProfile;
import com.ekopa.android.app.api.ApiClient;
import com.ekopa.android.app.api.ApiInterface;
import com.ekopa.android.app.fragment.HomeActiveLoanFragment;
import com.ekopa.android.app.fragment.HomeFirstTimeFragment;
import com.ekopa.android.app.fragment.HomePaidLoanFragment;
import com.ekopa.android.app.fragment.HomePendingLoanFragment;
import com.ekopa.android.app.fragment.HomeRejectedLoanFragment;
import com.ekopa.android.app.fragment.LoadingFragment;
import com.ekopa.android.app.fragment.NoInternetFragment;
import com.ekopa.android.app.geolocation.LocationService;
import com.ekopa.android.app.helper.PrefManager;
import com.ekopa.android.app.invite.ContactsPickerActivity;
import com.ekopa.android.app.model.CustomerCallLog;
import com.ekopa.android.app.model.Data;
import com.ekopa.android.app.model.Message;
import com.ekopa.android.app.model.ResponseModel;
import com.facebook.appevents.AppEventsLogger;
import com.github.orangegangsters.lollipin.lib.PinCompatActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends PinCompatActivity implements
        HomeActiveLoanFragment.OnFragmentInteractionListener,
        HomeRejectedLoanFragment.OnFragmentInteractionListener,
        NoInternetFragment.OnRefreshButtonClick {
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final int TIME_INTERVAL = 2000; //Time in milliseconds passed betweem two back presses
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_drawer)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;
    private int mCurrentSelectedPosition;
    private boolean mFromSavedInstanceState;
    private PrefManager prefManager;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(getApplicationContext());

        AppEventsLogger.activateApp(this);
        prefManager = new PrefManager(this);
        prefManager.checkLogin();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initInstancesDrawer(savedInstanceState);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fragment, new HomeFirstTimeFragment());
        tx.commitAllowingStateLoss();


    }


    private void fetchCustomerStatus() {
        //Show loading fragment while we fetch customer status
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fragment, new LoadingFragment());
        tx.commit();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.getCustomerStatus(prefManager.getUserDetails().get(PrefManager.KEY_USERTOKEN));
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body()!=null){
                    onSuccessFetch(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.fragment, new NoInternetFragment());
                tx.commit();
                Toast.makeText(MainActivity.this,"Something went wrong please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onSuccessFetch(Data data) {
        //Check if customer is first time user
        if (data.getLoan_requests() == null || data.getLoans() == null ||(data.getLoan_requests().size() < 1 && data.getLoans().size() <1)){
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.fragment, new HomeFirstTimeFragment());
            tx.commit();

            return;
        }

        String loanRequestStatus = data.getLoan_requests().get(0).getStatus();
        String loanPaymentStatus = data.getLoans().get(0).getStatus();

        Log.e("Loans Request", "Request:"+loanRequestStatus);
        Log.e("Loans Payment", ""+loanPaymentStatus);

        //Check for pending loan approval
        if (loanRequestStatus.equalsIgnoreCase("pending")){
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.fragment, new HomePendingLoanFragment());
            tx.commit();

            return;
        }

        //check if loan has been approved or rejected
        if (loanRequestStatus.equalsIgnoreCase("approved")){

            //check if loan has been repaid fully
            if (loanPaymentStatus.equalsIgnoreCase("paid")){
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.fragment, new HomePaidLoanFragment());
                tx.commitAllowingStateLoss();
            }else{
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.fragment, new HomeActiveLoanFragment());
                tx.commitAllowingStateLoss();
            }

        }else if (loanRequestStatus.equalsIgnoreCase("rejected")){
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.fragment, new HomeRejectedLoanFragment());
            tx.commitAllowingStateLoss();
        }


    }

    private void initInstancesDrawer(Bundle savedInstanceState) {
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        mUserLearnedDrawer = Boolean.valueOf(prefManager.readSharedSetting(PREF_USER_LEARNED_DRAWER, "false"));

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        Menu menu;
        if (navigationView != null) {
            setupDrawerContent(navigationView);
            menu = navigationView.getMenu();
            menu.getItem(0).setChecked(true);
        }

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

        if (!mUserLearnedDrawer) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            mUserLearnedDrawer = true;
            prefManager.saveSharedSetting(PREF_USER_LEARNED_DRAWER, "true");
        }
    }

    private void selectDrawerItem(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.drawer_home:
                mCurrentSelectedPosition = 0;
                //fragmentClass = HomeFragment.class;
                break;

            case R.id.drawer_apply_loan:
                mCurrentSelectedPosition = 1;
                //Apply for a loan
                Intent applyLoan = new Intent(MainActivity.this, ApplyForLoanActivity.class);
                startActivity(applyLoan);
                break;

            case R.id.drawer_manage_account:
                mCurrentSelectedPosition = 2;

                //my account
                Intent myAccount = new Intent(MainActivity.this, UserProfile.class);
                startActivity(myAccount);
                break;

            case R.id.drawer_my_loans:
                mCurrentSelectedPosition = 3;
                //my loans
                Intent myLoans = new Intent(MainActivity.this, MyLoans.class);
                startActivity(myLoans);

                break;
            case R.id.drawer_invite_friends:
                mCurrentSelectedPosition = 4;
                //invite friends
                Intent inviteFriends = new Intent(MainActivity.this, ContactsPickerActivity.class);
                startActivity(inviteFriends);
                break;

            case R.id.drawer_feedback:
                mCurrentSelectedPosition = 1;
                //invite friends
                Intent feedBack = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(feedBack);
                break;

            case R.id.drawer_help:
                Intent help = new Intent(MainActivity.this, HelpFAQActivity.class);
                startActivity(help);
                break;

            default:
                //fragmentClass = HomeFragment.class;
                break;
        }

        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
//            case R.id.action_settings:
//                break;

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.action_logout:
                logoutUser();
                break;

            case R.id.action_firstTime:
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.fragment, new HomeFirstTimeFragment());
                tx.commitAllowingStateLoss();
                break;

            case R.id.action_pendingLoan:
                FragmentTransaction pend = getSupportFragmentManager().beginTransaction();
                pend.replace(R.id.fragment, new HomePendingLoanFragment());
                pend.commitAllowingStateLoss();
                break;

            case R.id.action_activeLoan:
                FragmentTransaction active = getSupportFragmentManager().beginTransaction();
                active.replace(R.id.fragment, new HomeActiveLoanFragment());
                active.commitAllowingStateLoss();
                break;

            case R.id.action_rejectedLoan:
                FragmentTransaction reject = getSupportFragmentManager().beginTransaction();
                reject.replace(R.id.fragment, new HomeRejectedLoanFragment());
                reject.commitAllowingStateLoss();
                break;

            case R.id.action_paidLoan:
                FragmentTransaction paid = getSupportFragmentManager().beginTransaction();
                paid.replace(R.id.fragment, new HomePaidLoanFragment());
                paid.commitAllowingStateLoss();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);
        Menu menu = navigationView.getMenu();
        menu.getItem(mCurrentSelectedPosition).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Snackbar.make(mDrawerLayout, "Press back again to exit", Snackbar.LENGTH_SHORT).show();
            mBackPressed = System.currentTimeMillis();
        }

    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void logoutUser() {
        prefManager.logoutUser();
    }

    private void uploadMpesaMessages() {
        String[] phoneNumber = PrefManager.MESSAGE_SENDER_ID;
        Cursor cursor1 = getContentResolver().query(Uri.parse("content://sms/inbox"),
                new String[]{"_id", "thread_id", "address", "person", "date", "body", "type"}, "address=?", phoneNumber, null);
        //StringBuffer msgData = new StringBuffer();
        Message[] message = new Message[cursor1 != null ? cursor1.getCount() : 0];
        int j = 0;
        if (cursor1 != null && cursor1.moveToFirst()) {
            do {
                message[j] = new Message();
                for (int i = 0; i < cursor1.getColumnCount(); i++) {
//                    msgData.append(" " + cursor1.getColumnName(idx) + ":" + cursor1.getString(idx));
//                    Log.e("MESSAGE "+idx, " " + cursor1.getColumnName(idx) + ":" + cursor1.getString(idx));
                    String column = cursor1.getColumnName(i);
                    String value = cursor1.getString(i);
                    if (column.equalsIgnoreCase("address")) {
                        message[j].setAddress(value);
                    } else if (column.equalsIgnoreCase("body")) {
                        message[j].setBody(value);
                    } else if (column.equalsIgnoreCase("type")) {
                        message[j].setMessage_type(Integer.parseInt(value));
                    }
                }
                j++;

            } while (cursor1.moveToNext() && j < message.length);
            if(message != null && message.length > 0) {
                message[j - 1].setAccess_token(prefManager.getUserDetails().get(PrefManager.KEY_USERTOKEN));
                //make API call
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<ResponseModel> call = apiService.messageSynchronization(message);
                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        Log.e("RESPONSE", " "+response.message());
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Log.e("FAIL", " "+t.getMessage());
                    }
                });
            }

        } else {
//            edtmessagebody.setText("no message from this contact"+phoneNumber);
            Log.e("NO SMS", "no message from this contact" + Arrays.toString(phoneNumber));
        }
        if (cursor1 != null) {
            cursor1.close();
        }
    }

    private void uploadAllCallLogs(ContentResolver cr) {
        // reading all data in descending order according to DATE
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Uri callUri = Uri.parse("content://call_log/calls");
        Cursor cur = cr.query(callUri, null, null, null, strOrder);
        CustomerCallLog[] callLog = new CustomerCallLog[cur != null ? cur.getCount() : 0];
        // loop through cursor
        int i = 0;
        while ((cur != null && cur.moveToNext()) && i < callLog.length) {
            String callNumber = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
            String callName = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
            String callDate = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.DATE));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.getDefault());
            String dateString = formatter.format(new Date(Long.parseLong(callDate)));
            String callType = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.TYPE));
            String isCallNew = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.NEW));
            String duration = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION));
            // process log data...
            if (callType != null && duration != null && callNumber != null) {
                callLog[i] = new CustomerCallLog();
                callLog[i].setCall_type(callType);
                callLog[i].setDuration(duration);
                callLog[i].setNumber(callNumber);
            }
            i++;
        }
        if(callLog != null && callLog.length > 0) {
            callLog[i - 1].setAccess_token(prefManager.getUserDetails().get(PrefManager.KEY_USERTOKEN));
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseModel> call = apiService.callSynchronization(callLog);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    Log.e("RESPONSE", response.message());
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("FAIL", t.getMessage());
                }
            });
        }
        if (cur != null) {
            cur.close();
        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    @Override
    public void refreshClicked() {
        if(isNetworkAvailable()) {
            fetchCustomerStatus();
            //upload user messages and call logs
            uploadMpesaMessages();
            //uploadAllCallLogs(getContentResolver());
            startService(new Intent(this, LocationService.class));
        }else{
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.fragment, new NoInternetFragment());
            tx.commit();
            Toast.makeText(MainActivity.this,"No Internet connection!", Toast.LENGTH_LONG).show();
        }
    }
}
