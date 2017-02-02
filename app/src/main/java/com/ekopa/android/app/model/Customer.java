package com.ekopa.android.app.model;


public class Customer {
    private String id;
    private String name;
    private String password;
    private String phonenumber;
    private String photo;
    private String access_token;
    private String id_number;
    private String dob;
    private String created_at;
    private String updated_at;
    private String is_activated;
    private String activation_code;
    private CustomerSettings settings;

    private String password_token;
    private String new_password;
    private String confirmation_password;

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
     * @return The id_number
     */
    public String getId_number() {
        return id_number;
    }

    /**
     * @param id_number The id_number
     */
    public void setId_number(String id_number) {
        this.id_number = id_number;
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
     * @return The is_activated
     */
    public String getIs_activated() {
        return is_activated;
    }

    /**
     * @param is_activated The is_activated
     */
    public void setIs_activated(String is_activated) {
        this.is_activated = is_activated;
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
}
