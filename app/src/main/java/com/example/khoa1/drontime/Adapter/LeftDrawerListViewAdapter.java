package com.example.khoa1.drontime.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khoa1.drontime.Model.LeftDrawerListViewModel;
import com.example.khoa1.drontime.R;

/**
 * Created by khoa1 on 12/14/2017.
 */

public class LeftDrawerListViewAdapter extends ArrayAdapter<LeftDrawerListViewModel> {

    Context mContext;
    int layoutResourceId;
    LeftDrawerListViewModel data[] = null;

    public LeftDrawerListViewAdapter(Context mContext, int layoutResourceId, LeftDrawerListViewModel[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);

        LeftDrawerListViewModel folder = data[position];


        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);

        return listItem;
    }
}