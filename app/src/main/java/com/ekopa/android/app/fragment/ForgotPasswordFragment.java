package com.ekopa.android.app.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lamudi.phonefield.PhoneInputLayout;
import com.ekopa.android.app.R;
import com.ekopa.android.app.api.ApiClient;
import com.ekopa.android.app.api.ApiInterface;
import com.ekopa.android.app.model.Customer;
import com.ekopa.android.app.model.ResponseModel;
import com.ekopa.android.app.util.KopaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment {

    @BindView(R.id.et_forgot_number)
    PhoneInputLayout _phoneNumber;
    @BindView(R.id.btn_forgot_submit)
    AppCompatButton _submitButton;

    private ForgotPasswordFragmentListener mListener;
    String phone;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ButterKnife.bind(this, v);

        _phoneNumber.setHint(R.string.phoneNumber);
        _phoneNumber.setDefaultCountry(KopaUtils.countryCode);

        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

        return v;
    }


    private void resetPassword() {
        phone = _phoneNumber.getPhoneNumber();

        if (_phoneNumber.isValid()){
            _phoneNumber.setError(null);
        }else{
            _phoneNumber.setError("Invalid Phone Number");
            return;
        }

        _submitButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Requesting server, please wait...");
        progressDialog.show();

        phone = phone.split("\\+")[1];
        Customer customer = new Customer();
        customer.setPhonenumber(phone);
        customer.setAccess_token(getString(R.string.system_token));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.forgotPassword(customer);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().getStatus_code().equals(200)) {
                    onResetSuccess();
                } else if (response.body().getStatus_code().equals(400)) {
                    onResetFailed(response.body().getData().getCode() != null ? response.body().getData().getCode():"");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                _submitButton.setEnabled(true);
                onResetFailed(t.getMessage());
            }
        });

    }

    private void onResetFailed(String message) {
        if (!message.equals("")) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
        _submitButton.setEnabled(true);
    }

    private void onResetSuccess() {
        _submitButton.setEnabled(true);
        mListener.requestResetSuccess(phone);
    }

    public interface ForgotPasswordFragmentListener{
        void requestResetSuccess(String phoneNumber);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ForgotPasswordFragmentListener) {
            mListener = (ForgotPasswordFragmentListener) context;
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
