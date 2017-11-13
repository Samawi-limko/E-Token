package com.assignment.ticketer20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.ticketer20.API.EndPoint.Ticket;
import com.assignment.ticketer20.API.Model.CreateBooking;
import com.assignment.ticketer20.API.Model.Enquiry;
import com.assignment.ticketer20.API.Model.TicketInfo;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookingScreen extends AppCompatActivity {

    SharedPreferences shared;
    SharedPreferences.Editor editor;
    TextView yourNum, current;
    Button cancel, postpone;
    int booked = 0;
    String ticketNum = "zero";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_screen);

        shared = getSharedPreferences("Booking", MODE_PRIVATE);
        editor = shared.edit();

        yourNum = (TextView) findViewById(R.id.yourNum);
        current = (TextView) findViewById(R.id.current);

        Retro();

        cancel = (Button) findViewById(R.id.cancel);
        postpone = (Button) findViewById(R.id.postpone);

        if(!shared.getBoolean("hasBooking", false)) {
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }

        getTicket(shared.getString("id", ""));
        Log.e("BookingScreen#63>>>", ticketNum);
        Toast.makeText(this, shared.getString("id", ""), Toast.LENGTH_SHORT).show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBooking(shared.getString("id", ""));

            }
        });

        postpone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postpone(shared.getString("id", ""));
            }
        });

    }

    public void Retro() {

        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(getResources().getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create()).client(okBuilder.build());

        Retrofit retrofit = builder.build();

        Ticket ticket = retrofit.create(Ticket.class);

        Call<List<Enquiry>> call = ticket.getAll();

        call.enqueue(new Callback<List<Enquiry>>() {
            @Override
            public void onResponse(Call<List<Enquiry>> call, Response<List<Enquiry>> response) {
                current.setText(response.body().get(0).getCurrent());
                booked = Integer.parseInt(response.body().get(0).getBooked());

            }

            @Override
            public void onFailure(Call<List<Enquiry>> call, Throwable t) {
                Log.e("Error ", t.getMessage());
            }
        });

        Timer timer = new Timer ();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Retro();
            }
        }, 10000, 60000);

        new Thread(new Runnable() {
            int lastMinute;
            int currentMinute;
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                currentMinute = calendar.get(Calendar.MINUTE);
                lastMinute = currentMinute;
                if (currentMinute != lastMinute) {
                    lastMinute = currentMinute;

                }
            }
        }).run();
    }
    
    public void deleteBooking(String id) {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(getResources().getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).client(okBuilder.build());

        Retrofit retrofit = builder.build();

        Ticket ticket = retrofit.create(Ticket.class);

        Call<Object> call = ticket.deleteBooking(id);
        
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                editor.clear();
                editor.commit();
                startActivity(new Intent(BookingScreen.this, MainActivity.class));
                BookingScreen.this.finish();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(BookingScreen.this, "Check the log", Toast.LENGTH_SHORT).show();
                Log.e("----------", ""+t.getMessage());
            }
        });
    }

    public void postpone(String id) {

        if (booked < Integer.parseInt (shared.getString("ticket", ""))+1) {
            Toast.makeText(this, "You cannot postpone", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(getResources().getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).client(okBuilder.build());

        Retrofit retrofit = builder.build();

        Ticket ticket = retrofit.create(Ticket.class);

        Call<CreateBooking> call = ticket.postpone(id);

        call.enqueue(new Callback<CreateBooking>() {
            @Override
            public void onResponse(Call<CreateBooking> call, Response<CreateBooking> response) {
                yourNum.setText(response.body().getTicket());
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("ticket", response.body().getTicket());
                editor.apply();
            }

            @Override
            public void onFailure(Call<CreateBooking> call, Throwable t) {
                Log.e("---------", t.getMessage());
            }
        });
    }

    public String getBooked(){

        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(getResources().getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create()).client(okBuilder.build());

        Retrofit retrofit = builder.build();

        Ticket tkt = retrofit.create(Ticket.class);

        Call<List<Enquiry>> call = tkt.getAll();
        call.enqueue(new Callback<List<Enquiry>>() {
            @Override
            public void onResponse(Call<List<Enquiry>> call, Response<List<Enquiry>> response) {
                booked = Integer.parseInt(response.body().get(0).getBooked());
            }

            @Override
            public void onFailure(Call<List<Enquiry>> call, Throwable t) {

            }
        });
        return Integer.toString(booked);
    }

    public void getTicket(String id) {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(getResources().getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create()).client(okBuilder.build());

        Retrofit retrofit = builder.build();

        Ticket tkt = retrofit.create(Ticket.class);

        Call<TicketInfo> call = tkt.getTicket(id);

        call.enqueue(new Callback<TicketInfo>() {
            @Override
            public void onResponse(Call<TicketInfo> call, Response<TicketInfo> response) {
                yourNum.setText(response.body().getTicket());
                Log.e(">>>>>>>>>", response.body().getTicket());
            }

            @Override
            public void onFailure(Call<TicketInfo> call, Throwable t) {

            }
        });

    }

}
