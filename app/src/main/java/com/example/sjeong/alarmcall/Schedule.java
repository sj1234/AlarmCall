package com.example.sjeong.alarmcall;

/**
 * Created by SJeong on 2017-05-04.
 */

public class Schedule {

    private String start;
    private String end;
    private Boolean sun;
    private Boolean mon;
    private Boolean tue;
    private Boolean wed;
    private Boolean thu;
    private Boolean fri;
    private Boolean sat;
    private String modename;
    private String premodename;
    private int id;

    public int getId(){return id;};

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public Boolean getSun() {
        return sun;
    }

    public Boolean getMon() {
        return mon;
    }

    public Boolean getTue() {
        return tue;
    }

    public Boolean getWed() {
        return wed;
    }

    public Boolean getThu() {
        return thu;
    }

    public Boolean getFri() {
        return fri;
    }

    public Boolean getSat() {
        return sat;
    }

    public String getModename() { return modename;}

    public String getPremodename() { return premodename;}

    public void setId(int id){this.id = id;}

    public void setStart(String start){
        this.start = start;
    }

    public void setEnd(String end){
        this.end = end;
    }

    public void setSun(Boolean sun){
        this.sun = sun;
    }

    public void setMon(Boolean mon){
        this.mon = mon;
    }

    public void setTue(Boolean tue){
        this.tue = tue;
    }

    public void setWed(Boolean wed){
        this.wed = wed;
    }

    public void setThu(Boolean thu){
        this.thu = thu;
    }

    public void setFri(Boolean fri){
        this.fri = fri;
    }

    public void setSat(Boolean sat){
        this.sat = sat;
    }

    public void setModename(String modename) { this.modename = modename; }

    public void setPremodename(String premodename) { this.premodename = premodename; }
}
