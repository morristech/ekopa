package com.ekopa.android.app.model;

/**
 * Peter Gikera on 8/4/2016.
 */
public class Loan {

    private Integer id;
    private Integer loan_amount;
    private String request_date;
    private String due_date;
    private String reference_id;
    private String status;
    private String created_at;
    private String updated_at;
    private Integer customer_id;

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
     * @return The request_date
     */
    public String getRequest_date() {
        return request_date;
    }

    /**
     * @param request_date The request_date
     */
    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }

    /**
     * @return The due_date
     */
    public String getDue_date() {
        return due_date;
    }

    /**
     * @param due_date The due_date
     */
    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    /**
     * @return The reference_id
     */
    public String getReference_id() {
        return reference_id;
    }

    /**
     * @param reference_id The reference_id
     */
    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
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
}
