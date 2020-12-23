package com.example.carsharing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TimerActivity extends AppCompatActivity {

    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    private String url_1 = "http://cars.areas.su/book";
    private HttpURLConnection connection;
    private JSONObject response_obj;
    private String request;
    private WarningDialog warningDialog;
    private String idCar, modePay, timeNow, token;
    private TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        timer = findViewById(R.id.timer);
        Bundle args = getIntent().getExtras();
        idCar = args.getString("idCar");
        modePay = args.getString("modePay");
        timeNow = args.getString("timeNow");
        token = args.getString("token");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    connection = (HttpURLConnection) new URL(url_1).openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.connect();
                    request = "idCar=" + idCar + "&" + "modePay=" + modePay + "&" + "timeNow=" + timeNow + "&" + "token=" + token;
                    connection.getOutputStream().write(request.getBytes());

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine = "";
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null){
                        response.append(inputLine);
                    }

                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            startTimer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

public void startTimer() throws InterruptedException {
    for(int i = 0; i < 1000 * 60 * 15; i++) {
        Thread.sleep(1000);
        seconds++;
        if (seconds == 60) {
            minutes++;
            seconds = 0;
        }
        if (minutes == 60) {
            hours++;
            minutes = 0;
        }
        if (seconds < 10) {
            timer.setText(String.valueOf(hours) + ":" + String.valueOf(minutes) + ":" + "0" + String.valueOf(seconds));
        } else {
            timer.setText(String.valueOf(hours) + ":" + String.valueOf(minutes) + ":" + String.valueOf(seconds));
            }
        }
    }
}