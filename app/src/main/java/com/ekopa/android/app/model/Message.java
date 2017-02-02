package com.ekopa.android.app.model;


public class Message {
    private Integer message_type;
    private String address;
    private String body;
    private String access_token;

    /**
     * @return The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address set address
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * @return The message type
     */
    public Integer getMessage_type()
    {
        return message_type;
    }

    /**
     * @param message_type The message_type
     */
    public void setMessage_type(Integer message_type)
    {
        this.message_type = message_type;
    }

    /**
     * @return The body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body The body
     */
    public void setBody(String body) {
        this.body = body;
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
