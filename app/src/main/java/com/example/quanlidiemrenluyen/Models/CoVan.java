package com.example.quanlidiemrenluyen.Models;

public class CoVan {
    String hockyid;
    String classid;
    String covanid;

    public CoVan(String hockyid, String classid, String covanid) {
        this.hockyid = hockyid;
        this.classid = classid;
        this.covanid = covanid;
    }

    public String getHockyid() {
        return hockyid;
    }

    public void setHockyid(String hockyid) {
        this.hockyid = hockyid;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getCovanid() {
        return covanid;
    }

    public void setCovanid(String covanid) {
        this.covanid = covanid;
    }

    public CoVan() {
    }
}
