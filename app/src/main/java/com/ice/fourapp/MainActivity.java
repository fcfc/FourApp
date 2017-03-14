package com.ice.fourapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    ListView mListView;
    List<String> cityList = new ArrayList<String>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources(); // Resource object to get Drawables

        // setup list view
        mListView = (ListView) findViewById(R.id.list);

        TypedArray cities= res.obtainTypedArray(R.array.cities);
        for (int i=0; i<cities.length(); i++) {
            cityList.add(cities.getString(i));
        }

        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityList));
        mListView.setVisibility(View.VISIBLE);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView parent, View view, int position, long id) {

                CharSequence testString = ((TextView) view).getText();
                Intent i = new Intent(MainActivity.this, VenueScreen.class);
                i.putExtra("city", testString);
                startActivity(i);
            }
        });

    }
}