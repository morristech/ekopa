package com.ekopa.android.app.model;


public class CustomerSettings {

    private Integer id;
    private Integer credit_limit;
    private String referral_code;
    private String status;
    private Integer customer_id;
    private String created_at;
    private String updated_at;

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
     * @return The credit_limit
     */
    public Integer getCredit_limit() {
        return credit_limit;
    }

    /**
     * @param credit_limit The credit_limit
     */
    public void setCredit_limit(Integer credit_limit) {
        this.credit_limit = credit_limit;
    }

    /**
     * @return The referral_code
     */
    public String getReferral_code() {
        return referral_code;
    }

    /**
     * @param referral_code The referral_code
     */
    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
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
}
