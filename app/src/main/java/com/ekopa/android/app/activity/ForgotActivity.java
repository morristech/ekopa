package com.ekopa.android.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ekopa.android.app.R;
import com.ekopa.android.app.fragment.ForgotPasswordFragment;
import com.ekopa.android.app.fragment.ResetPasswordFragment;

public class ForgotActivity extends AppCompatActivity implements ForgotPasswordFragment.ForgotPasswordFragmentListener, ResetPasswordFragment.ResetPasswordFragmentListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fragment, new ForgotPasswordFragment());
        tx.commit();
    }


    @Override
    public void requestResetSuccess(String phoneNumber) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        new ResetPasswordFragment();
        tx.replace(R.id.fragment, ResetPasswordFragment.newInstance(phoneNumber));
        tx.commit();
    }


    @Override
    public void onResetSuccess() {
        // user is not logged in redirect him to Login Activity
        Intent i = new Intent(ForgotActivity.this, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Starting Login Activity
        startActivity(i);
    }
}
