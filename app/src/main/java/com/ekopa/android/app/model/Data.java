package com.ekopa.android.app.model;


import java.util.ArrayList;
import java.util.List;

public class Data {
    private String id; //Same as refId from server side
    private String name;
    private String password;
    private String password_token;
    private String new_password;
    private String confirmation_password;
    private String code;
    private String phonenumber;
    private String photo;
    private String access_token;
    private String idNumber;
    private String dob;
    private String created_at;
    private String updated_at;
    private String isActive;
    private String activation_code;
    private Integer creditScore;
    private Double currentBalance;
    private CustomerSettings settings;
    private List<Loan_request> loan_requests = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();

    /**
     * @return The password_token
     */
    public String getPassword_token() {
        return password_token;
    }

    /**
     * @param password_token The password_token
     */
    public void setPassword_token(String password_token) {
        this.password_token = password_token;
    }

    /**
     * @return The new_password
     */
    public String getNew_password() {
        return new_password;
    }

    /**
     * @param new_password The new_password
     */
    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    /**
     * @return The confirmation_password
     */
    public String getConfirmation_password() {
        return confirmation_password;
    }

    /**
     * @param confirmation_password The code
     */
    public void setConfirmation_password(String confirmation_password) {
        this.confirmation_password = confirmation_password;
    }

    /**
     * @return The code
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return The password
     */

    public String getPassword() {
        return password;
    }

    /**
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The phonenumber
     */
    public String getPhonenumber() {
        return phonenumber;
    }

    /**
     * @param phonenumber The phonenumber
     */
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    /**
     * @return The photo
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * @param photo The photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * @return The access_token
     */
    public String getAccess_token() {
        return access_token;
    }

    /**
     * @param access_token The access_token
     */
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    /**
     * @return The idNumber
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * @param idNumber The idNumber
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * @return The dob
     */
    public String getDob() {
        return dob;
    }

    /**
     * @param dob The dob
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * @return The created_at
     */
    public String getCreated_at() {
        return created_at;
    }

    /**
     * @param created_at The created_at
     */
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    /**
     * @return The updated_at
     */
    public String getUpdated_at() {
        return updated_at;
    }

    /**
     * @param updated_at The updated_at
     */
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    /**
     * @return The isActive
     */
    public String getIsActive() {
        return isActive;
    }

    /**
     * @param isActive The isActive
     */
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    /**
     * @return The activation_code
     */
    public String getActivation_code() {
        return activation_code;
    }

    /**
     * @param activation_code The activation_code
     */
    public void setActivation_code(String activation_code) {
        this.activation_code = activation_code;
    }

    /**
     * @return The settings
     */
    public CustomerSettings getCustomerSettings() {
        return settings;
    }

    /**
     * @param settings The settings
     */
    public void setSettings(CustomerSettings settings) {
        this.settings = settings;
    }

    /**
     * @return The loan_requests
     */
    public List<Loan_request> getLoan_requests() {
        return loan_requests;
    }

    /**
     * @param loan_requests The loan_requests
     */
    public void setLoan_requests(List<Loan_request> loan_requests) {
        this.loan_requests = loan_requests;
    }

    /**
     * @return The loans
     */
    public List<Loan> getLoans() {
        return loans;
    }

    /**
     * @param loans The loans
     */
    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    /**
     * @return The payments
     */
    public List<Payment> getPayments() {
        return payments;
    }

    /**
     * @param payments The payments
     */
    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    public CustomerSettings getSettings() {
        return settings;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }
}
