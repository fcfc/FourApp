package com.ice.fourapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends BaseAdapter {
    private Context mContext;
    public List<String> imageList = new ArrayList<String>();


    public PhotoAdapter(Context c, List<String> photoList) {
        this.mContext = c;
        this.imageList = photoList;

    }

    public int getCount() {
        return this.imageList.size();
    }

    public Object getItem(int position) {
        return this.imageList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView photoView = null;
        String fileName = imageList.get(position);

        if (convertView == null) {
            photoView = new ImageView(mContext);
            photoView.setLayoutParams(new GridView.LayoutParams(300, 300));
            photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            photoView.setPadding(8, 8, 8, 8);

        } else {
            photoView = (ImageView) convertView;
        }
;
        Log.e("Inside Photo Adapter", fileName);
        Picasso.with(mContext).load(fileName).into(photoView);

        return photoView;
    }

}