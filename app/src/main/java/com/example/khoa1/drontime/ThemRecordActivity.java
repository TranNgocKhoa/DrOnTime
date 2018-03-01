package com.example.khoa1.drontime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.khoa1.drontime.Adapter.RecordAdapter;
import com.example.khoa1.drontime.Database.SQLiteRecord;
import com.example.khoa1.drontime.Model.BenhNhan;
import com.example.khoa1.drontime.Model.Record;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.khoa1.drontime.Util.DateTimeUtil.dfDay;
import static com.example.khoa1.drontime.Util.DateTimeUtil.dfHour;

public class ThemRecordActivity extends AppCompatActivity {
    private EditText edTieuDe;
    private TextView tvChonBenhNhan;
    private TextView tvChonNgay;
    private TextView tvChonGio;
    private CheckBox checkBoxTrangThai;
    private EditText edNoiDung;
    private SQLiteRecord sqLiteRecord;
    private Record record;
    private BenhNhan benhNhan;
    private Calendar myCalendar;
    private boolean them = true;

    private static final int REQUEST_BENHNHAN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_record);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Thêm lịch hẹn");
        initComponent();

        //Set back toolbar button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveMenuClick() {
        //Todo: them handle cho nut save
        if(benhNhan == null)
        {
            Toast.makeText(this, "Chưa chọn bệnh nhân", Toast.LENGTH_SHORT).show();
        }
       else {
            String tieuDe = edTieuDe.getText().toString();
            String noiDung = edNoiDung.getText().toString();
            Date ngayGio = myCalendar.getTime();
            boolean trangThai = checkBoxTrangThai.isChecked();
            if (them) {
                record = new Record(0, tieuDe, noiDung, ngayGio, benhNhan, trangThai);
                sqLiteRecord.addRecord(record);
            } else {
                record = new Record(record.getMaRecord(), tieuDe, noiDung, ngayGio, benhNhan, trangThai);
                sqLiteRecord.updateRecord(record);
            }
            setResult(RESULT_OK, new Intent().putExtra("Record", record));
            this.finish();
        }
    }

    private void initComponent()
    {
         edTieuDe = (EditText) findViewById(R.id.edTieuDe);
         tvChonBenhNhan = (TextView) findViewById(R.id.tvChonBenhNhan);
         tvChonNgay = (TextView) findViewById(R.id.tvChonNgay);
         tvChonGio = (TextView) findViewById(R.id.tvChonGio);
         checkBoxTrangThai = (CheckBox) findViewById(R.id.checkBoxTrangThai);
         edNoiDung = (EditText) findViewById(R.id.edNoiDung);
        sqLiteRecord = new SQLiteRecord(this);
        //Khoi tao Calendar
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(myCalendar.getTime());

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.HOUR, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                tvChonGio.setText(dfHour.format(myCalendar.getTime()));
            }
        };

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                tvChonNgay.setText(dfDay.format(myCalendar.getTime()));
            }

        };

        tvChonNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ThemRecordActivity.this, date , myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tvChonGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(ThemRecordActivity.this, time,
                        myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        tvChonBenhNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThemRecordActivity.this, ChonBenhNhanActivity.class);
                startActivityForResult(intent, REQUEST_BENHNHAN);
            }
        });

        record = (Record) getIntent().getSerializableExtra("Record");
        if(record != null)
        {
            setTitle("Chỉnh sửa lịch hẹn");
            them = false;
            edTieuDe.setText(record.getTieuDe());
            benhNhan = record.getBenhNhan();
            tvChonNgay.setText(dfDay.format(record.getNgayGio()));
            tvChonGio.setText(dfHour.format(record.getNgayGio()));
            checkBoxTrangThai.setChecked(record.isDaKham());
            edNoiDung.setText(record.getNoiDung());
            myCalendar.setTime(record.getNgayGio());
        }
        if(benhNhan == null) {
            benhNhan = (BenhNhan) getIntent().getSerializableExtra("BenhNhan");
        }
        if(benhNhan != null)
        {
            tvChonBenhNhan.setText(benhNhan.getTenBenhNhan());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BENHNHAN) {
            if(resultCode == RESULT_OK) {
                benhNhan = (BenhNhan) data.getSerializableExtra("BenhNhan");
                if(benhNhan != null)
                {
                    tvChonBenhNhan.setText(benhNhan.getTenBenhNhan());
                }
            }
        }

    }
}
