package com.example.sjeong.alarmcall;

/**
 * Created by SJeong on 2017-05-04.
 */

public class Mode {

    private String name;
    private int star;
    private int contact;
    private int unknown;
    private int time;
    private int count;
    private int draw;
    private String sms;
    private String picture;

    public String getName(){return name;}

    public int getStar(){return star;}

    public int getContact(){return contact;}

    public int getUnknown(){return unknown;}

    public int getTime(){return time;}

    public int getCount(){return count;}

    public int getDraw() {return draw;}

    public String getSms() {return sms;}

    public String getPicture() {return picture;}

    public void setSms(String sms) {this.sms = sms;}

    public void setPicture(String picture) {this.picture = picture;}

    public void setDraw(int draw) {this.draw = draw;}

    public void setName(String name){this.name = name;}

    public void setStar(int star){this.star = star;}

    public void setContact(int contact){this.contact = contact;}

    public void setUnknown(int unknown){this.unknown = unknown;}

    public void setTime(int time){this.time = time;}

    public void setCount(int count){this.count = count;}
}

