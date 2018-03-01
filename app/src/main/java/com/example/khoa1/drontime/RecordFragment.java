package com.example.khoa1.drontime;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.khoa1.drontime.Adapter.BenhNhanAdapter;
import com.example.khoa1.drontime.Adapter.RecordAdapter;
import com.example.khoa1.drontime.Database.SQLiteBenhNhan;
import com.example.khoa1.drontime.Database.SQLiteRecord;
import com.example.khoa1.drontime.Model.BenhNhan;
import com.example.khoa1.drontime.Model.Record;

import java.util.ArrayList;
import java.util.Calendar;



public class RecordFragment extends Fragment {
    private static final int REQUEST_CODE_VIEW = 22;
    private ListView lvRecord;
    private FloatingActionButton fabThemRecord;
    private SQLiteRecord sqLiteRecord;
    private SQLiteBenhNhan sqLiteBenhNhan;
    private Handler handlerSetDataListview;
    private static final int MSG_SET_DATA_SUCCESS = 1;
    public RecordFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_record, container, false);
        sqLiteRecord = new SQLiteRecord(getContext());
        sqLiteBenhNhan = new SQLiteBenhNhan(getContext());
        lvRecord = (ListView) rootView.findViewById(R.id.lvRecord);
        registerForContextMenu(lvRecord);
        fabThemRecord = (FloatingActionButton) rootView.findViewById(R.id.fabThemRecord);
        fabThemRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sqLiteBenhNhan.isExistPatient()) {
                    Intent intent = new Intent(getContext(), ThemRecordActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_VIEW);
                }
                else
                {
                    Toast.makeText(getContext(), "Vui lòng thêm bệnh nhân trước khi đặt lịch hẹn!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), ThemBenhNhanActivity.class);
                    startActivity(intent);
                }
            }
        });

        setDataListView();
        handlerSetDataListview = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_SET_DATA_SUCCESS) {
                    //BenhNhanAdapter benhNhanAdapter = new BenhNhanAdapter(getActivity(), R.layout.custom_listview_benh_nhan,
                    //(ArrayList<BenhNhan>) msg.obj);
                    lvRecord.setAdapter((RecordAdapter)msg.obj);
                    lvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(getContext(), ChiTietRecordActivity.class);
                            intent.putExtra("Record",(Record) adapterView.getItemAtPosition(i));
                            startActivityForResult(intent, REQUEST_CODE_VIEW);
                        }
                    });
                }
            }
        };
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setDataListView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Lựa chọn:");
        //groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "Xem");
        menu.add(0, v.getId(), 0, "Sửa");
        menu.add(0, v.getId(), 0, "Xoá");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        //Lay vi tri click
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        if(item.getTitle()=="Xem"){
            Intent intent = new Intent(getActivity().getApplicationContext(),ChiTietRecordActivity.class);
            //Truyen vao ma chi tieu cho Activity Chi Tiet Hoat Dong
            intent.putExtra("Record",(Record)lvRecord.getItemAtPosition(index));
            startActivityForResult(intent, REQUEST_CODE_VIEW);
        }
        else if(item.getTitle()=="Sửa"){
            Intent intent = new Intent(getActivity().getApplicationContext(),ThemRecordActivity.class);
            //Truyen vao ma chi tieu cho Activity Chi Tiet Hoat Dong
            intent.putExtra("Record",((Record)lvRecord.getItemAtPosition(index)));
            intent.putExtra("Them", false);
            startActivityForResult(intent, REQUEST_CODE_VIEW);
            Toast.makeText(getContext(),"Sửa",Toast.LENGTH_LONG).show();
        }
        else if(item.getTitle()=="Xoá"){
            sqLiteRecord.deleteRecord(((Record)lvRecord.getItemAtPosition(index)).getMaRecord());
            setDataListView();
            Toast.makeText(getContext(),"Xoá",Toast.LENGTH_LONG).show();
        }
        else{
            return false;
        }
        return true;
    }

    private void setDataListView() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Record> arrRecord = sqLiteRecord.getListRecord();
                RecordAdapter recordAdapter = new RecordAdapter(getActivity(), R.layout.custom_listview_record,
                        arrRecord);
                //lấy message từ Main thread
                Message msg = handlerSetDataListview.obtainMessage();
                msg.what = MSG_SET_DATA_SUCCESS;
                msg.obj = recordAdapter;
                //gửi lại Message này về cho Main Thread
                handlerSetDataListview.sendMessage(msg);
            }
        });
        //kích hoạt tiến trình
        th.start();
    }
}
