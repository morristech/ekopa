package com.ekopa.android.app.model;

/**
 * Peter Gikera on 8/4/2016.
 */
public class Loan_request {

    private Integer id;
    private Integer customer_id;
    private Integer loan_amount;
    private String status;
    private Integer approved_loan_id;
    private String created_at;
    private String updated_at;
    private Integer device_id;

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The customer_id
     */
    public Integer getCustomer_id() {
        return customer_id;
    }

    /**
     * @param customer_id The customer_id
     */
    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    /**
     * @return The loan_amount
     */
    public Integer getLoan_amount() {
        return loan_amount;
    }

    /**
     * @param loan_amount The loan_amount
     */
    public void setLoan_amount(Integer loan_amount) {
        this.loan_amount = loan_amount;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The approved_loan_id
     */
    public Integer getApproved_loan_id() {
        return approved_loan_id;
    }

    /**
     * @param approved_loan_id The approved_loan_id
     */
    public void setApproved_loan_id(Integer approved_loan_id) {
        this.approved_loan_id = approved_loan_id;
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
     * @return The device_id
     */
    public Integer getDevice_id() {
        return device_id;
    }

    /**
     * @param device_id The device_id
     */
    public void setDevice_id(Integer device_id) {
        this.device_id = device_id;
    }
}
