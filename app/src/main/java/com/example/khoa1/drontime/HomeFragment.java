package com.example.khoa1.drontime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khoa1.drontime.Adapter.BenhNhanAdapter;
import com.example.khoa1.drontime.Database.SQLiteBenhNhan;
import com.example.khoa1.drontime.Database.SQLiteRecord;
import com.example.khoa1.drontime.Model.BenhNhan;
import com.example.khoa1.drontime.Model.Record;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import static com.example.khoa1.drontime.Util.DateTimeUtil.dfDay;
import static com.example.khoa1.drontime.Util.DateTimeUtil.dfHour;
import static com.example.khoa1.drontime.Util.DateTimeUtil.isToday;


public class HomeFragment extends Fragment {

    private static final int REQUEST_CODE_VIEW = 22;

    public HomeFragment() {
        // Required empty public constructor
    }

    private TextView tvTongSoCa;
    private TextView tvSoCaDaGap;
    private TextView tvSoCaConLai;
    private ViewGroup groupViewRecordHomNay;
    private TextView tvNgay;
    private TextView tvGio;
    private TextView tvTieuDe;
    private TextView tvTenBenhNhan;
    private ImageView imgHinhAnh;
    SQLiteRecord sqLiteRecord;
    private Record record;
    private BenhNhan benhNhan;
    private Handler handlerGetRecord;
    private int tongKham = 0;
    private int daKham = 0;
    private int conLai = 0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initData();
        tvTongSoCa = (TextView) rootView.findViewById(R.id.tvTongSoCa);
        tvSoCaDaGap = (TextView) rootView.findViewById(R.id.tvSoCaDaGap);
        tvSoCaConLai = (TextView) rootView.findViewById(R.id.tvSoCaConLai);
        groupViewRecordHomNay = (ViewGroup) rootView.findViewById(R.id.groupViewRecordHomNay);

        groupViewRecordHomNay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(record == null) return;
                Intent intent = new Intent(getActivity().getApplicationContext(),ChiTietRecordActivity.class);
                //Truyen vao ma chi tieu cho Activity Chi Tiet Hoat Dong
                intent.putExtra("Record",record);
                startActivityForResult(intent, REQUEST_CODE_VIEW);
            }
        });
        tvNgay = (TextView) rootView.findViewById(R.id.tvNgay);
        tvGio = (TextView) rootView.findViewById(R.id.tvGio);
        tvTieuDe = (TextView) rootView.findViewById(R.id.tvTieuDe);
        tvTenBenhNhan = (TextView) rootView.findViewById(R.id.tvTenBenhNhan);
        imgHinhAnh = (ImageView) rootView.findViewById(R.id.imgHinhAnh);

        handlerGetRecord = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    setInfoGroupView();
                }
            }
        };


        return rootView;
    }

    private void initData() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                sqLiteRecord = new SQLiteRecord(getContext());
                record = sqLiteRecord.getNextMeeting();
                if(record != null)
                    benhNhan = record.getBenhNhan();
                //lấy message từ Main thread
                Message msg = handlerGetRecord.obtainMessage();
                msg.what = 1;
                msg.obj = record;
                //gửi lại Message này về cho Main Thread
                handlerGetRecord.sendMessage(msg);
            }
        });
        //kích hoạt tiến trình
        th.start();
    }

    private void setInfoGroupView()
    {
        if(record == null) return;
        Date ngayGio = record.getNgayGio();
        if(isToday(ngayGio)) {
            tvNgay.setText("Hôm nay");
            tvNgay.setBackgroundColor(0xE624A0ED);
        }
        else {
            tvNgay.setText(dfDay.format(ngayGio));
            tvNgay.setBackgroundColor(0XA6D3F2FD);
        }
        tvGio.setText(dfHour.format(record.getNgayGio()));

        tvTieuDe.setText(record.getTieuDe());
        tvTenBenhNhan.setText(benhNhan.getTenBenhNhan());
        if(benhNhan.getHinhAnh()!= null && benhNhan.getHinhAnh().length()!=0)
        {
            File imgFile = new  File(benhNhan.getHinhAnh());
            if(imgFile.exists()){
                Picasso.with(getContext()).load(imgFile).resize(250, 250).centerCrop().into(imgHinhAnh);
            }
        }
        else
        {
            Picasso.with(getContext()).load(R.drawable.no_photo).resize(250, 250).centerCrop().into(imgHinhAnh);
        }
        ArrayList<Integer> arrayList = sqLiteRecord.getOverViewToDay();
        tongKham = arrayList.get(0);
        daKham = arrayList.get(1);
        conLai = tongKham - daKham;

        tvTongSoCa.setText(String.valueOf(tongKham));
        tvSoCaDaGap.setText(String.valueOf(daKham));
        tvSoCaConLai.setText(String.valueOf(conLai));
    }


}
