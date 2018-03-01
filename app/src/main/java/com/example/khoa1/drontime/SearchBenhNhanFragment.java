package com.example.khoa1.drontime;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.khoa1.drontime.Adapter.BenhNhanAdapter;
import com.example.khoa1.drontime.Database.SQLiteBenhNhan;
import com.example.khoa1.drontime.Model.BenhNhan;
import com.example.khoa1.drontime.Util.BenhNhanUtil;

import java.util.ArrayList;


public class SearchBenhNhanFragment extends Fragment {

    public SearchBenhNhanFragment() {
        // Required empty public constructor
    }

    private ListView lvBenhNhan;
    private Handler handlerSetDataListview;
    private static final int MSG_SET_DATA_SUCCESS = 1;
    private static final int REQUEST_CODE_VIEW = 2;
    String query;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        query = getArguments().getString("query");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_benh_nhan, container, false);
        lvBenhNhan = (ListView) rootView.findViewById(R.id.lvBenhNhan);
        setDataListView();
        handlerSetDataListview = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_SET_DATA_SUCCESS) {
                    //BenhNhanAdapter benhNhanAdapter = new BenhNhanAdapter(getActivity(), R.layout.custom_listview_benh_nhan,
                    //(ArrayList<BenhNhan>) msg.obj);
                    lvBenhNhan.setAdapter((BenhNhanAdapter)msg.obj);
                    lvBenhNhan.setOnItemClickListener(new SearchBenhNhanFragment.ListViewOnItemClickListener());
                }
            }
        };

        return rootView;

    }

    private void setDataListView() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<BenhNhan> arrBenhNhan = BenhNhanUtil.searchBenhNhan(query, getContext());
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
