package com.example.khoa1.drontime.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.khoa1.drontime.ChiTietRecordActivity;
import com.example.khoa1.drontime.MainActivity;
import com.example.khoa1.drontime.R;

/**
 * Created by khoa1 on 12/18/2017.
 */

public class ScheduleService extends Service{


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }
}
