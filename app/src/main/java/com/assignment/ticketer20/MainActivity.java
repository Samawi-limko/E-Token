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

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView current;
    Button book;
    int booked;
    TextView bookedid;  //// samawi

    SharedPreferences shared;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shared = getSharedPreferences("Booking", MODE_PRIVATE);
        if(shared.getBoolean("hasBooking", false)) {
            startActivity(new Intent(this, BookingScreen.class));
            this.finish();
        }

        setContentView(R.layout.activity_main);


        editor = shared.edit();

        current = (TextView) findViewById(R.id.current);
        bookedid = (TextView) findViewById(R.id.bookedid); /// samawi
        book = (Button) findViewById(R.id.book);



        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(getResources().getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create()).client(okBuilder.build());

        Retrofit retrofit = builder.build();

        Ticket ticket = retrofit.create(Ticket.class);


        Retro(ticket);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(current.equals("Loading.."))
                    return;
                else
                {
                    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
                    Retrofit.Builder builder = new Retrofit.Builder().baseUrl(getResources().getString(R.string.baseurl))
                            .addConverterFactory(GsonConverterFactory.create()).client(okBuilder.build());

                    Retrofit retrofit = builder.build();

                    final Ticket tkt = retrofit.create(Ticket.class);

                    Call<CreateBooking> call = tkt.createBooking(Integer.parseInt(getBooked())+1);
                    call.enqueue(new Callback<com.assignment.ticketer20.API.Model.CreateBooking>() {
                        @Override
                        public void onResponse(Call<com.assignment.ticketer20.API.Model.CreateBooking> call, Response<com.assignment.ticketer20.API.Model.CreateBooking> response) {
                            Log.i("Object", ""+response.body().get_id());
                            Toast.makeText(MainActivity.this, "Your booking is: "+response.body().getTicket(), Toast.LENGTH_SHORT).show();
                            editor.putBoolean("hasBooking", true);
                            editor.putString("id", response.body().get_id());
                            Log.e("MainActivity#86>>>>>", response.body().get_id());
                            editor.commit();

                            startActivity(new Intent(MainActivity.this, BookingScreen.class));
                            MainActivity.this.finish();
                        }

                        @Override
                        public void onFailure(Call<com.assignment.ticketer20.API.Model.CreateBooking> call, Throwable t) {

                        }
                    });
                }


            }
        });
    }





    public void Retro(Ticket ticket) {


        Call<List<Enquiry>> call = ticket.getAll();

        call.enqueue(new Callback<List<Enquiry>>() {
            @Override
            public void onResponse(Call<List<Enquiry>> call, Response<List<Enquiry>> response) {
                booked = Integer.parseInt(response.body().get(0).getBooked());
                current.setText(response.body().get(0).getCurrent());
                bookedid.setText(response.body().get(0).getBooked()); //// samawi


            }

            @Override
            public void onFailure(Call<List<Enquiry>> call, Throwable t) {
                Log.e("Error ", t.getMessage());
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
                Toast.makeText(MainActivity.this, "Booked: " + response.body().get(0).getBooked(), Toast.LENGTH_SHORT).show();
                booked = Integer.parseInt(response.body().get(0).getBooked());
            }

            @Override
            public void onFailure(Call<List<Enquiry>> call, Throwable t) {

            }
        });
        return Integer.toString(booked);
    }
}
