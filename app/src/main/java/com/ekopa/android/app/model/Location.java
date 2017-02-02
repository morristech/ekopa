package com.ekopa.android.app.model;

/**
 * Peter Gikera on 8/4/2016.
 */
public class Location {

    private String access_token;
    private String lat;
    private String lon;

    /**
     * @return The getAccess_token
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
     * @return The lat
     */
    public String getLat() {
        return lat;
    }

    /**
     * @param lat The lat
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * @return The lon
     */
    public String getLon() {
        return lon;
    }

    /**
     * @param lon The lon
     */
    public void setLon(String lon) {
        this.lon = lon;
    }
}
