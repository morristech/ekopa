package com.ekopa.android.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ekopa.android.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePaidLoanFragment extends Fragment {


    public HomePaidLoanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_paid_loan, container, false);
    }

}
