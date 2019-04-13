package com.example.quanlidiemrenluyen.Models;

public class HocKy {
    String id;
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HocKy() {
    }

    public HocKy(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
