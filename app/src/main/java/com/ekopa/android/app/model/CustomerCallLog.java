package com.ekopa.android.app.model;


public class CustomerCallLog {
    private String number;
    private String duration;
    private String call_type;
    private String access_token;

    /**
     * @return The address
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number set address
     */
    public void setNumber(String number)
    {
        this.number = number;
    }

    /**
     * @return The message type
     */
    public String getDuration()
    {
        return duration;
    }

    /**
     * @param duration The message_type
     */
    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    /**
     * @return The body
     */
    public String getCall_type() {
        return call_type;
    }

    /**
     * @param call_type The body
     */
    public void setCall_type(String call_type) {
        this.call_type = call_type;
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

}
