package com.example.quanlidiemrenluyen.Models;

public class User {
    private String username;
    private String id;
    private String imageURL;
    private String status;

    public User(String userName, String id, String imageURL, String status) {
        this.username = userName;
        this.id = id;
        this.imageURL = imageURL;
        this.status = status;
    }
    public User(){

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
