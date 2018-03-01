package com.example.khoa1.drontime;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.khoa1.drontime.Adapter.BenhNhanAdapter;
import com.example.khoa1.drontime.Database.SQLiteBenhNhan;
import com.example.khoa1.drontime.Model.BenhNhan;

import java.io.File;
import java.util.ArrayList;

public class ChonBenhNhanActivity extends AppCompatActivity {
    private ListView lvBenhNhan;
    private SQLiteBenhNhan sqLiteBenhNhan;
    private Handler handlerSetDataListview;
    private static final int MSG_SET_DATA_SUCCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_benh_nhan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Chọn bệnh nhân");
        sqLiteBenhNhan = new SQLiteBenhNhan(this);
        setDataListView();
        handlerSetDataListview = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_SET_DATA_SUCCESS) {
                    BenhNhanAdapter benhNhanAdapter = new BenhNhanAdapter(ChonBenhNhanActivity.this, R.layout.custom_listview_benh_nhan,
                    (ArrayList<BenhNhan>) msg.obj);
                    lvBenhNhan.setAdapter(benhNhanAdapter);
                    lvBenhNhan.setOnItemClickListener(new ListViewOnItemClickListener());
                }
            }
        };
        lvBenhNhan = (ListView) findViewById(R.id.lvBenhNhan);
    }

    private void setDataListView() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<BenhNhan> arrBenhNhan = sqLiteBenhNhan.getListBenhNhan();

                //lấy message từ Main thread
                Message msg = handlerSetDataListview.obtainMessage();
                msg.what = MSG_SET_DATA_SUCCESS;
                msg.obj = arrBenhNhan;
                //gửi lại Message này về cho Main Thread
                handlerSetDataListview.sendMessage(msg);
            }
        });
        //kích hoạt tiến trình
        th.start();
    }

    private class ListViewOnItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Intent intent = new Intent();
            //Truyen vao ma chi tieu cho Activity Chi Tiet Hoat Dong
            intent.putExtra("BenhNhan",((BenhNhan)adapterView.getItemAtPosition(i)));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
