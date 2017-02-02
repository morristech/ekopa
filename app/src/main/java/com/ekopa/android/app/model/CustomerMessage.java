package com.ekopa.android.app.model;


public class CustomerMessage {
    private String address;
    private String body;
    private String message_type;

    /**
     * @return The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address The address
     */
    public void setAddress(String address) {
        this.address = address;
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
     * @return The message_type
     */
    public String getMessage_type() {
        return message_type;
    }

    /**
     * @param message_type The message_type
     */
    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }
}
