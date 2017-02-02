package com.ekopa.android.app.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekopa.android.app.R;
import com.ekopa.android.app.activity.ApplyForLoanActivity;
import com.ekopa.android.app.helper.PrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFirstTimeFragment extends Fragment {
    private PrefManager prefManager;

    @BindView(R.id.tv_home_greetings) TextView _custName;
    //@BindView(R.id.tv_home_customer_photo) CircleImageView _custPhoto;
    @BindView(R.id.tv_home_credit_limit)    TextView _creditLimit;
    @BindView(R.id.btn_apply_for_loan)    AppCompatButton _btnApplyLoan;

    public HomeFirstTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prefManager = new PrefManager(getContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_first_time, container, false);
        ButterKnife.bind(this, view);

        String creditLimit = "KES "+prefManager.getUserDetails().get("creditLimit");
        String photoURL = prefManager.getUserDetails().get("ImageURL");
        //Picasso.with(getContext()).load(photoURL).into(_custPhoto);
        String name = prefManager.getUserDetails().get("name");
        String arr[] = name.split(" ", 2);
        String greetings = "Hi "+arr[0];

        _custName.setText(greetings);
        _creditLimit.setText(creditLimit);

        _btnApplyLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent applyLoan = new Intent(getContext(), ApplyForLoanActivity.class);
                startActivity(applyLoan);
            }
        });

        return view;
    }

}
