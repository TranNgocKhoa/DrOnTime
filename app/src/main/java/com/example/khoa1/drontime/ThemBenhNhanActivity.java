package com.example.khoa1.drontime;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khoa1.drontime.Database.SQLiteBenhNhan;
import com.example.khoa1.drontime.Model.BenhNhan;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ThemBenhNhanActivity extends AppCompatActivity {

    private ImageView imgHinhAnh;
    private ImageButton imgButtonChupHinh;
    private EditText edTenBenhNhan;
    private TextView tvNgaySinh;
    private EditText edSoDienThoai;
    private EditText edEmail;
    private CheckBox checkBoxGioiTinh;
    private EditText edDiaChi;
    private SQLiteBenhNhan sqLiteBenhNhan;
    private BenhNhan benhNhan;
    private Calendar myCalendar;
    private String imgPath;
    private boolean them = true;
    private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_benh_nhan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Thêm bệnh nhân");
        //Set back toolbar button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initComponent();
        //Khoi tao doi tuong SQLiteBenhNhan
        sqLiteBenhNhan = new SQLiteBenhNhan(this);



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tvNgaySinh.setText(df.format(myCalendar.getTime()));
            }

        };
        tvNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ThemBenhNhanActivity.this, date , myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });







    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_button_toolbar, menu);
        // Action View
        //MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners
        //return super.onCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId()) {
            case  android.R.id.home:
                this.finish();
                return true;
            case R.id.save_button:
                saveMenuClick();
                this.finish();
                return true;
            case R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveMenuClick() {
        //Todo: them handle cho nut save
        String tenBenhNhan = edTenBenhNhan.getText().toString();
        Date ngaySinh = myCalendar.getTime();
        boolean gioiTinh = checkBoxGioiTinh.isChecked();
        String soDienThoai = edSoDienThoai.getText().toString();
        String email = edEmail.getText().toString();
        String diaChi = edDiaChi.getText().toString();
        if (imgPath == null || imgPath.length() == 0)
        {
            if(benhNhan != null)
            imgPath = benhNhan.getHinhAnh();
            else
                imgPath = null;
        }
        if(them) {
            benhNhan = new BenhNhan(0, tenBenhNhan, imgPath, ngaySinh, gioiTinh, soDienThoai,
                    email, diaChi, true);
            sqLiteBenhNhan.addBenhNhan(benhNhan);
        }
        else {
            benhNhan = new BenhNhan(benhNhan.getMaBenhNhan(), tenBenhNhan, imgPath, ngaySinh, gioiTinh, soDienThoai,
                    email, diaChi, benhNhan.isTrangThai());
            sqLiteBenhNhan.updateBenhNhan(benhNhan);
        }

        setResult(RESULT_OK, new Intent().putExtra("BenhNhan", benhNhan));
    }

    private void initComponent()
    {
        //Khoi tao Calendar
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(myCalendar.getTime());
        imgHinhAnh = (ImageView) findViewById(R.id.imgHinhAnh);
        imgButtonChupHinh = (ImageButton) findViewById(R.id.imgButtonChupHinh);
        edTenBenhNhan = (EditText) findViewById(R.id.edTenBenhNhan);
        tvNgaySinh = (TextView) findViewById(R.id.tvNgaySinh);
        edSoDienThoai = (EditText) findViewById(R.id.edSoDienThoai);
        edEmail = (EditText) findViewById(R.id.edEmail);
        checkBoxGioiTinh = (CheckBox) findViewById(R.id.checkBoxGioiTinh);
        edDiaChi = (EditText) findViewById(R.id.edDiaChi);
        benhNhan = (BenhNhan) getIntent().getSerializableExtra("BenhNhan");
        if(benhNhan!=null) {
            them = false;
            setTitle("Chỉnh sửa bệnh nhân");
            edTenBenhNhan.setText(benhNhan.getTenBenhNhan());
            tvNgaySinh.setText(df.format(benhNhan.getNgaySinh()));
            myCalendar.setTime(benhNhan.getNgaySinh());
            edSoDienThoai.setText(benhNhan.getSoDienThoai());
            edEmail.setText(benhNhan.getEmail());
            edDiaChi.setText(benhNhan.getDiaChi());
            checkBoxGioiTinh.setChecked(benhNhan.isGioiTinh());
            if(benhNhan.getHinhAnh() != null && benhNhan.getHinhAnh().length()>0)
            {
                imgPath = benhNhan.getHinhAnh();
                File imgFile = new  File(imgPath);
                if(imgFile.exists()){
                    Picasso.with(this).load(imgFile).resize(250, 250).centerCrop().into(imgHinhAnh);
                }
            }
        }

        imgButtonChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThemBenhNhanActivity.this, CameraActivity.class);
                startActivityForResult(intent, 3);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3)
        {
            if(resultCode == RESULT_OK) {
                String imgString = data.getStringExtra("Image");
                if(imgString != null)
                {
                    imgPath = imgString;

                    File imgFile = new  File(imgString);

                    if(imgFile.exists()){
                        //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        //imgHinhAnh.setImageBitmap(myBitmap);
//                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        Picasso.with(this).load(imgFile).resize(250, 250).centerCrop().into(imgHinhAnh);

                    }
                }

            }
        }
    }
}
