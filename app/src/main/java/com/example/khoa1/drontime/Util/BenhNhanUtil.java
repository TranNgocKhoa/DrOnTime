package com.example.khoa1.drontime.Util;

import android.content.Context;

import com.example.khoa1.drontime.Database.SQLiteBenhNhan;
import com.example.khoa1.drontime.Model.BenhNhan;

import java.util.ArrayList;

/**
 * Created by khoa1 on 12/18/2017.
 */

public class BenhNhanUtil {
    public static ArrayList<BenhNhan> searchBenhNhan(String dataString, Context context)
    {
        SQLiteBenhNhan sqLiteBenhNhan = new SQLiteBenhNhan(context);
        ArrayList<BenhNhan>  arrayList = sqLiteBenhNhan.getListBenhNhan();
        ArrayList<BenhNhan> result = new ArrayList<>();
        for (BenhNhan benhNhan: arrayList
             ) {
            if(benhNhan.joinAllField().toLowerCase().contains(dataString.toLowerCase()))
            {
                result.add(benhNhan);
            }

        }
        return  result;
    }
}
