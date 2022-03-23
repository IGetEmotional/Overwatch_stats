package com.example.commerce_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    private EditText user_field;
    private Button button;
    //profile
    private ImageView avatar;
    private TextView name;
    private ImageView level_stars;
    //support
    private TextView textView;
    private ImageView supportRankImage;
    //tank
    private TextView tank_stat;
    private ImageView tankRankImage;
    //damage
    private TextView damage_stat;
    private ImageView damageRankImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_field = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        //support
        textView = findViewById(R.id.textView);
        supportRankImage = findViewById(R.id.imageSupportRank);
        //profile
        avatar = findViewById(R.id.avatar);
        name = findViewById(R.id.name);
        level_stars = findViewById(R.id.level_stars);
        //tank
        tankRankImage = findViewById(R.id.imageTankRank);
        tank_stat = findViewById(R.id.tank_stat);
        //damage
        damageRankImage = findViewById(R.id.imageDamageRank);
        damage_stat = findViewById(R.id.damage_stat);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_field.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "Enter your battle tag", Toast.LENGTH_SHORT).show();
                }
                else{
                    String battleTag = user_field.getText().toString();
                    String url = "https://owapi.io/profile/pc/eu/"+battleTag;

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
                //avatar
                Picasso.get().load(obj.getString("portrait")).into(avatar);
                name.setText(obj.getString("username"));
                Picasso.get().load(obj.getString("star")).into(level_stars);
                //support
                textView.setText(obj.getJSONObject("competitive").getJSONObject("support").getString("rank"));
                Picasso.get().load(obj.getJSONObject("competitive").getJSONObject("support").getString("rank_img")).into(supportRankImage);
                //tank
                tank_stat.setText(obj.getJSONObject("competitive").getJSONObject("tank").getString("rank"));
                Picasso.get().load(obj.getJSONObject("competitive").getJSONObject("tank").getString("rank_img")).into(tankRankImage);
                //damage
                damage_stat.setText(obj.getJSONObject("competitive").getJSONObject("damage").getString("rank"));
                Picasso.get().load(obj.getJSONObject("competitive").getJSONObject("damage").getString("rank_img")).into(damageRankImage);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}