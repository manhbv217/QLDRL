package com.example.quanlidiemrenluyen.Models;

public class HocKyView {
    String id;
    String hocky;
    String note;

    public HocKyView(String hocky, String note, String id) {
        this.hocky = hocky;
        this.note = note;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHocky() {
        return hocky;
    }

    public void setHocky(String hocky) {
        this.hocky = hocky;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
