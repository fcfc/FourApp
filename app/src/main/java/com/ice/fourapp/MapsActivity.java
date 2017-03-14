 package com.ice.fourapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

 public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

     private GoogleMap mMap;
     public LatLng mLatLng;

     public String title;
     public String location;

     public float zoom = 10;
     /**
      * ATTENTION: This was auto-generated to implement the App Indexing API.
      * See https://g.co/AppIndexing/AndroidStudio for more information.
      */
     private GoogleApiClient client;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_maps);
         // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                 .findFragmentById(R.id.map);

         Intent intent = getIntent();
         double lat = Double.parseDouble(intent.getStringExtra("latitude"));
         double lng = Double.parseDouble(intent.getStringExtra("longitude"));
         zoom = intent.getFloatExtra("zoom", 0.0f);
         title = intent.getStringExtra("name");
         location = intent.getStringExtra("location");
         mLatLng = new LatLng(lat, lng);
         mapFragment.getMapAsync(this);
         // ATTENTION: This was auto-generated to implement the App Indexing API.
         // See https://g.co/AppIndexing/AndroidStudio for more information.
         client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
     }


     /**

      */
     @Override
     public void onMapReady(GoogleMap googleMap) {
         mMap = googleMap;
         ArrayList<LatLng> coordList = new ArrayList<LatLng>();
         ArrayList<MarkerInfo> markerList = new ArrayList<MarkerInfo>();
         ArrayList<LatLng> coordList2 = new ArrayList<LatLng>();
         ArrayList<MarkerInfo> markerList2 = new ArrayList<MarkerInfo>();
         ArrayList<LatLng> coordList3 = new ArrayList<LatLng>();
         ArrayList<MarkerInfo> markerList3 = new ArrayList<MarkerInfo>();

         float zoomLevel = 15.0f;
         LatLng centerpoint = mLatLng;  // 37.74, -122.433
         coordList.add(mLatLng);
         markerList.add(new MarkerInfo(mLatLng.latitude, mLatLng.longitude, title, location));
         mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
         mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
         {

             @Override
             public boolean onMarkerClick(Marker arg0) {
 /*                getAlpha()
                 getId()
                 getPosition()
                 getRotation()
                 getSnippet()
                 getTag()
                 getTitle()
                 getZIndex() */
                 String name = arg0.getTitle() + "\n"  + arg0.getSnippet();
                 Toast.makeText(MapsActivity.this, name, Toast.LENGTH_SHORT).show();// display toast
                 return true;
             }

         });

 //        ReadCSVFile(R.raw.tour07, coordList, markerList);
 //        MakePolyline(googleMap, coordList, 10, Color.GREEN);
         MakeMarkers(markerList, googleMap);


         googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerpoint, zoomLevel));

         // Set listeners for click events.

     }

     public void MakeMarkers(ArrayList<MarkerInfo> mList, GoogleMap aMap) {
         Iterator<MarkerInfo> itr = mList.iterator();
         while (itr.hasNext()) {
             MarkerInfo element = itr.next();

             boolean vis = false;
             if (element.name.length() > 0)
                 vis = true;
             aMap.addMarker(new MarkerOptions()
                     .position(new LatLng(element.latitude, element.longitude))
                     .snippet(element.club)
                     .title(element.name)
                     .visible(vis));
         }
     }

     public void MakePolyline(GoogleMap aMap, ArrayList<LatLng> clist, int width, int color) {
         Polyline polyline2 = aMap.addPolyline(new PolylineOptions()
                 .clickable(true)
                 .addAll(clist)
                 .width(width)
                 .color(color));
     }
     public void ReadCSVFile (int csvfile, ArrayList<LatLng> cList, ArrayList<MarkerInfo> mList) {
         InputStream inputStream = getResources().openRawResource(csvfile);
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
         try {
             String line;
             while ((line = reader.readLine()) != null) {
                 String[] latlong =  line.split(",");
                 double latitude = Double.parseDouble(latlong[0]);
                 double longitude = Double.parseDouble(latlong[1]);
                 String name      = latlong[2];
                 String club      = latlong[3];
                 cList.add(new LatLng(latitude, longitude));
                 mList.add(new MarkerInfo(latitude, longitude, name, club));

             }
         }
         catch (IOException ex) {
             throw new RuntimeException("Error in reading CSV file: "+ex);
         }
     }

     public class MarkerInfo {
         public String name;
         public String club;
         public double latitude;
         public double longitude;
         public int zoom;
         public int radius;
         public String address;


         public MarkerInfo(Double d3, Double d4, String s2, String s3) {
             this.name = s2;
             this.club = s3;
             this.latitude = d3;
             this.longitude = d4;

         }

         public double getLongitude() {
             return longitude;
         }

         public double getLatitude() {
             return latitude;
         }

         @Override
         public String toString() {
             return String.format("%s:", name);
         }
     }
     /**
      * ATTENTION: This was auto-generated to implement the App Indexing API.
      * See https://g.co/AppIndexing/AndroidStudio for more information.
      */
     public Action getIndexApiAction() {
         Thing object = new Thing.Builder()
                 .setName("Maps Page") // TODO: Define a title for the content shown.
                 // TODO: Make sure this auto-generated URL is correct.
                 .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                 .build();
         return new Action.Builder(Action.TYPE_VIEW)
                 .setObject(object)
                 .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                 .build();
     }

     @Override
     public void onStart() {
         super.onStart();

         // ATTENTION: This was auto-generated to implement the App Indexing API.
         // See https://g.co/AppIndexing/AndroidStudio for more information.
         client.connect();
         AppIndex.AppIndexApi.start(client, getIndexApiAction());
     }

     @Override
     public void onStop() {
         super.onStop();

         // ATTENTION: This was auto-generated to implement the App Indexing API.
         // See https://g.co/AppIndexing/AndroidStudio for more information.
         AppIndex.AppIndexApi.end(client, getIndexApiAction());
         client.disconnect();
     }

 }
