package com.example.khoa1.drontime.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.example.khoa1.drontime.Model.BenhNhan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by khoa1 on 12/15/2017.
 */

public class SQLiteBenhNhan extends SQLiteDataController{

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public SQLiteBenhNhan(Context con) {
        super(con);
    }

    public ArrayList<BenhNhan> getListBenhNhan(){
        ArrayList<BenhNhan> ListAccount= new ArrayList<BenhNhan>();
        try{
            openDataBase();
            Cursor cs = database.rawQuery("SELECT *\n" +
                            "FROM BenhNhan\n"
                    ,null);
            BenhNhan benhNhan;
            while (cs.moveToNext()) {
                benhNhan = new BenhNhan(cs.getInt(0), cs.getString(1), cs.getString(2),
                       df.parse(cs.getString(3)) ,cs.getInt(4) > 0, cs.getString(5),
                        cs.getString(6), cs.getString(7), cs.getInt(8)>0);
                Log.d("aaa",cs.getString(0));
                ListAccount.add(benhNhan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return ListAccount;
    }

    public boolean isExistPatient(){
        boolean result = false;
        try{
            openDataBase();
            Cursor cs = database.rawQuery("SELECT count(MaBenhNhan)\n" +
                            "FROM BenhNhan\n"
                    ,null);
            while (cs.moveToNext())
            result = cs.getInt(0) > 0;
            }

         finally {
            close();
        }
        return result;
    }

    public BenhNhan getBenhNhanByID(int ID)
    {
        BenhNhan benhNhan = null;
        try{
            openDataBase();
            Cursor cs = database.rawQuery("SELECT *\n" +
                            "FROM BenhNhan\n"+
                    "WHERE BenhNhan.MaBenhNhan = " + String.valueOf(ID)
                    ,null);
            while (cs.moveToNext()) {
                benhNhan = new BenhNhan(cs.getInt(0), cs.getString(1), cs.getString(2),
                        df.parse(cs.getString(3)) ,cs.getInt(4) > 0, cs.getString(5),
                        cs.getString(6), cs.getString(7), cs.getInt(8)>0);
                Log.d("aaa",cs.getString(0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return benhNhan;
    }
    public boolean addBenhNhan(BenhNhan benhNhan){
        boolean result = false;
        try {

            openDataBase();
            int trangThai = 0;
            if(benhNhan.isTrangThai())
                trangThai = 1;
            ContentValues values = new ContentValues();
            values.put("TenBenhNhan", benhNhan.getTenBenhNhan());
            values.put("HinhAnh", benhNhan.getHinhAnh());
            values.put("NgaySinh", df.format(benhNhan.getNgaySinh()));
            values.put("GioiTinh", benhNhan.isGioiTinh());
            values.put("SoDienThoai", benhNhan.getSoDienThoai());
            values.put("Email", benhNhan.getEmail());
            values.put("DiaChi", benhNhan.getDiaChi());
            values.put("TrangThai", trangThai);
            //  values.put("SoTienNo", account.getAccountType().getId());
            long rs = database.insert("BenhNhan", null, values);
            if (rs > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        Log.d("aaa","finish");
        return result;
    }
    public boolean updateBenhNhan(BenhNhan benhNhan){
        boolean result = false;
        try {

            openDataBase();
            int trangThai = (benhNhan.isTrangThai()) ? 1 : 0;
            int gioiTinh = (benhNhan.isGioiTinh()) ? 1 : 0;
            ContentValues values = new ContentValues();
            values.put("TenBenhNhan", benhNhan.getTenBenhNhan());
            values.put("HinhAnh", benhNhan.getHinhAnh());
            values.put("NgaySinh", df.format(benhNhan.getNgaySinh()));
            values.put("GioiTinh", gioiTinh);
            values.put("SoDienThoai", benhNhan.getSoDienThoai());
            values.put("Email", benhNhan.getEmail());
            values.put("DiaChi", benhNhan.getDiaChi());
            values.put("TrangThai", trangThai);
            int rs = database.update("BenhNhan",values,"MaBenhNhan="+ benhNhan.getMaBenhNhan(),null);
            if (rs > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        Log.d("aaa","finish");
        return result;
    }


    public boolean deleteBenhNhan(int id){
        boolean result = false;
        try {

            openDataBase();
            //
            int rs = database.delete("BenhNhan", "MaBenhNhan=" + id, null);
            if (rs > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

}
