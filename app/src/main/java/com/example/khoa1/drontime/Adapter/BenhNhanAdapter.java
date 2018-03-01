package com.example.khoa1.drontime.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khoa1.drontime.Model.BenhNhan;
import com.example.khoa1.drontime.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by khoa1 on 12/14/2017.
 */

public class BenhNhanAdapter extends ArrayAdapter<BenhNhan> {
    private int resource;
    private ArrayList<BenhNhan> arrBenhNhan;
    private Context context;

    public BenhNhanAdapter(Context context, int resource, ArrayList<BenhNhan> arrBenhNhan) {
        super(context, resource, arrBenhNhan);
        this.context = context;
        this.resource = resource;
        this.arrBenhNhan = arrBenhNhan;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        BenhNhanAdapter.ViewHolder viewHolder;
        if (convertView == null) {  //Nếu convertView == null thì tạo một convertView mới bằng cách infalte từ row_listview
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_listview_benh_nhan, parent, false);
            //Tạo một viewHolder để lưu các giá view lấy được từ convertView
            viewHolder = new BenhNhanAdapter.ViewHolder();
            viewHolder.imgHinhAnh = (ImageView)convertView.findViewById(R.id.imgHinhAnh);
            viewHolder.imgTen = (ImageView)convertView.findViewById(R.id.imgTenBenhNhan);
            viewHolder.tvTenBenhNhan = (TextView) convertView.findViewById(R.id.tvTenBenhNhan);
            viewHolder.tvTrangThai = (TextView) convertView.findViewById(R.id.tvTrangThai);
            viewHolder.imgTrangThai = (ImageView)convertView.findViewById(R.id.imgTrangThai);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BenhNhanAdapter.ViewHolder) convertView.getTag();
        }
        BenhNhan benhNhan = arrBenhNhan.get(position);
        if(position%2==0) {
        convertView.setBackgroundResource(R.color.colorBackgroundListView);
    }
    else {
        convertView.setBackgroundResource(android.R.color.background_light);
    }
        //viewHolder.imgHinhAnh.setImageResource(R.drawable.avatar);
        String imgString = benhNhan.getHinhAnh();
        if(imgString != null && imgString.length()!=0)
        {
            File imgFile = new  File(imgString);

            if(imgFile.exists()){

                //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Picasso.with(context).load(imgFile).resize(150, 150).centerCrop().into(viewHolder.imgHinhAnh);
                //viewHolder.imgHinhAnh.setImageBitmap(myBitmap);

            }
        }
        else
        {
            Picasso.with(context).load(R.drawable.avatar).resize(150, 150).centerCrop().into(viewHolder.imgHinhAnh);
        }

        viewHolder.tvTenBenhNhan.setText(benhNhan.getTenBenhNhan());
        viewHolder.imgTen.setImageResource(R.drawable.account);
        if(benhNhan.isTrangThai()) {
            viewHolder.tvTrangThai.setText(R.string.finish_patient);
            viewHolder.imgTrangThai.setImageResource(R.drawable.green_check);
        }
        else {
            viewHolder.tvTrangThai.setText(R.string.pending_patient);
            viewHolder.imgTrangThai.setImageResource(R.drawable.yellow_mark);
        }
        return convertView;
    }

    public class ViewHolder {
        TextView tvTenBenhNhan, tvTrangThai;
        ImageView imgHinhAnh, imgTen, imgTrangThai;
    }
}