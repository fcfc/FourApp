package com.ice.fourapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


public class VenueScreen extends Activity {
    String catID = null;
            ;
    ListView mListView;
    String cityName;
    List<Venue> venueList = new ArrayList<Venue>();
    List<String> imageList = new ArrayList<String>();

    // foursquare info
    String token = "LLOSQ0WAHKANWWOSATMBNWWFZVQYV1QL542133YJ54DPHETE";
    String foursquareURL = "https://api.foursquare.com/v2/venues/search?near=";
    String foursquareCatURL = "https://api.foursquare.com/v2/venues/search?categoryId=";
    String chineseRestCatId = "4bf58dd8d48988d145941735";
    String italianRestCatId = "4bf58dd8d48988d110941735";
    String vegCatId = "4bf58dd8d48988d1d3941735";
    String musicVenueId = "4bf58dd8d48988d1e5931735";
    String gasStationCatId ="4bf58dd8d48988d113951735";
    String barCatId = "4bf58dd8d48988d116941735";

    private RadioGroup radioCatGroup;
    private RadioButton radioCatButton;
    private Button btnSearch;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venue);

        Resources res = getResources(); // Resource object to get Drawables

        Intent i = getIntent();
        cityName = i.getStringExtra("city");
        TextView mCityText = (TextView) findViewById(R.id.city);
        mCityText.setText(cityName);

        mListView = (ListView) findViewById(R.id.venuelist);
        radioCatGroup = (RadioGroup) findViewById(R.id.radioCat);
        btnSearch = (Button) findViewById(R.id.search);
        btnSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioCatGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioCatButton = (RadioButton) findViewById(selectedId);
                String buttonText =  radioCatButton.getText().toString();
                if (buttonText.equals("Bar")) { catID = barCatId; };
                if (buttonText.equals("Gas")) { catID = gasStationCatId; };
                if (buttonText.equals("Ch")) { catID = chineseRestCatId; };
                if (buttonText.equals("It")) { catID = italianRestCatId; };
                if (buttonText.equals("Music")) { catID = musicVenueId; };
                if (buttonText.equals("Veg")) { catID = vegCatId; };

                venueList.clear();
                populateVenueList(cityName, catID);
            }

        });



        populateVenueList(cityName, catID);


    }

    public void onResume()   {
        super.onResume();

    }

    public void setBookmark(String id) {

    }

    public void createAdapter() {
        mListView.setAdapter(new CustomAdapter(this, venueList));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Venue v = (Venue) parent.getAdapter().getItem(position);
                Intent i = new Intent(VenueScreen.this, PhotoScreen.class);
                i.putExtra("venueid", v.id);
                i.putExtra("name", v.name);
                i.putExtra("location", v.location);
                i.putExtra("latitude", v.latitude);
                i.putExtra("longitude", v.longitude);
                startActivity(i);

            }
        });
    }

    private void populateVenueList(String city, String categoryID) {
        String url;
        try {
            city = URLEncoder.encode(city, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (categoryID != null) {
            url = foursquareCatURL + categoryID + "&near=" + city + "&oauth_token=" + token + "&v=20170306";
        } else {
            url = foursquareURL + city + "&oauth_token=" + token + "&v=20170306";

        }
        Log.v("URL", url);

        NetworkInterface ni = new NetworkInterface();
        ni.execute(url);
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

            Log.e("Response", "" + server_response);
            doVenueJSONDecode(server_response);
            createAdapter();
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



        protected void doVenueJSONDecode(String Content) {
            JSONObject jsonResponse;
            String id;
            String name;
            String address;
            String catName;
            String latitude;
            String longitude;
            String url = "";
            try {

                JSONObject jsonObject = new JSONObject(Content);
                JSONObject response = jsonObject.getJSONObject("response");
                JSONArray venues = response.getJSONArray("venues");
                /*********** Process each JSON Node ************/

                for (int i = 0; i < venues.length(); i++) {
                    /****** Get Object for each JSON node.***********/

                     JSONObject v = venues.getJSONObject(i);
                     catName = null;
                    /******* Fetch node values **********/
                    id = v.optString("id");
                    name = v.optString("name");
                    JSONObject location = v.getJSONObject("location");
                    address = location.optString("address") + " " + location.optString("crossStreet");
                    latitude = location.optString("lat");
                    longitude = location.optString("lng");
                    JSONArray categories = v.getJSONArray("categories");
                    for (int j = 0; j < categories.length(); j++) {
                        JSONObject cat = categories.getJSONObject(j);
                        catName = cat.optString("name");
                        JSONObject icon = cat.getJSONObject("icon");
                        url = icon.optString("prefix") + "100" + icon.optString("suffix");
                    }
                    Log.e("id", "" + id + "  " + name + "  " + address + " " + catName + " " + url);

                    venueList.add(new Venue(id, name, null, address, getFileName(url), catName, url, latitude, longitude));
                    imageList.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    // gets characters after last slash in url, usually filename
    private String getFileName(String url)  {
        String buffer = "";
        for (String token : url.split("/"))
        {
            buffer = token;
        }
        return buffer;
    }


}
