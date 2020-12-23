package com.example.carsharing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private Integer token;
    private String url = "http://cars.areas.su/login";
    private HttpURLConnection connection;
    private JSONObject response_obj;
    private EditText usernameEditText, passwordEditText;
    private String username, password, request;
    private Button button;
    private WarningDialog warningDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        response_obj = new JSONObject();
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        button = findViewById(R.id.sign_in);
        button.setOnClickListener(new View.OnClickListener() {
            Boolean access = false;

            @Override
            public void onClick(View v) {
                try {
                    username = usernameEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                } catch (NullPointerException e) {
                    warningDialog = new WarningDialog("Заполните все поля!");
                    warningDialog.show(getSupportFragmentManager(), "custom");
                }
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            connection = (HttpURLConnection) new URL(url).openConnection();
                            connection.setRequestMethod("POST");
                            connection.setDoOutput(true);
                            connection.connect();
                            request = "username=" + username + "&" + "password=" + password;
                            connection.getOutputStream().write(request.getBytes());

                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            String inputLine = "";
                            StringBuilder response = new StringBuilder();
                            while ((inputLine = in.readLine()) != null){
                                response.append(inputLine);
                            }
                            int a = response.lastIndexOf("answer") - 2;
                            int b = response.indexOf("}", a) + 1;
                            String response_finally = response.substring(a, b);
                            JSONObject jsonObject = new JSONObject(response_finally);
                            String answer = jsonObject.get("answer").toString();
                            if(answer.equals("User is active")){
                                login();
                            } else {
                                try {
                                    token = Integer.parseInt(answer);
                                } catch (NumberFormatException e) {
                                    WarningDialog warningDialog = new WarningDialog("Неправильный логин или пароль!");
                                    warningDialog.show(getSupportFragmentManager(), "custom");
                                }
                            }
                            connection.disconnect();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            public void login(){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });
    }

    public void onClick(View view) throws IOException {
        switch (view.getId()){
            case R.id.registration:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}