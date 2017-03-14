package com.ice.fourapp;

/**
 * Created by ice on 3/6/17.
 */

public class Venue {
    public String id;
    public String name;
    public String city;
    public String location;
    public String thumbnail;
    public String category;
    public String fullFileName;
    public String bookmarked;
    public String latitude;
    public String longitude;


    public Venue(String s1, String s2, String s3, String s4, String s5, String s6, String s7,  String s8,  String s9) {
        // Auto-generated constructor
        id = s1;
        name = s2;
        city = s3;
        location = s4;
        thumbnail = s5;
        category = s6;
        fullFileName = s7;
        bookmarked = "";
        latitude = s8;
        longitude = s9;
    }

    public Venue() {

        id = null;
        name = null;
        city = null;
        location = null;
        thumbnail = null;
        category = null;
        fullFileName = null;
        bookmarked = null;
    }
}

