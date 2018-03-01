package com.example.khoa1.drontime;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khoa1.drontime.Adapter.RecordAdapter;
import com.example.khoa1.drontime.Database.SQLiteRecord;
import com.example.khoa1.drontime.Model.BenhNhan;
import com.example.khoa1.drontime.Model.Record;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChiTietBenhNhanActivity extends AppCompatActivity {
    private ImageView imgHinhAnh;
    private TextView tvTenBenhNhan;
    private TextView tvNgaySinh;
    private ImageView imgGioiTinh;
    private TextView tvGioiTinh;
    private TextView tvDiaChi;
    private TextView tvEmail;
    private TextView tvSoDienThoai;
    private ListView listViewLichHen;
    private Handler handlerSetDataListview;
    private FloatingActionButton fabChinhSua;
    private FloatingActionButton fabThemRecord;
    private static final int MSG_SET_DATA_SUCCESS = 11;
    public static final int REQUEST_EDIT = 22;
    public static final int REQUEST_ADD_RECORD = 23;
    private BenhNhan benhNhan;
    private SQLiteRecord sqLiteRecord;
    private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_benh_nhan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Chi tiết bệnh nhân");
        initComponent();
        Intent intent = getIntent();
        benhNhan = (BenhNhan) intent.getSerializableExtra("BenhNhan");
        if (benhNhan != null) {
            setThongTinBenhNhan();
        }
        setDataListView();
        handlerSetDataListview = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_SET_DATA_SUCCESS) {
                    RecordAdapter recordAdapter = new RecordAdapter(ChiTietBenhNhanActivity.this, R.layout.custom_listview_record,
                            (ArrayList<Record>) msg.obj);
                    listViewLichHen.setAdapter(recordAdapter);
                    listViewLichHen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (l < 0) return;
                            Intent intent = new Intent(ChiTietBenhNhanActivity.this, ChiTietRecordActivity.class);
                            intent.putExtra("Record", (Record) adapterView.getItemAtPosition(i));
                            startActivityForResult(intent, REQUEST_EDIT);
                        }
                    });

                }
            }
        };

        imgHinhAnh = (ImageView) findViewById(R.id.imgHinhAnh);
        Picasso.with(this).load(R.drawable.no_photo).resize(300, 300).centerCrop().into(imgHinhAnh);
        if (benhNhan.getHinhAnh() != null && benhNhan.getHinhAnh().length() != 0) {

            File imgFile = new File(benhNhan.getHinhAnh());

            if (imgFile.exists()) {

                Picasso.with(this).load(imgFile).resize(300, 300).centerCrop().into(imgHinhAnh);

            }
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

    private void setDataListView() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Record> arrRecord = sqLiteRecord.getListRecordByBenhNhan(benhNhan.getMaBenhNhan());
                //lấy message từ Main thread
                Message msg = handlerSetDataListview.obtainMessage();
                msg.what = MSG_SET_DATA_SUCCESS;
                msg.obj = arrRecord;
                //gửi lại Message này về cho Main Thread
                handlerSetDataListview.sendMessage(msg);
            }
        });
        //kích hoạt tiến trình
        th.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setDataListView();
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            benhNhan = (BenhNhan) data.getSerializableExtra("BenhNhan");
            if (benhNhan != null) {
                setThongTinBenhNhan();
                setDataListView();
            }
        }

    }

    private void initComponent() {
        listViewLichHen = (ListView) findViewById(R.id.listViewLichHen);
        View view = getLayoutInflater().inflate(R.layout.header_for_chi_tiet_benh_nhan, null, false);
        listViewLichHen.addHeaderView(view);
        sqLiteRecord = new SQLiteRecord(this);
        imgHinhAnh = (ImageView) findViewById(R.id.imgHinhAnh);
        tvTenBenhNhan = (TextView) findViewById(R.id.tvTenBenhNhan);
        tvNgaySinh = (TextView) findViewById(R.id.tvCalendar);
        imgGioiTinh = (ImageView) findViewById(R.id.imgSex);
        tvGioiTinh = (TextView) findViewById(R.id.tvSex);
        tvDiaChi = (TextView) findViewById(R.id.tvDiaChi);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvSoDienThoai = (TextView) findViewById(R.id.tvPhone);
        fabChinhSua = (FloatingActionButton) findViewById(R.id.fabChinhSua);
        fabThemRecord = (FloatingActionButton) findViewById(R.id.fabThemRecord);
        //View header1 =  getLayoutInflater().inflate(R.layout.listheader, null, false);
        fabChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChiTietBenhNhanActivity.this, ThemBenhNhanActivity.class);
                intent.putExtra("BenhNhan", benhNhan);
                startActivityForResult(intent, REQUEST_EDIT);
            }
        });

        fabThemRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChiTietBenhNhanActivity.this, ThemRecordActivity.class);
                intent.putExtra("BenhNhan", benhNhan);
                startActivityForResult(intent, REQUEST_ADD_RECORD);
            }
        });
        tvSoDienThoai.setOnClickListener(new TextView.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if(tvSoDienThoai.getText().length() == 0) return;
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+tvSoDienThoai.getText().toString().trim()));
                startActivity(callIntent);
            }
        });
    }

    private void setThongTinBenhNhan()
    {
        //imgHinhAnh = (ImageView) findViewById(R.id.imgHinhAnh);
        tvTenBenhNhan.setText(benhNhan.getTenBenhNhan());
        tvNgaySinh.setText(df.format(benhNhan.getNgaySinh()));
        if(benhNhan.isGioiTinh())
        {
            imgGioiTinh.setImageResource(R.drawable.sex_male);
            tvGioiTinh.setText("Nam");
        }
        else
        {
            imgGioiTinh.setImageResource(R.drawable.sex_female);
            tvGioiTinh.setText("Nữ");
        }

        tvDiaChi.setText(benhNhan.getDiaChi());
        tvEmail.setText(benhNhan.getEmail());
        tvSoDienThoai.setText(benhNhan.getSoDienThoai());
        //listViewLichHen = (ListView)findViewById(R.id.listViewLichHen);
        if(benhNhan.getHinhAnh()!= null && benhNhan.getHinhAnh().length()!=0)
        {

            File imgFile = new  File(benhNhan.getHinhAnh());

            if(imgFile.exists()){

                Picasso.with(this).load(imgFile).resize(300, 300).centerCrop().into(imgHinhAnh);

            }
        }
    }


}
