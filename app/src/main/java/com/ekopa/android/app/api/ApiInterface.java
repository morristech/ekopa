package com.ekopa.android.app.api;


import com.ekopa.android.app.model.CustomerResponse;
import com.google.gson.JsonObject;
import com.ekopa.android.app.model.Customer;
import com.ekopa.android.app.model.CustomerCallLog;
import com.ekopa.android.app.model.Location;
import com.ekopa.android.app.model.Message;
import com.ekopa.android.app.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    /**
     * Endpoints:
     * /api/customer/create?access_token=token     POST
     * /api/customer/update?access_token=token     POST
     * /api/customer/login?access_token=token      POST
     * /api/customer/activate?access_token=token   POST
     * /api/customer/activate?access_token=token   POST
     * /api/customer/activate?access_token=token       GET
     * /api/customer/calls?access_token=token      POST
     * /api/customer/messages?access_token=token   POST
     * /api/loan/apply?access_token=token          POST
     * /api/loan/loanIdentifier?access_token=token     GET
     **/

    @POST("users/createUserByIdNumber")
    Call<CustomerResponse> createCustomer(@Header("api-key") String apiKey, @Body Customer customer);

    @POST("customer/update")
    Call<ResponseModel> updateCustomerDetails(@Body Customer customer);

    @POST("users/auth3")
    Call<ResponseModel> loginCustomer(@Header("api-key") String apiKey,@Body Customer customer);

    @POST("customer/password/forgot")
    Call<ResponseModel> forgotPassword(@Body Customer customer);

    @POST("customer/password/reset")
    Call<ResponseModel> resetPassword(@Body Customer customer);

    @FormUrlEncoded
    @POST("user/activate")
    Call<JsonObject> activateCustomer(@Header("api-key") String apiKey, @Path("activationCode") String activationCode, @Path("userRefId") String userRefId);

    @POST("customer/activate/resend")
    Call<JsonObject> resendActivationCode(@Query("access_token") String userToken);

    @POST("customer/activate")
    Call<ResponseModel> updateCustomerProfile(@Body Customer customer);

    @GET("customer/status")
    Call<ResponseModel> getCustomerStatus(@Query("access_token") String userToken);

    @POST("customer/calls?access_token=SYSKEY")
    Call<ResponseModel> callSynchronization(@Body CustomerCallLog [] callLog);

    @POST("customer/messages?access_token=SYSKEY")
    Call<ResponseModel> messageSynchronization(@Body Message [] message);


    @FormUrlEncoded
    @POST("loan/apply")
    Call<ResponseModel> applyLoan(@Query("access_token") String userToken, @Field("loan_amount") String loanAmount);

    @GET("loan/{loanIdentifier}")
    Call<ResponseModel> getLoanDetails(@Path("loanIdentifier") String loanIdentifier, @Query("access_token") String userToken);

    @GET("customer/status")
    Call<ResponseModel> customerStatus(@Query("access_token") String userToken);

    @POST("customer/locations")
    Call<ResponseModel> updateCustomerLocation(@Body Location location);

}
