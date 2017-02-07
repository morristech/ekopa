package com.ekopa.android.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ekopa.android.app.MainActivity;
import com.ekopa.android.app.R;
import com.ekopa.android.app.api.ApiClient;
import com.ekopa.android.app.api.ApiInterface;
import com.ekopa.android.app.helper.PrefManager;
import com.ekopa.android.app.model.ResponseModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyForLoanActivity extends AppCompatActivity {
    private static final String TAG = "ApplyForLoanActivity";

    @BindView(R.id.tv_apply_credit_limit)
    TextView _creditLimit;
    @BindView(R.id.et_apply_amount)
    EditText _amount;
    @BindView(R.id.btn_apply_loan_submit)
    AppCompatButton btnSubmit;

    PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new PrefManager(this);
        pref.checkLogin();
        setContentView(R.layout.activity_apply_for_loan);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String creditLimit = "KES " + pref.getUserDetails().get("creditLimit") + ".00";

        _creditLimit.setText(creditLimit);


        final MaterialDialog dlgConfirm = new MaterialDialog.Builder(this)
                .title("Success")
                .content("Loan application sent successfully. We will notify you once it has been approved.")
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).build();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitLoan();
            }
        });
    }

    private void submitLoan() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onSubmitFailed("");
            return;
        }

        btnSubmit.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ApplyForLoanActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting Request...");
        progressDialog.show();

        String amount = _amount.getText().toString();
        String token = pref.getUserDetails().get("userToken");

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.applyLoan(token, amount);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                if (response.body() == null) {
                    onSubmitFailed("Something went wrong, please try again later");
                } else {
                    if (response.body() != null && response.body().getStatusCode().equals(200)) {
                        onSubmitSuccess();
                    } else if (response.body().getStatusCode().equals(400)) {
                        onSubmitFailed("Incorrect phone or password");
                    } else if (response.body().getStatusCode().equals(404)) {
                        onSubmitFailed("No user found with that phone number.");
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void onSubmitSuccess() {
        btnSubmit.setEnabled(true);
        new MaterialDialog.Builder(ApplyForLoanActivity.this)
                .title("Success")
                .content(getString(R.string.submit_success))
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();

    }

    private void onSubmitFailed(String message) {
        if (!message.equals("")) {
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        }
        btnSubmit.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        int amount = 0, limit = Integer.parseInt(pref.getUserDetails().get("creditLimit"));

        if (!_amount.getText().toString().equals("")) {
            amount = Integer.parseInt(_amount.getText().toString());
            _amount.setError(null);
        } else {
            _amount.setError("How much are you applying for?");
            Toast.makeText(this, "How much are you applying for?", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (amount < 1000) {
            _amount.setError("Minimum amount to borrow is KES 1000");
            Toast.makeText(this, "Minimum amount to borrow is KES 1000", Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (amount > limit) {
            _amount.setError("You can not borrow more than your credit limit");
            Toast.makeText(this, "You can not borrow more than your credit limit", Toast.LENGTH_SHORT).show();
            valid = false;
        }else{
            _amount.setError(null);
        }
        return valid;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
