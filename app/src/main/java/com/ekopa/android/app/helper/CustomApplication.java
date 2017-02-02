package com.ekopa.android.app.helper;

import android.app.Application;

import com.github.orangegangsters.lollipin.lib.managers.LockManager;
import com.ekopa.android.app.R;
import com.ekopa.android.app.activity.ApplyForLoanActivity;
import com.ekopa.android.app.activity.LoginActivity;

/**
 * Peter Gikera on 9/14/2016.
 */
public class CustomApplication extends Application {

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate() {
        super.onCreate();

        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
        lockManager.getAppLock().setLogoId(R.mipmap.ic_launcher);
        lockManager.getAppLock().setTimeout(30000);
        lockManager.getAppLock().addIgnoredActivity(LoginActivity.class);
        lockManager.getAppLock().addIgnoredActivity(CustomPinActivity.class);
        lockManager.getAppLock().addIgnoredActivity(ApplyForLoanActivity.class);
    }
}