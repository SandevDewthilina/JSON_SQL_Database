package com.example.jsonsqldatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
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

    private ProgressBar progressBar;
    private Button downloadBtn;
    private Button readBtn;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.main_progress);
        downloadBtn = findViewById(R.id.download_btn);
        readBtn = findViewById(R.id.sql_btn);

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncFetch().execute();
            }
        });

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(MainActivity.this, DetailsActivity.class);
                startActivity(detailIntent);
            }
        });

    }

    private class AsyncFetch extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);

            DatabaseHelper helper = new DatabaseHelper(getApplicationContext());

            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("DROP TABLE COSTUMERS;");

            Toast.makeText(MainActivity.this, "Deleted the table", Toast.LENGTH_LONG).show();

          Toast.makeText(MainActivity.this, "Download started", Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(String... string) {


            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("http://176.9.136.71:8001/pdaservice/getCustomerList?imei=354216101237562");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file2
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {                // exception is being thrown in here
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //this method will be running on UI thread
            DatabaseHelper helper = new DatabaseHelper(getApplicationContext());

            SQLiteDatabase db = helper.getWritableDatabase();

            db.execSQL("CREATE TABLE COSTUMERS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "CODE TEXT, "
                    + "ADDRESS TEXT, "
                    + "PHONE INTEGER);");

            progressBar.setVisibility(View.INVISIBLE);

            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);

                    ContentValues costumerValues = new ContentValues();

                    costumerValues.put("NAME", json_data.getString("Name"));
                    costumerValues.put("CODE", json_data.getString("Code"));
                    costumerValues.put("ADDRESS", json_data.getString("Address"));
                    costumerValues.put("PHONE", json_data.getString("Phone"));
                    db.insert("COSTUMERS", null, costumerValues);

                }
                Toast.makeText(MainActivity.this, "created the table", Toast.LENGTH_LONG).show();

               Toast.makeText(MainActivity.this, "Successfully retrieved the data", Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }


        }
    }

}
