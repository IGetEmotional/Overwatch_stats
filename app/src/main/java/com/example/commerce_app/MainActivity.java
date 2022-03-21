package com.example.commerce_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText user_field;
    private TextView textView;
    private Button button;
    private String answer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_field = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_field.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "Enter your battle tag", Toast.LENGTH_SHORT).show();
                }
                else{
                    String battleTag = user_field.getText().toString();
                    String url = "https://best-overwatch-api.herokuapp.com/player/pc/eu/"+battleTag;

                    new GetUrlData().execute(url);
                }
            }
        });

    }

    private class GetUrlData extends AsyncTask<String, String, String>{

        protected void onPreExecute(){
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Wait for it...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings){
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer str_buf = new StringBuffer();
                String s = "";
                while((s = reader.readLine()) != null)
                    str_buf.append(s).append("\n");
                return  str_buf.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(connection != null){
                    connection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @SuppressLint("SetTextI18n")

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject obj = new JSONObject(result);
               // answer = obj.getString("username");
                textView.setText(obj.getString("username"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}