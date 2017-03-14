package com.ice.fourapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ice on 3/6/17.
 */

public class PhotoScreen extends Activity {

    Button mButton;
    Button mapButton;
    TextView mName;
    String venueID;
    String venueName;
    String location;
    String photoCount;

    String latitude = "37.75";
    String longitude = "-122.15";

    List<String> photoList = new ArrayList<String>();
    GridView gridview;

    // foursquare token and version
    String token = "LLOSQ0WAHKANWWOSATMBNWWFZVQYV1QL542133YJ54DPHETE";
    String foursquareURL = "https://api.foursquare.com/v2/venues/";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo);

        Resources res = getResources(); // Resource object to get Drawables
        Intent i = getIntent();
        venueID = i.getStringExtra("venueid");
        venueName = i.getStringExtra("name");
        location = i.getStringExtra("location");

        latitude = i.getStringExtra("latitude");
        longitude = i.getStringExtra("longitude");

        mName = (TextView) findViewById(R.id.name);
        mName.setText("Pics for " + venueName);
        // setup list view
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(buttonHandler);

        mapButton = (Button) findViewById(R.id.mapbutton);
        mapButton.setOnClickListener(mapButtonHandler);

        getImages(venueID);
    }

    View.OnClickListener buttonHandler = new View.OnClickListener() {
        public void onClick(View v) {
            mButton.setText("Marked");

            //   setBookmark(venueID);
        }
    };


    View.OnClickListener mapButtonHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(PhotoScreen.this, MapsActivity.class);
            i.putExtra("location", location);
            i.putExtra("longitude", longitude);

            i.putExtra("latitude", latitude);
            i.putExtra("name", venueName);
            i.putExtra("zoom", 20.0f);
            startActivity(i);
        }
    };

    void getImages(String venueID) {
        try {
            venueID = URLEncoder.encode(venueID, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //  https://api.foursquare.com/v2/venues/$venueID/photos?oauth_token=$oathToken&v=20170306
        String url = foursquareURL + venueID + "/photos?oauth_token=" + token + "&v=20170306";
        Log.e("URL", "" + url);

        NetworkInterface ni = new NetworkInterface();
        ni.execute(url);
    }

    public void createPhotoAdapter() {
        gridview = (GridView)findViewById(R.id.gridView1);
        PhotoAdapter ia = new PhotoAdapter(this, photoList);
        gridview.setAdapter(ia);
    }


    public class NetworkInterface extends AsyncTask<String, Void, String> {
        public String server_response;


        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", server_response);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            doPhotoJSONDecode(server_response);
            mName.setText("Num Pics " + photoCount);

            createPhotoAdapter();

        }

// Converting InputStream to String

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }

        protected void doPhotoJSONDecode(String Content) {
            String id;
            String url;

            try {

                JSONObject jsonObject = new JSONObject(Content);
                JSONObject response = jsonObject.getJSONObject("response");
                JSONObject photos= response.getJSONObject("photos");
                photoCount = photos.optString("count");
                JSONArray  items = photos.getJSONArray("items");
                /*********** Process each JSON Node ************/

                for (int i = 0; i < items.length(); i++) {
                    /****** Get Object for each JSON node.***********/
                    JSONObject v = items.getJSONObject(i);

                    /******* Fetch node values **********/
                    id = v.optString("id");
                    url = v.optString("prefix") + "200x200" + v.optString("suffix");  // format for foursquare image download
                    photoList.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}