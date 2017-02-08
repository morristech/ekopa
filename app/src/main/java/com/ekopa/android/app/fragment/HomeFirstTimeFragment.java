package com.ekopa.android.app.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ekopa.android.app.MainActivity;
import com.ekopa.android.app.R;
import com.ekopa.android.app.activity.ApplyForLoanActivity;
import com.ekopa.android.app.api.ApiClient;
import com.ekopa.android.app.api.ApiInterface;
import com.ekopa.android.app.helper.PrefManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFirstTimeFragment extends Fragment {
    private PrefManager prefManager;

    @BindView(R.id.tv_home_greetings) TextView _custName;
    //@BindView(R.id.tv_home_customer_photo) CircleImageView _custPhoto;
    @BindView(R.id.tv_home_credit_limit)    TextView _creditLimit;
    @BindView(R.id.tv_next_credit_limit)    TextView _next_creditLimit;
    @BindView(R.id.tv_home_loan_balance)    TextView _homeLoanBalance;
    @BindView(R.id.btn_apply_for_loan)    AppCompatButton _btnApplyLoan;
    @BindView(R.id.btn_view_current_loan)    AppCompatButton _btnViewCurrentLoan;

    public static NumberFormat nf = NumberFormat.getIntegerInstance ();


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

        //This are the full Names of the Customer
        String name = prefManager.getUserDetails().get("name");
        String greetings = "Hi "+ name;
        _custName.setText(greetings);


        //Set the Balance
        String currentBalance = prefManager.getUserDetails().get(PrefManager.KEY_CURRENT_BALANCE);
        currentBalance = "KES "+ nf.format(Double.valueOf(currentBalance));

        //Determine User Action
        determineUserAction(prefManager.getUserDetails().get(PrefManager.KEY_CURRENT_BALANCE));

        _custName.setText(greetings);
        _homeLoanBalance.setText(currentBalance);

        //How much does he qualify?
        loadCustomerLoanMatrix();

        _btnApplyLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent applyLoan = new Intent(getContext(), ApplyForLoanActivity.class);
                startActivity(applyLoan);
            }
        });

        _btnViewCurrentLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent applyLoan = new Intent(getContext(),HomeActiveLoanFragment.class);
                startActivity(applyLoan);
            }
        });

        return view;
    }

    private void determineUserAction(String currentBalance) {
        Double balance = Double.valueOf(currentBalance);
        if(balance>0){
            _btnApplyLoan.setVisibility(View.INVISIBLE);
            _btnViewCurrentLoan.setVisibility(View.VISIBLE);
        }else{
            _btnApplyLoan.setVisibility(View.VISIBLE);
            _btnViewCurrentLoan.setVisibility(View.INVISIBLE);
        }
    }

    private void loadCustomerLoanMatrix() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Getting your qualification, please wait...");
        progressDialog.show();

        String idNumber = prefManager.getUserDetails().get(PrefManager.KEY_IDNUMBER);

        Log.d(">>>Value of Id Number::",idNumber);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.getCustomerLoanQualification("omy4w7bRRKEUFP9Z",idNumber);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.body()!=null){
                    onQualificationSuccess(response.body());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onQualificationFailed();
                Toast.makeText(getContext(),"Unable to pull your qualification. Please try again.", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });

    }

    private void onQualificationSuccess(JsonObject qualifications) {
        JsonArray loanQualification = qualifications.getAsJsonArray("loans");
        if(loanQualification.size()>0) {
            prefManager.setCustomerQualifiedAmounts(loanQualification.get(0).toString());
            String creditLimit = "KES "+ nf.format(Double.valueOf(prefManager.getUserDetails().get(PrefManager.KEY_CREDIT_LIMIT)));
            String nextQualifiedAmount = "KES 0";
            _creditLimit.setText(creditLimit);
            _next_creditLimit.setText(nextQualifiedAmount);
        }else{
            onQualificationFailed();
            Toast.makeText(getContext(),"Unable to pull your qualification. Empty Array.", Toast.LENGTH_LONG).show();
        }
    }

    public void onQualificationFailed(){
        _creditLimit.setText("0.0");
        _next_creditLimit.setText("0.0");
        _homeLoanBalance.setText("0.0");
    }

}
