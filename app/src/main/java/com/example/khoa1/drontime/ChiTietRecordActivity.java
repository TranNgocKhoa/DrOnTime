package com.example.khoa1.drontime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khoa1.drontime.Database.SQLiteRecord;
import com.example.khoa1.drontime.Model.Record;

import java.text.SimpleDateFormat;

public class ChiTietRecordActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_VIEW = 22;
    private TextView tvTieuDe;
    private TextView tvBenhNhan;
    private TextView tvNgay;
    private TextView tvGio;
    private ImageView imgTrangThai;
    private TextView tvTrangThai;
    private TextView tvNoiDung;
    private Record record;
    private SQLiteRecord sqLiteRecord;
    private FloatingActionButton fabDaKham;
    private FloatingActionButton fabChinhSua;
    private SimpleDateFormat dfDay = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat dfHour = new SimpleDateFormat("HH:mm");
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_record);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Chi tiết lịch hẹn");
        context = this;
        initComponent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void initComponent()
    {
        sqLiteRecord = new SQLiteRecord(this);
        tvTieuDe = (TextView) findViewById(R.id.tvTieuDe);
        tvBenhNhan = (TextView) findViewById(R.id.tvBenhNhan);
        tvNgay = (TextView) findViewById(R.id.tvNgay);
        tvGio = (TextView) findViewById(R.id.tvGio);
        imgTrangThai = (ImageView) findViewById(R.id.imgTrangThai);
        tvTrangThai = (TextView) findViewById(R.id.tvTrangThai);
        tvNoiDung = (TextView) findViewById(R.id.tvNoiDung);
        fabDaKham = (FloatingActionButton) findViewById(R.id.fabDaKham);
        fabChinhSua = (FloatingActionButton) findViewById(R.id.fabChinhSua);
        record = (Record) getIntent().getSerializableExtra("Record");

        tvTieuDe.setText(record.getTieuDe());
        tvBenhNhan.setText(record.getBenhNhan().getTenBenhNhan());
        tvNgay.setText(dfDay.format(record.getNgayGio()));
        tvGio.setText(dfHour.format(record.getNgayGio()));

        if(record.isDaKham())
        {
            imgTrangThai.setImageResource(R.drawable.green_check);
            tvTrangThai.setText("Đã khám");
        }
        else
        {
            imgTrangThai.setImageResource(R.drawable.yellow_mark);
            tvTrangThai.setText("Chưa khám");
        }
        tvNoiDung.setText(record.getNoiDung());


        fabDaKham.setOnClickListener(new FloatingActionButton.OnClickListener() {

            @Override
            public void onClick(View view) {
                record.setDaKham(true);
                sqLiteRecord.updateRecord(record);
                finish();
            }
        });

        fabChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChiTietRecordActivity.this,ThemRecordActivity.class);
                //Truyen vao ma chi tieu cho Activity Chi Tiet Hoat Dong
                intent.putExtra("Record",record);
                intent.putExtra("Them", false);
                startActivityForResult(intent, REQUEST_CODE_VIEW);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_VIEW && resultCode == RESULT_OK) {
            record = (Record) data.getSerializableExtra("Record");
            if (record != null) {
                tvTieuDe.setText(record.getTieuDe());
                tvBenhNhan.setText(record.getBenhNhan().getTenBenhNhan());
                tvNgay.setText(dfDay.format(record.getNgayGio()));
                tvGio.setText(dfHour.format(record.getNgayGio()));

                if(record.isDaKham())
                {
                    imgTrangThai.setImageResource(R.drawable.green_check);
                    tvTrangThai.setText("Đã khám");
                }
                else
                {
                    imgTrangThai.setImageResource(R.drawable.yellow_mark);
                    tvTrangThai.setText("Chưa khám");
                }
                tvNoiDung.setText(record.getNoiDung());

            }
        }

    }

}
