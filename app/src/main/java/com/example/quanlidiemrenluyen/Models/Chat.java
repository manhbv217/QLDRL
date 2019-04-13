package com.example.quanlidiemrenluyen.Models;

public class Chat {
    private String sender;
    private String receiver;
    private String mesenger;
    private boolean isseen;

    public Chat(String sender, String receiver, String mesenger, boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.mesenger = mesenger;
        this.isseen = isseen;
    }

    public Chat(){

    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMesenger() {
        return mesenger;
    }

    public void setMesenger(String mesenger) {
        this.mesenger = mesenger;
    }
}
