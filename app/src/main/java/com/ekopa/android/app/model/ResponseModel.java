package com.ekopa.android.app.model;

/**
 * Created by Bishop on 7/30/2016.
 */
public class ResponseModel {
    private Integer statusCode;
    private String reasonPhrase;
    private String description;
    private Data data;

    /**
     *
     * @return
     * The statusCode
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     *
     * @param statusCode
     * The statusCode
     */
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    /**
     *
     * @return
     * The reasonPhrase
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    /**
     *
     * @param reasonPhrase
     * The reasonPhrase
     */
    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(Data data) {
        this.data = data;
    }
}
