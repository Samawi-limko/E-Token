package com.assignment.ticketer20.API.EndPoint;


import com.assignment.ticketer20.API.Model.CreateBooking;
import com.assignment.ticketer20.API.Model.Enquiry;
import com.assignment.ticketer20.API.Model.TicketInfo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface Ticket {
    @GET("/enquiry")
    Call<List<Enquiry>> getAll();

    @POST("/enquiry/create")
    @FormUrlEncoded
    Call<com.assignment.ticketer20.API.Model.CreateBooking> createBooking(@Field("ticket") int ticket);

    @DELETE("/enquiry/delete/{id}")
    Call<Object> deleteBooking(@Path("id") String id);

    @PUT("/enquiry/postpone/{id}")
    Call<CreateBooking> postpone(@Path("id") String id);

    @GET("/enquiry/{id}")
    Call<TicketInfo> getTicket(@Path("id") String id);

}
