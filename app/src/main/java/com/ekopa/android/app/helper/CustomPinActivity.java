package com.ekopa.android.app.helper;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.orangegangsters.lollipin.lib.managers.AppLockActivity;
import com.ekopa.android.app.R;

/**
 * Peter Gikera on 9/14/2016.
 */
public class CustomPinActivity extends AppLockActivity {
    @Override
    public void showForgotDialog() {
        //Launch your popup or anything you want here
        new MaterialDialog.Builder(CustomPinActivity.this)
                .title("Log out")
                .content(R.string.prompt_log_out_to_reset_pin)
                .positiveText("LOG OUT")
                .negativeText("CANCEL")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        logoutUser();
                    }
                })
                .show();
    }

    @Override
    public void onPinFailure(int attempts) {

        if (attempts == 3){
            Toast.makeText(getApplicationContext(), "Forgot Pin?", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPinSuccess(int attempts) {

    }


    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void logoutUser() {
        PrefManager prefManager = new PrefManager(getApplicationContext());
        prefManager.logoutUser();
    }
}