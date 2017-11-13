package com.assignment.ticketer20.API.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Osama on 17/9/2017.
 */

public class Enquiry {

    @SerializedName("current")
    @Expose
    private String current;
    @SerializedName("booked")
    @Expose
    private String booked;

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getBooked() {
        return booked;
    }

    public void setBooked(String booked) {
        this.booked = booked;
    }
}
