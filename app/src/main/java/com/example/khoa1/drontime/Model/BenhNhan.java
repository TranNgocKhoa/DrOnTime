package com.example.khoa1.drontime.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by khoa1 on 12/14/2017.
 */

public class BenhNhan implements Serializable{

    private int maBenhNhan;
    private String tenBenhNhan;
    private String hinhAnh;
    private Date ngaySinh;
    private boolean gioiTinh;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private boolean trangThai;

    public BenhNhan(int maBenhNhan, String tenBenhNhan, String hinhAnh,
                    Date ngaySinh, boolean gioiTinh, String soDienThoai, String email, String diaChi, boolean trangThai) {
        this.maBenhNhan = maBenhNhan;
        this.tenBenhNhan = tenBenhNhan;
        this.hinhAnh = hinhAnh;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.trangThai = trangThai;
    }

    public int getMaBenhNhan() {
        return maBenhNhan;
    }

    public void setMaBenhNhan(int maBenhNhan) {
        this.maBenhNhan = maBenhNhan;
    }

    public String getTenBenhNhan() {
        return tenBenhNhan;
    }

    public void setTenBenhNhan(String tenBenhNhan) {
        this.tenBenhNhan = tenBenhNhan;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String joinAllField()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(maBenhNhan);
        stringBuilder.append(" ");
        stringBuilder.append(tenBenhNhan);
        stringBuilder.append(" ");
        stringBuilder.append(email);
        stringBuilder.append(soDienThoai);
        stringBuilder.append(" ");
        return stringBuilder.toString();
    }
}
