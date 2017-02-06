package com.ekopa.android.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ekopa.android.app.model.Customer;
import com.ekopa.android.app.model.CustomerResponse;
import com.google.gson.JsonObject;
import com.ekopa.android.app.MainActivity;
import com.ekopa.android.app.R;
import com.ekopa.android.app.api.ApiClient;
import com.ekopa.android.app.api.ApiInterface;
import com.ekopa.android.app.helper.PrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivationActivity extends AppCompatActivity {
    //Declare timer
    CountDownTimer cTimer = null;
    private static final String TAG = "ActivationActivity";

    @BindView(R.id.btnActivate)
    Button _btnActivate;
    @BindView(R.id.input_activationCode)
    EditText _activationCode;
    @BindView(R.id.link_resend)
    TextView _linkResendCode;
    PrefManager pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        ButterKnife.bind(this);
        pref = new PrefManager(ActivationActivity.this);
        _btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateCustomer();
            }
        });

        startTimer();
    }

    //start timer function
    void startTimer() {
        cTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                String message = "Resend code in: "+millisUntilFinished / 1000+" seconds";
                _linkResendCode.setText(message);
                _linkResendCode.setOnClickListener(null);
            }

            public void onFinish() {
                _linkResendCode.setText(getString(R.string.prompt_resend_code));
                _linkResendCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resendCode();
                    }
                });
            }
        };
        cTimer.start();
    }


    //cancel timer
    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    private void activateCustomer() {
        Log.d(TAG, "Activate Customer");

        if (!validate()) {
            onActivateFailed("");
            return;
        }

        _btnActivate.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ActivationActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Activating Account...");
        progressDialog.show();


        String userRefId = pref.getUserDetails().get("userRefId");


        String code = _activationCode.getText().toString();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.activateCustomer("omy4w7bRRKEUFP9Z",code, userRefId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    String status = response.body().get("statusCode").getAsString();
                    String description = response.body().get("statusDescription").getAsString();
                    String reason = response.body().get("resultStatus").getAsString();

                    if (status.equals("0000")) {
                        onActivateSuccess();
                    } else {
                        onActivateFailed(description);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                //TODO change error message shown to users
                onActivateFailed(t.getMessage());

                Log.d(TAG, "Activate Customer",t);
            }
        });
    }

    private void resendCode() {
        Log.d(TAG, "Resend Code");

        final ProgressDialog progressDialog = new ProgressDialog(ActivationActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Resending Code...");
        progressDialog.show();

        _btnActivate.setEnabled(false);

        // TODO: Implement your own signup logic here.
        Customer customer = new Customer();
        customer.setIdNumber(pref.getUserDetails().get("idNumber"));
        customer.setPhoneNumber(pref.getUserDetails().get("phoneNumber"));
        // customer.setPhoto(photo_base64)

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CustomerResponse> call = apiService.createCustomer("omy4w7bRRKEUFP9Z",customer);
        call.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                progressDialog.dismiss();
                _btnActivate.setEnabled(true);
                startTimer();
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                progressDialog.dismiss();
                _btnActivate.setEnabled(true);
            }
        });

    }

    private boolean validate() {
        boolean valid = true;

        String code = _activationCode.getText().toString();

        if (code.isEmpty()) {
            _activationCode.setError("Field is required");
            valid = false;
        } else {
            _activationCode.setError(null);
        }
        return valid;
    }

    private void onActivateFailed(String message) {
        if (!message.equals(""))
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        _btnActivate.setEnabled(true);
    }

    private void onActivateSuccess() {
        _btnActivate.setEnabled(true);
        PrefManager pref = new PrefManager(ActivationActivity.this);
        pref.setIsLogin();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


}
