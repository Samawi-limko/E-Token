package com.assignment.ticketer20.API.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohammed Al-Samawi on 11/11/2017.
 */

public class TicketInfo {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("ticket")
    @Expose
    private String ticket;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
