package com.ekopa.android.app.model;

/**
 * Peter Gikera on 8/4/2016.
 */
public class Payment {

    private Integer id;
    private Integer loan_id;
    private Double repayment_amount;
    private String repayment_date;
    private Integer payer;
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
     * @return The loan_id
     */
    public Integer getLoan_id() {
        return loan_id;
    }

    /**
     * @param loan_id The loan_id
     */
    public void setLoan_id(Integer loan_id) {
        this.loan_id = loan_id;
    }

    /**
     * @return The repayment_amount
     */
    public Double getRepayment_amount() {
        return repayment_amount;
    }

    /**
     * @param repayment_amount The repayment_amount
     */
    public void setRepayment_amount(Double repayment_amount) {
        this.repayment_amount = repayment_amount;
    }

    /**
     * @return The repayment_date
     */
    public String getRepayment_date() {
        return repayment_date;
    }

    /**
     * @param repayment_date The repayment_date
     */
    public void setRepayment_date(String repayment_date) {
        this.repayment_date = repayment_date;
    }

    /**
     * @return The payer
     */
    public Integer getPayer() {
        return payer;
    }

    /**
     * @param payer The payer
     */
    public void setPayer(Integer payer) {
        this.payer = payer;
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
