package com.example.quanlidiemrenluyen.Models;

public class DiemRenLuyen {
    String studenid;
    String hockyid;
    String status;
    Point point;

    public DiemRenLuyen() {
    }

    public String getStudenid() {
        return studenid;
    }

    public void setStudenid(String studenid) {
        this.studenid = studenid;
    }

    public String getHockyid() {
        return hockyid;
    }

    public void setHockyid(String hockyid) {
        this.hockyid = hockyid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public DiemRenLuyen(String studenid, String hockyid, String status, Point point) {
        this.studenid = studenid;
        this.hockyid = hockyid;
        this.status = status;
        this.point = point;
    }
}
