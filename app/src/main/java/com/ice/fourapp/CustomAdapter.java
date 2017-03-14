package com.ice.fourapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context mContext;

    List<Venue> myList;
    private static LayoutInflater inflater=null;

    public CustomAdapter(VenueScreen mainActivity, List<Venue> myListArray) {
        mContext=mainActivity;
        myList = myListArray;

        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        TextView mName;
        ImageView mImg;
        TextView mLocation;
        TextView mCategories;
        TextView mBookmark;
        View rowView;

        rowView = inflater.inflate(R.layout.list_item, null);
        mName=(TextView) rowView.findViewById(R.id.textView1);
        mLocation=(TextView) rowView.findViewById(R.id.bodytext);
        mImg=(ImageView) rowView.findViewById(R.id.imageView1);
        mCategories=(TextView) rowView.findViewById(R.id.categories);
        mBookmark=(TextView) rowView.findViewById(R.id.bookmark);


        mName.setText(myList.get(position).name);
        mLocation.setText(myList.get(position).location);
        mCategories.setText(myList.get(position).category);
        mBookmark.setText(myList.get(position).bookmarked);

        String fileName = myList.get(position).fullFileName;
// Use Picasso addon to do picture loading
        Picasso.with(mContext).load(fileName).into(mImg);

        return rowView;
    }

}