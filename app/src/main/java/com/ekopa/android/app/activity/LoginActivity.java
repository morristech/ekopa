package com.ekopa.android.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.lamudi.phonefield.PhoneInputLayout;
import com.ekopa.android.app.MainActivity;
import com.ekopa.android.app.R;
import com.ekopa.android.app.api.ApiClient;
import com.ekopa.android.app.api.ApiInterface;
import com.ekopa.android.app.helper.PrefManager;
import com.ekopa.android.app.model.Customer;
import com.ekopa.android.app.model.Data;
import com.ekopa.android.app.model.ResponseModel;
import com.ekopa.android.app.util.KopaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_phoneNumber) PhoneInputLayout _phoneText;
    @BindView(R.id.input_password)    EditText _passwordText;
    @BindView(R.id.btn_login)    Button _loginButton;
    @BindView(R.id.login_button)    LoginButton _fbLoginButton;
    @BindView(R.id.link_signup)    TextView _signupLink;
    @BindView(R.id.link_forgot_password)    TextView _forgotPassLink;

    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        String s = getUserPhoneNumber();
        _phoneText.getEditText().setText(s != null ? s:"");

        _phoneText.setHint(R.string.phoneNumber);
        _phoneText.setDefaultCountry(KopaUtils.countryCode);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
        _forgotPassLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        _fbLoginButton.setReadPermissions("email");

        _fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed("");
            return;
        }

//        _loginButton.setEnabled(false);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

//        String phone = "2547" + _phoneText.getText().toString();
        String phone = _phoneText.getPhoneNumber();
        phone = phone.split("\\+")[1];
        Log.e("PHONE", phone);
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        Customer customer = new Customer();
        customer.setPhonenumber(phone);
        customer.setPassword(password);
        customer.setAccess_token(getString(R.string.system_token));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.loginCustomer(customer);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    if(response.body().getStatus_code().equals(200)) {
                        onLoginSuccess(response.body());
                    }else if (response.body().getStatus_code().equals(400)) {
                        onLoginFailed("Incorrect phone or password");
                    } else if (response.body().getStatus_code().equals(404)) {
                        onLoginFailed("No user found with that phone number.");
                    }
                }else{
                    onLoginFailed("No response from server.");
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                onLoginFailed(t.getMessage());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                startActivity(new Intent(this, ActivationActivity.class));
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(ResponseModel rm) {
        _loginButton.setEnabled(true);
        Data data = rm.getData();
        PrefManager pref = new PrefManager(LoginActivity.this);

        pref.createLoginSession(data.getAccess_token(), data.getId(), data.getName(), data.getPhoto(),
                data.getPhonenumber(), data.getId_number(), data.getDob(),
                data.getCustomerSettings().getCredit_limit().toString(),
                data.getCustomerSettings().getReferral_code());

        if (data.getIs_activated().equals("1")) {

            pref.setIsLogin();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, ActivationActivity.class));
            this.finish();
        }
    }

    public void onLoginFailed(String message) {
        if (!message.equals("")) {
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        }
        _loginButton.setEnabled(true);
    }


    public boolean validate() {
        boolean valid = true;

//        String phone = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();

        if (_phoneText.isValid()) {
            _phoneText.setError(null);
        } else {
            // set error message
            _phoneText.setError("Phone number is invalid");
            valid = false;
        }

//        if (phone.isEmpty() || phone.length() < 8) {
//            _phoneText.setError("Phone number is invalid");
//            valid = false;
//        } else {
//            _phoneText.setError(null);
//        }

        if (password.isEmpty() || password.length() < 4) {
            _passwordText.setError("Password is too short");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    private String requestPhoneNumber(){
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getLine1Number();
    }

    private String getUserPhoneNumber(){
        String s = requestPhoneNumber();
        return s != null && s.length() > 8 ? "0"+s.substring(s.length()-9) : null;
    }
}
