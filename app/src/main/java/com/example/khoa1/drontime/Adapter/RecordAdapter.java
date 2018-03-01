package com.example.khoa1.drontime.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khoa1.drontime.Model.BenhNhan;
import com.example.khoa1.drontime.Model.Record;
import com.example.khoa1.drontime.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by khoa1 on 12/14/2017.
 */

public class RecordAdapter extends ArrayAdapter<Record> {
    private int resource;
    private ArrayList<Record> arrRecord;
    private Context context;
    private SimpleDateFormat dfDay = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat dfHour = new SimpleDateFormat("HH:mm");

    public RecordAdapter(Context context, int resource, ArrayList<Record> arrRecord) {
        super(context, resource, arrRecord);
        this.context = context;
        this.resource = resource;
        this.arrRecord = arrRecord;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        RecordAdapter.ViewHolder viewHolder;
        if (convertView == null) {  //Nếu convertView == null thì tạo một convertView mới bằng cách infalte từ row_listview
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_listview_record, parent, false);
            //Tạo một viewHolder để lưu các giá view lấy được từ convertView
            viewHolder = new RecordAdapter.ViewHolder();
            viewHolder.tvTenBenhNhan = (TextView) convertView.findViewById(R.id.tvTenBenhNhan);
            viewHolder.tvTieuDe = (TextView) convertView.findViewById(R.id.tvTieuDe);
            viewHolder.tvNgay = (TextView) convertView.findViewById(R.id.tvNgay);
            viewHolder.tvGio = (TextView) convertView.findViewById(R.id.tvGio);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RecordAdapter.ViewHolder) convertView.getTag();
        }
        Record record = arrRecord.get(position);
        if(position%2==0) {
            convertView.setBackgroundResource(R.color.colorBackgroundListView);
        }
        else {
            convertView.setBackgroundResource(android.R.color.background_light);
        }

        viewHolder.tvTenBenhNhan.setText(record.getBenhNhan().getTenBenhNhan());
        viewHolder.tvTieuDe.setText(record.getTieuDe());
        Date ngayGio = record.getNgayGio();
        if(isToday(ngayGio)) {
             viewHolder.tvNgay.setText("Hôm nay");
             viewHolder.tvNgay.setBackgroundColor(0xE624A0ED);
        }
        else {
            viewHolder.tvNgay.setText(dfDay.format(ngayGio));
            viewHolder.tvNgay.setBackgroundColor(0XA6D3F2FD);
        }
        viewHolder.tvGio.setText(dfHour.format(record.getNgayGio()));
        return convertView;
    }

    public class ViewHolder {
        TextView tvTenBenhNhan, tvTieuDe, tvNgay, tvGio;
    }

    private static long daysBetween(Date one, Date two) {
        long difference =  (one.getTime()-two.getTime())/86400000;
        return Math.abs(difference);
    }

    private boolean isToday(Date time) {
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTime(time);
        return now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)
                && now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR);
    }
}