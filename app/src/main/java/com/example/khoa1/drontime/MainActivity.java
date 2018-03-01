package com.example.khoa1.drontime;

import android.app.SearchManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.khoa1.drontime.Adapter.LeftDrawerListViewAdapter;
import com.example.khoa1.drontime.Database.SQLiteDataController;
import com.example.khoa1.drontime.Model.LeftDrawerListViewModel;
import com.example.khoa1.drontime.Service.ScheduleService;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView mDrawerList;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Handler handerCreateLeftDrawer;
    private Handler handlerSetDataListview;
    private Toolbar toolbar;

    private final int MSG_SET_DATA_SUCCESS = 1;
    private final int MSG_SET_DATA_LEFT_DRAWER_SUCCESS = 2;
    private String[] mNavigationDrawerItemTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, ScheduleService.class);
        startService(intent);
        //kiểm tra xem đã có csdl chưa
        setCheckIfHaveDatabase();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LeftDrawerListViewModel[] drawerItem = new LeftDrawerListViewModel[3];

        drawerItem[0] = new LeftDrawerListViewModel(R.drawable.home, "Tổng quan");
        drawerItem[1] = new LeftDrawerListViewModel(R.drawable.patient, "Bệnh nhân");
        drawerItem[2] = new LeftDrawerListViewModel(R.drawable.calendar, "Lịch hẹn");

        mNavigationDrawerItemTitles = new String[3];
        mNavigationDrawerItemTitles[0] = "Tổng quan";
        mNavigationDrawerItemTitles[1] = "Bệnh nhân";
        mNavigationDrawerItemTitles[2] = "Lịch hẹn";
        LeftDrawerListViewAdapter adapter = new LeftDrawerListViewAdapter(MainActivity.this,
                R.layout.left_drawer_custom_listview, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        //Setup drawer layout
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Setup toggle button
        drawer.addDrawerListener(toggle);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        //Fragment fragment = new BenhNhanFragment();
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft= fragmentManager.beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setOnClickListener();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Fragment fragment = new SearchBenhNhanFragment();
                Bundle bundle = new Bundle();
                bundle.putString("query", newText);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }
    private final int POSITION_OVERVIEW = 0;
    private final int POSITION_PATIENT = 1;
    private final int POSITION_RECORD = 2;

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case POSITION_OVERVIEW:
                fragment = new HomeFragment();
                break;
            case POSITION_PATIENT:
                fragment = new BenhNhanFragment();
                break;
            case POSITION_RECORD:
                fragment = new RecordFragment();
               break;
            default:
                break;
        }

        if (fragment != null) {
            //Khi fragment được chọn
            //Dùng fragmentManager chuyển sang fragment được chọn
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            drawer.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private void setCheckIfHaveDatabase() {
                SQLiteDataController sql = new SQLiteDataController(MainActivity.this);
                try {
                    sql.isCreatedDatabase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }


}
