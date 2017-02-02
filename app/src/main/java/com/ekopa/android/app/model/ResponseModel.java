package com.ekopa.android.app.model;

/**
 * Created by Bishop on 7/30/2016.
 */
public class ResponseModel {
    private Integer status_code;
    private String reason_phrase;
    private String description;
    private Data data;

    /**
     *
     * @return
     * The status_code
     */
    public Integer getStatus_code() {
        return status_code;
    }

    /**
     *
     * @param status_code
     * The status_code
     */
    public void setStatus_code(Integer status_code) {
        this.status_code = status_code;
    }

    /**
     *
     * @return
     * The reason_phrase
     */
    public String getReason_phrase() {
        return reason_phrase;
    }

    /**
     *
     * @param reason_phrase
     * The reason_phrase
     */
    public void setReason_phrase(String reason_phrase) {
        this.reason_phrase = reason_phrase;
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
