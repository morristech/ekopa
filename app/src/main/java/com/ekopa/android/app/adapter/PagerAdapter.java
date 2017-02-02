package com.ekopa.android.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ekopa.android.app.fragment.ApprovedLoansFragment;
import com.ekopa.android.app.fragment.LoanRepaymentsFragment;
import com.ekopa.android.app.fragment.LoanRequestsFragment;

/**
 * Created by almodad on 06/07/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment frag=null;
        switch (position){
            case 0:
                frag=new LoanRequestsFragment();
                break;
            case 1:
                frag=new ApprovedLoansFragment();
                break;
            case 2:
                frag=new LoanRepaymentsFragment();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title=" ";
        switch (position){
            case 0:
                title="Loan Requests";
                break;
            case 1:
                title="Issued Loans";
                break;
            case 2:
                title="Repayments";
                break;
        }

        return title;
    }
}