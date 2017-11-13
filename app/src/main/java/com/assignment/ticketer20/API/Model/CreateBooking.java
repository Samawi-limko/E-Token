
package com.assignment.ticketer20.API.Model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CreateBooking {

    @SerializedName("ticket")
    private String mTicket;
    @SerializedName("_id")
    private String m_id;

    public String getTicket() {
        return mTicket;
    }

    public void setTicket(String ticket) {
        mTicket = ticket;
    }

    public String get_id() {
        return m_id;
    }

    public void set_id(String _id) {
        m_id = _id;
    }

}
