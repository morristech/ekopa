package com.ekopa.android.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.ekopa.android.app.model.CustomerResponse;
import com.google.gson.Gson;
import com.lamudi.phonefield.PhoneInputLayout;
import com.ekopa.android.app.R;
import com.ekopa.android.app.api.ApiClient;
import com.ekopa.android.app.api.ApiInterface;
import com.ekopa.android.app.helper.PrefManager;
import com.ekopa.android.app.model.Customer;
import com.ekopa.android.app.model.ResponseModel;
import com.ekopa.android.app.util.KopaUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    static final int TAKE_PHOTO_CAMERA_REQUEST = 1;
    static final int TAKE_PHOTO_GALLERY_REQUEST = 2;
    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";
    private static final String TAG = "SignupActivity";
    String photo_base64 = "";

    @BindView(R.id.img_sign_up_photo)    CircleImageView user_photo;
    @BindView(R.id.img_sign_up_icon)    ImageView cam_icon;
    @BindView(R.id.input_idNumber) EditText _idNumberText;
    @BindView(R.id.input_phoneNumber) PhoneInputLayout _phoneNumber;
    @BindView(R.id.input_password_signUp) EditText _passwordText;
    @BindView(R.id.input_confirm_password_signUp) EditText _confirmPasswordText;
    @BindView(R.id.cb_terms_conditions)    AppCompatCheckBox _termsCheckBox;
    @BindView(R.id.link_terms_and_condition) TextView _termsLink;
    @BindView(R.id.btn_signup)   AppCompatButton _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        String s = getUserPhoneNumber();
        _phoneNumber.getEditText().setText(s != null ? s:"");

        _phoneNumber.setHint(R.string.phoneNumber);
        _phoneNumber.setDefaultCountry(KopaUtils.countryCode);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
        _termsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.terms_url)));
                startActivity(browserIntent);
            }
        });

        /**
         * Dates must be in the format 1990-05-27 (yyyy-mm-dd)
         * */

        //used to set date range for users +18
        final Calendar c = Calendar.getInstance();
        final int newYear = c.get(Calendar.YEAR) - 18;
        c.set(Calendar.YEAR, newYear);


        user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSheet.Builder(SignUpActivity.this)
                        .title("Profile Picture")
                        .sheet(R.menu.menu_image_picker).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.camera:
                                Intent pictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(pictureIntent, TAKE_PHOTO_CAMERA_REQUEST);
                                break;
                            case R.id.gallery:
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                                pickPhoto.setType("image/*");
                                startActivityForResult(pickPhoto, TAKE_PHOTO_GALLERY_REQUEST);
                                break;
                        }
                    }
                }).show();
            }
        });


    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


        String idNumber = _idNumberText.getText().toString();
//        String phoneNumber = "2547" + _phoneNumber.getText().toString();
        String phoneNumber = _phoneNumber.getPhoneNumber();
        phoneNumber = phoneNumber.split("\\+")[1];
        String password = _passwordText.getText().toString();


        // TODO: Implement your own signup logic here.
        Customer customer = new Customer();
        customer.setIdNumber(idNumber);
        customer.setPhoneNumber(phoneNumber);
        customer.setPassword(password);
        // customer.setPhoto(photo_base64)

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CustomerResponse> call = apiService.createCustomer("omy4w7bRRKEUFP9Z",customer);
        call.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                _signupButton.setEnabled(true);
                progressDialog.dismiss();
                if (response.body() != null && (response.body().getStatusCode().equals("0000")||response.body().getStatusCode().equals("E001") )) {
                    onSignupSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                _signupButton.setEnabled(true);
                progressDialog.dismiss();

                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Signup Failure",t);
            }
        });

    }

    public void onSignupSuccess(CustomerResponse response) {
        Gson gson = new Gson();
        String json = gson.toJson(response);
        CustomerResponse customer = gson.fromJson(json, CustomerResponse.class);
        PrefManager pref = new PrefManager(SignUpActivity.this);

        String userRefId = customer.getCustomerRefId();
        String name = customer.getName();
        //String imgURL = customer.getPhoto();
        String imgURL = "";
        String creditScore = customer.getCreditScore().toString();
        Double creditLimit=0.0;

        String phone = customer.getPhoneNumber(),
                id = customer.getIdNumber(),
                dob = customer.getDob();

        startActivity(new Intent(this, ActivationActivity.class));

        pref.createLoginSession(userRefId, name, imgURL, phone, id, dob, creditLimit.toString());

        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        _signupButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String idNumber = _idNumberText.getText().toString();
        String password = _passwordText.getText().toString();
        String confirmPass = _confirmPasswordText.getText().toString();
//        String phoneNumber = _phoneNumber.getText().toString();

        if (idNumber.isEmpty() || idNumber.length() < 7) {
            _idNumberText.setError("ID number is invalid. Must be greater than 7 digits.");
            valid = false;
        } else {
            _idNumberText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 ) {
            _passwordText.setError("Password too short");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

//        if (phoneNumber.isEmpty() || phoneNumber.length() < 8) {
//            _phoneNumber.setError("Valid phone number is required");
//            valid = false;
//        } else {
//            _phoneNumber.setError(null);
//        }

        if (_phoneNumber.isValid()) {
            _phoneNumber.setError(null);
        } else {
            // set error message
            _phoneNumber.setError("Valid phone number is required");
            valid = false;
        }

        if (confirmPass.isEmpty()){
            _confirmPasswordText.setError("Please confirm password");
            valid = false;
        }else if (!confirmPass.equals(password)){
            _confirmPasswordText.setError("Passwords do not match");
            valid = false;
        }else{
            _confirmPasswordText.setError(null);
        }

        if (!_termsCheckBox.isChecked()){
            Toast.makeText(SignUpActivity.this, "Agree to terms and condition to continue.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

//        if (photo_base64.isEmpty()) {
//            Toast.makeText(SignUpActivity.this, "Please take a selfie :)", Toast.LENGTH_SHORT).show();
//            valid = false;
//        }

        return valid;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO_CAMERA_REQUEST:
                if (resultCode == Activity.RESULT_CANCELED) {
                    // Avatar camera mode was canceled.
                } else if (resultCode == Activity.RESULT_OK) {
                    // TODO: HANDLE PHOTO TAKEN
                    Bitmap cameraPic = (Bitmap) data.getExtras().get("data");
                    saveAvatar(cameraPic);
                }
                break;
            case TAKE_PHOTO_GALLERY_REQUEST:
                if (resultCode == Activity.RESULT_CANCELED) {
                    // Avatar gallery request mode was canceled.
                } else if (resultCode == Activity.RESULT_OK) {
                    // TODO: HANDLE IMAGE CHOSEN
                    Uri photoUri = data.getData();
                    try {
                        Bitmap galleryPic = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                        saveAvatar(galleryPic);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void saveAvatar(Bitmap avatar) {
        cam_icon.setVisibility(View.GONE);
        user_photo.setImageBitmap(null);
        user_photo.setImageDrawable(null);
        user_photo.setImageBitmap(avatar);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        photo_base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
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
