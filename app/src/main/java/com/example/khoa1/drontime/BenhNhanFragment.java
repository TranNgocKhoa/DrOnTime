package com.example.khoa1.drontime;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.khoa1.drontime.Adapter.BenhNhanAdapter;
import com.example.khoa1.drontime.Database.SQLiteBenhNhan;
import com.example.khoa1.drontime.Model.BenhNhan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class BenhNhanFragment extends Fragment {
    public BenhNhanFragment() {
        // Required empty public constructor
    }

    private ListView lvBenhNhan;
    private FloatingActionButton fabThemBenhNhan;
    private SQLiteBenhNhan sqLiteBenhNhan;
    private Handler handlerSetDataListview;
    private static final int MSG_SET_DATA_SUCCESS = 1;
    private static final int REQUEST_CODE_VIEW = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_benh_nhan, container, false);
        setDataListView();

        lvBenhNhan = (ListView) rootView.findViewById(R.id.lvBenhNhan);
        registerForContextMenu(lvBenhNhan);
        fabThemBenhNhan = (FloatingActionButton) rootView.findViewById(R.id.fabThemBenhNhan);
        fabThemBenhNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ThemBenhNhanActivity.class);
                intent.putExtra("Them", true);
                startActivityForResult(intent, REQUEST_CODE_VIEW);
            }
        });
        handlerSetDataListview = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_SET_DATA_SUCCESS) {
                    //BenhNhanAdapter benhNhanAdapter = new BenhNhanAdapter(getActivity(), R.layout.custom_listview_benh_nhan,
                            //(ArrayList<BenhNhan>) msg.obj);
                    lvBenhNhan.setAdapter((BenhNhanAdapter)msg.obj);
                    lvBenhNhan.setOnItemClickListener(new ListViewOnItemClickListener());
                }
            }
        };
        return rootView;
    }

    private void setDataListView() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                sqLiteBenhNhan = new SQLiteBenhNhan(getContext());
                ArrayList<BenhNhan> arrBenhNhan = sqLiteBenhNhan.getListBenhNhan();
                BenhNhanAdapter benhNhanAdapter = new BenhNhanAdapter(getActivity(), R.layout.custom_listview_benh_nhan,
                        arrBenhNhan);
                //lấy message từ Main thread
                Message msg = handlerSetDataListview.obtainMessage();
                msg.what = MSG_SET_DATA_SUCCESS;
                msg.obj = benhNhanAdapter;
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
            Intent intent = new Intent(getActivity().getApplicationContext(),ChiTietBenhNhanActivity.class);
            //Truyen vao ma chi tieu cho Activity Chi Tiet Hoat Dong
            intent.putExtra("BenhNhan",(BenhNhan)lvBenhNhan.getItemAtPosition(index));
            startActivity(intent);
        }
        else if(item.getTitle()=="Sửa"){
            Intent intent = new Intent(getActivity().getApplicationContext(),ThemBenhNhanActivity.class);
            //Truyen vao ma chi tieu cho Activity Chi Tiet Hoat Dong
            intent.putExtra("BenhNhan",((BenhNhan)lvBenhNhan.getItemAtPosition(index)));
            intent.putExtra("Them", false);
            startActivityForResult(intent, REQUEST_CODE_VIEW);
            Toast.makeText(getContext(),"Sửa",Toast.LENGTH_LONG).show();
        }
        else if(item.getTitle()=="Xoá"){
            sqLiteBenhNhan.deleteBenhNhan(((BenhNhan)lvBenhNhan.getItemAtPosition(index)).getMaBenhNhan());
            setDataListView();
            Toast.makeText(getContext(),"Xoá",Toast.LENGTH_LONG).show();
        }
        else{
            return false;
        }
        return true;
    }
    private class ListViewOnItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity().getApplicationContext(), ChiTietBenhNhanActivity.class);
            //Truyen vao ma chi tieu cho Activity Chi Tiet Hoat Dong
            intent.putExtra("BenhNhan",((BenhNhan)adapterView.getItemAtPosition(i)));
            intent.putExtra("Them", true);
            startActivityForResult(intent, REQUEST_CODE_VIEW);
        }
    }

}
