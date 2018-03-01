package com.example.khoa1.drontime.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by khoa1 on 12/14/2017.
 */

public class Record implements Serializable{
    private int maRecord;
    private String tieuDe;
    private String noiDung;
    private Date ngayGio;
    private BenhNhan benhNhan;
    private boolean daKham;


    public Record(int maRecord, String tieuDe, String noiDung, Date ngayGio, BenhNhan benhNhan, boolean daKham) {
        this.maRecord = maRecord;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.ngayGio = ngayGio;
        this.benhNhan = benhNhan;
        this.daKham = daKham;
    }

    public int getMaRecord() {
        return maRecord;
    }

    public void setMaRecord(int maRecord) {
        this.maRecord = maRecord;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Date getNgayGio() {
        return ngayGio;
    }

    public void setNgayGio(Date ngayGio) {
        this.ngayGio = ngayGio;
    }

    public BenhNhan getBenhNhan() {
        return benhNhan;
    }

    public void setBenhNhan(BenhNhan benhNhan) {
        this.benhNhan = benhNhan;
    }

    public boolean isDaKham() {
        return daKham;
    }

    public void setDaKham(boolean daKham) {
        this.daKham = daKham;
    }
}
