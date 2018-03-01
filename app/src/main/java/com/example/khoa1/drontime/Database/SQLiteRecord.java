package com.example.khoa1.drontime.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.khoa1.drontime.Model.BenhNhan;
import com.example.khoa1.drontime.Model.Record;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by khoa1 on 12/15/2017.
 */

public class SQLiteRecord extends SQLiteDataController{

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SQLiteBenhNhan sqLiteBenhNhan;
    public SQLiteRecord(Context con)
    {
        super(con);
        sqLiteBenhNhan = new SQLiteBenhNhan(con);
    }

    public ArrayList<Record> getListRecord(){
        ArrayList<Record> ListRecord = new ArrayList<Record>();
        try{
            openDataBase();
            Cursor cs = database.rawQuery("SELECT *\n" +
                            "FROM Record\n"
                    ,null);
            Record record;
            while (cs.moveToNext()) {
                record = new Record(cs.getInt(0), cs.getString(1), cs.getString(2),
                        df.parse(cs.getString(3)) ,
                        sqLiteBenhNhan.getBenhNhanByID(cs.getInt(4)), cs.getInt(5) > 0);
                Log.d("aaa",cs.getString(0));
                ListRecord.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        Collections.sort(ListRecord, new Comparator<Record>() {
            boolean isToday(Date time) {
                Calendar now = Calendar.getInstance();
                Calendar timeToCheck = Calendar.getInstance();
                timeToCheck.setTime(time);
                return now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)
                        && now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR);
            }
            @Override
            public int compare(Record record, Record otherRecord) {
                if(isToday(record.getNgayGio()) && !isToday(otherRecord.getNgayGio())) return - 99;
                if(!isToday(record.getNgayGio()) && isToday(otherRecord.getNgayGio())) return 99;
                return record.getNgayGio().compareTo(otherRecord.getNgayGio());
            }
        });
        return ListRecord;
    }

    public ArrayList<Record> getListRecordByBenhNhan(int ID){
        ArrayList<Record> ListRecord = new ArrayList<Record>();
        try{
            openDataBase();
            Cursor cs = database.rawQuery("SELECT *\n" +
                            "FROM Record\n where Record.MaBenhNhan = " + ID
                    ,null);
            Record record;
            while (cs.moveToNext()) {
                record = new Record(cs.getInt(0), cs.getString(1), cs.getString(2),
                        df.parse(cs.getString(3)) ,
                        sqLiteBenhNhan.getBenhNhanByID(cs.getInt(4)), cs.getInt(5) > 0);
                Log.d("aaa",cs.getString(0));
                ListRecord.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        Collections.sort(ListRecord, new Comparator<Record>() {
            boolean isToday(Date time) {
                Calendar now = Calendar.getInstance();
                Calendar timeToCheck = Calendar.getInstance();
                timeToCheck.setTime(time);
                return now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)
                        && now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR);
            }
            @Override
            public int compare(Record record, Record otherRecord) {
                if(isToday(record.getNgayGio()) && !isToday(otherRecord.getNgayGio())) return - 99;
                if(!isToday(record.getNgayGio()) && isToday(otherRecord.getNgayGio())) return 99;
                return record.getNgayGio().compareTo(otherRecord.getNgayGio());
            }
        });
        return ListRecord;
    }

    public Record getNextMeeting(){
        Record record = null;
        try{
            openDataBase();
            Cursor cs = database.rawQuery("select *\n" +
                            "from Record\n" +
                            "where NgayGio > datetime('now', 'localtime') and DaKham = 0\n" +
                            "order by NgayGio asc\n" +
                            "limit 1"
                    ,null);

            while (cs.moveToNext()) {
                record = new Record(cs.getInt(0), cs.getString(1), cs.getString(2),
                        df.parse(cs.getString(3)) ,
                        sqLiteBenhNhan.getBenhNhanByID(cs.getInt(4)), cs.getInt(5) > 0);
                Log.d("aaa",cs.getString(0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            close();
        }


        return record;
    }

    public ArrayList<Integer> getOverViewToDay(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        try{
            openDataBase();
            Cursor cs = database.rawQuery("select count(MaRecord)\n" +
                            "from Record\n" +
                            "where date(NgayGio) == date('now', 'localtime')\n"
                    ,null);
            int tong = 0;
            while (cs.moveToNext()) {
                tong = cs.getInt(0);
                Log.d("aaa",cs.getString(0));
            }
            arrayList.add(tong);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        try{
            openDataBase();
            Cursor cs = database.rawQuery("select count(MaRecord)\n" +
                            "from Record\n" +
                            "where date(NgayGio) == date('now', 'localtime') and DaKham = 1\n"
                    ,null);
            int dakham = 0;
            while (cs.moveToNext()) {
                dakham = cs.getInt(0);
                Log.d("aaa",cs.getString(0));
            }
            arrayList.add(dakham);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }

        return arrayList;
    }

    public boolean addRecord(Record record){
        boolean result = false;
        try {

            openDataBase();
            int daKham = 0;
            if(record.isDaKham())
                daKham = 1;
            ContentValues values = new ContentValues();
            values.put("TieuDe", record.getTieuDe());
            values.put("NoiDung", record.getNoiDung());
            values.put("NgayGio", df.format(record.getNgayGio()));
            values.put("MaBenhNhan", record.getBenhNhan().getMaBenhNhan());
            values.put("DaKham", daKham);

            long rs = database.insert("Record", null, values);
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

    public boolean updateRecord(Record record){
        boolean result = false;
        try {

            openDataBase();
            int daKham = 0;
            if(record.isDaKham())
                daKham = 1;
            ContentValues values = new ContentValues();
            values.put("TieuDe", record.getTieuDe());
            values.put("NoiDung", record.getNoiDung());
            values.put("NgayGio", df.format(record.getNgayGio()));
            values.put("MaBenhNhan", record.getBenhNhan().getMaBenhNhan());
            values.put("DaKham", daKham);

            long rs = database.update("Record", values, "MaRecord = "+ record.getMaRecord(),null);
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

    public boolean deleteRecord(int ID){
        boolean result = false;
        try {

            openDataBase();
            int rs = database.delete("Record", "MaRecord=" + ID, null);
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
            ContentValues values = new ContentValues();
            values.put("TenBenhNhan", benhNhan.getTenBenhNhan());
            values.put("HinhAnh", benhNhan.getHinhAnh());
            values.put("NgaySinh", df.format(benhNhan.getNgaySinh()));
            values.put("GioiTinh", benhNhan.isGioiTinh());
            values.put("SoDienThoai", benhNhan.getSoDienThoai());
            values.put("Email", benhNhan.getEmail());
            values.put("DiaChi", benhNhan.getDiaChi());
            values.put("TrangThai", benhNhan.isTrangThai());
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
