package com.ekopa.android.app.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ekopa.android.app.R;
import com.ekopa.android.app.api.ApiClient;
import com.ekopa.android.app.api.ApiInterface;
import com.ekopa.android.app.model.Customer;
import com.ekopa.android.app.model.ResponseModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {
    //Declare timer
    CountDownTimer cTimer = null;

    @BindView(R.id.btnReset)
    Button _btnReset;
    @BindView(R.id.input_token)
    EditText _passwordToken;
    @BindView(R.id.input_new_password_reset)
    EditText _newPassword;
    @BindView(R.id.input_confirm_password_reset)
    EditText _confirmPassword;
    @BindView(R.id.link_resend)
    TextView _linkResendCode;

    private static final String TAG = "ResetPasswordFragment";
    private static final String ARG_PARAM1 = "phoneNumber";

    private String mPhoneNumber;

    private ResetPasswordFragmentListener mListener;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    public static ResetPasswordFragment newInstance(String param1) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPhoneNumber = getArguments().getString(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        ButterKnife.bind(this, view);


        _btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

        startTimer();

        return view;
    }

    private void resetPassword() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onResetFailed("");
            return;
        }

        _btnReset.setEnabled(false);

        String passwordToken = _passwordToken.getText().toString();
        String password = _newPassword.getText().toString();
        String confirmPass = _confirmPassword.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Resetting your password...");
        progressDialog.show();

        Customer customer = new Customer();
        customer.setPhonenumber(mPhoneNumber);
        customer.setAccess_token(getString(R.string.system_token));
        customer.setPassword_token(passwordToken);
        customer.setNew_password(password);
        customer.setConfirmation_password(confirmPass);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.resetPassword(customer);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().getStatus_code().equals(200)) {
                    onResetSuccess();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                onResetFailed(t.getMessage());
            }
        });
    }

    private boolean validate() {
        boolean valid = true;

        String passwordToken = _passwordToken.getText().toString();
        String password = _newPassword.getText().toString();
        String confirmPass = _confirmPassword.getText().toString();

        if (passwordToken.isEmpty()) {
            _passwordToken.setError("Enter the code you received");
            valid = false;
        } else {
            _passwordToken.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 ) {
            _newPassword.setError("Password too short");
            valid = false;
        } else {
            _newPassword.setError(null);
        }

        if (confirmPass.isEmpty()){
            _confirmPassword.setError("Please confirm password");
            valid = false;
        }else if (!confirmPass.equals(password)){
            _confirmPassword.setError("Passwords do not match");
            valid = false;
        }else{
            _confirmPassword.setError(null);
        }

        return valid;
    }

    //start timer function
    void startTimer() {
        _linkResendCode.setOnClickListener(null);
        cTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                String message = "Resend code in: "+millisUntilFinished / 1000+" seconds";
                _linkResendCode.setText(message);
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

    private void resendCode() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Requesting server, please wait...");
        progressDialog.show();

        Customer customer = new Customer();
        customer.setPhonenumber(mPhoneNumber);
        customer.setAccess_token(getString(R.string.system_token));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.forgotPassword(customer);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().getStatus_code().equals(200)) {
                    startTimer();
                    Toast.makeText(getContext(), "You will receive the code shortly", Toast.LENGTH_SHORT).show();
                } else if (response.body().getStatus_code().equals(400)) {
                    onResetFailed(response.body().getData().getCode() != null ? response.body().getData().getCode():"");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                onResetFailed(t.getMessage());
            }
        });
    }


    private void onResetFailed(String message) {
        _btnReset.setEnabled(true);
        if (!message.equals("")) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    private void onResetSuccess() {
        _btnReset.setEnabled(true);
        Toast.makeText(getContext(), "Password Reset Successfully. Please Log in", Toast.LENGTH_LONG).show();
        mListener.onResetSuccess();
    }

    //cancel timer
    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancelTimer();
    }

    public interface ResetPasswordFragmentListener{
        void onResetSuccess();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ResetPasswordFragmentListener) {
            mListener = (ResetPasswordFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement refreshClicked");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
