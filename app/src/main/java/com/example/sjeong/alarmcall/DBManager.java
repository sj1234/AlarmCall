package com.example.sjeong.alarmcall;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by SJeong on 2017-05-04.
 */

public class DBManager extends SQLiteOpenHelper {
    private Context dbcontext;

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        dbcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = "CREATE TABLE MODE"+ "(NAME TEXT PRIMARY KEY NOT NULL,"+"STAR INTEGER NOT NULL,"+"CONTACT INTEGER NOT NULL,"+"UNKNOWN INTEGER NOT NULL,"+" TIME INTEGER NOT NULL,"+" COUNT INTEGER NOT NULL, DRAW INTEGER NOT NULL, SMS TEXT NOT NULL, PICTURE TEXT);";
        String table2 = "CREATE TABLE SCHEDULE(_id INTEGER PRIMARY KEY AUTOINCREMENT, START TEXT, END TEXT, " +
              "SUN INTEGER, MON INTEGER, TUE INTEGER,WED INTEGER, THU INTEGER, FRI INTEGER, SAT INTEGER, MODENAME TEXT, PREMODENAME TEXT, ONOFF INTEGER);";
        db.execSQL(table);
        db.execSQL(table2);
        Log.i("test DB", "create db table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void ReadDB() { SQLiteDatabase db = getReadableDatabase(); }

    public void DeleteDB() {  // DB 리셋용
        SQLiteDatabase db = getWritableDatabase();
        String insertdb = "DELETE FROM MODE;";
        db.execSQL(insertdb);
        insertdb = "DELETE FROM SCHEDULE;";
        db.execSQL(insertdb);
        }


    public void insertMode(Mode mode){
        SQLiteDatabase db = getWritableDatabase();
        String insertmode = "INSERT INTO MODE("+"NAME, STAR, CONTACT, UNKNOWN, TIME, COUNT, DRAW, SMS ,PICTURE)"+" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
        db.execSQL(insertmode, new Object[]{mode.getName(), mode.getStar(), mode.getContact(), mode.getUnknown(), mode.getTime(), mode.getCount(), mode.getDraw(), mode.getSms(), mode.getPicture()});
        Log.i("test DB", "insert : " + mode.getName()+", "+mode.getStar()+", "+ mode.getContact()+", "+ mode.getUnknown()+", "+ mode.getTime()+", "+ mode.getCount());
    }

    public void insertSchedule(Schedule schedule){
        SQLiteDatabase db = getWritableDatabase();
        String insertschedule = "INSERT INTO SCHEDULE(_id, START, END, SUN, MON, TUE, WED, THU, FRI, SAT, MODENAME, PREMODENAME, ONOFF) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        db.execSQL(insertschedule, new Object[]{null, schedule.getStart(), schedule.getEnd(), schedule.getSun(), schedule.getMon(), schedule.getTue(), schedule.getWed(), schedule.getThu(), schedule.getFri(), schedule.getSat(), schedule.getModename().toString(), schedule.getPremodename().toString(), 0});
        Log.i("test DB", "insert : " + schedule.getStart()+", "+schedule.getEnd()+", "+schedule.getModename().toString()+", "+ schedule.getPremodename().toString());
    }

    public void updateMode(String originalname, Mode mode){
        SQLiteDatabase db = getWritableDatabase();
        String updatemode = "UPDATE MODE SET "+"NAME = ?, STAR =?, CONTACT =?, UNKNOWN =?, TIME =?, COUNT =?, DRAW =?, SMS =?, PICTURE =?"+" WHERE NAME=?;";
        String updateschedule = "UPDATE SCHEDULE SET "+"MODENAME = ?"+" WHERE MODENAME=?;";
        db.execSQL(updatemode, new Object[]{mode.getName().toString(), mode.getStar(), mode.getContact(), mode.getUnknown(), mode.getTime(), mode.getCount(), mode.getDraw(), mode.getSms(), mode.getPicture(), originalname});
        db.execSQL(updateschedule, new Object[]{mode.getName().toString(), originalname});
        Log.i("test DB", "update : " + mode.getName()+", "+mode.getStar()+", "+ mode.getContact()+", "+ mode.getUnknown()+", "+ mode.getTime()+", "+ mode.getCount());
    }

    public void updateSchedule(Schedule schedule){
        SQLiteDatabase db = getWritableDatabase();
        String updateschedule = "UPDATE SCHEDULE SET START =?, END =?, SUN =?, MON =?, TUE =?, WED =?, THU =?, FRI =?, SAT =?, MODENAME =?, PREMODENAME=?, ONOFF=? WHERE _id =?;";
        db.execSQL(updateschedule, new Object[]{ schedule.getStart().toString(), schedule.getEnd().toString(), schedule.getSun(), schedule.getMon(), schedule.getTue(), schedule.getWed(), schedule.getThu(), schedule.getFri(), schedule.getSat(), schedule.getModename().toString(), schedule.getPremodename(), schedule.getOnoff(), schedule.getId()});
        Log.i("test DB", "update : " + schedule.getSun() +"/"+ schedule.getMon()+"/"+ schedule.getTue()+"/"+schedule.getWed()+"/"+schedule.getThu()+"/"+schedule.getFri()+"/"+schedule.getSat());
    }

    public void deleteMode(String modename){
        SQLiteDatabase db  = getWritableDatabase();
        String deletemode = "DELETE FROM MODE WHERE NAME = ?;";
        String deleteschedule = "DELETE FROM SCHEDULE WHERE MODENAME = ?;";

        db.execSQL(deletemode, new Object[]{modename});
        db.execSQL(deleteschedule, new Object[]{modename});
        Log.i("test DB", "delete mode"+modename);
    }

    public ArrayList<Schedule> deleteMode_scheduleOff(String modename){
        String string = "SELECT * FROM SCHEDULE WHERE MODENAME = ?;";
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        Cursor cursor = db.rawQuery(string, new String[]{modename});
        if(cursor.moveToFirst()) {
            do {
                Schedule schedule = new Schedule();

                schedule.setId(cursor.getInt(0));
                schedule.setOnoff(cursor.getInt(12));

                schedules.add(schedule);
            }
            while (cursor.moveToNext());
        }
        return schedules;
    }

    public void deleteSchedule(int id){
        SQLiteDatabase db = getWritableDatabase();
        String deleteschedule = "DELETE FROM SCHEDULE WHERE _id = ?;";
        db.execSQL(deleteschedule, new Object[]{id});
    }

    public ArrayList<String> getModesName(){
        String string = "SELECT NAME FROM MODE;";
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<String> modes = new ArrayList<String>();

        Cursor cursor = db.rawQuery(string, null);
        if(cursor.moveToFirst()) {
            do {
                modes.add(cursor.getString(0));
                Log.i("test DBManager", cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        return modes;
    }

    public ArrayList<Mode> getModes() {

        String string = "SELECT * FROM MODE;";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(string, null);
        ArrayList<Mode> modes = new ArrayList<Mode>();

        if(cursor.moveToFirst())
        {
            do {
                Mode mode=new Mode();

                mode.setName(cursor.getString(0));
                mode.setStar(cursor.getInt(1));
                mode.setContact(cursor.getInt(2));
                mode.setUnknown(cursor.getInt(3));
                mode.setTime(cursor.getInt(4));
                mode.setCount(cursor.getInt(5));
                mode.setDraw(cursor.getInt(6));
                mode.setSms(cursor.getString(7));
                mode.setPicture(cursor.getString(8));

                modes.add(mode);
            }
            while (cursor.moveToNext());
        }
        else
            Log.i("test DBManager", "get mode null");

        return modes;
    }

    public ArrayList<Schedule> getSchedules(){
        String string = "SELECT * FROM SCHEDULE ORDER BY START;";
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        Cursor cursor = db.rawQuery(string, null);
        if(cursor.moveToFirst()) {
            do {
                Schedule schedule = new Schedule();

                schedule.setId(cursor.getInt(0));
                schedule.setStart(cursor.getString(1));
                schedule.setEnd(cursor.getString(2));
                schedule.setSun(cursor.getInt(3));
                schedule.setMon(cursor.getInt(4));
                schedule.setTue(cursor.getInt(5));
                schedule.setWed(cursor.getInt(6));
                schedule.setThu(cursor.getInt(7));
                schedule.setFri(cursor.getInt(8));
                schedule.setSat(cursor.getInt(9));
                schedule.setModename(cursor.getString(10));
                schedule.setPremodename(cursor.getString(11));
                schedule.setOnoff(cursor.getInt(12));

                schedules.add(schedule);

                Log.i("test DBManager", cursor.getString(10));
                Log.i("test DBManager", cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        return schedules;
    }

    public Mode getMode(String modename) {

        String string = "SELECT * FROM MODE;";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(string, null);
        Mode mode=new Mode();

        if(cursor.moveToFirst())
        {
            do {
                Log.i("test DBManager", cursor.getString(0));
                if (cursor.getString(0).equals(modename)) {
                    mode.setName(cursor.getString(0));
                    mode.setStar(cursor.getInt(1));
                    mode.setContact(cursor.getInt(2));
                    mode.setUnknown(cursor.getInt(3));
                    mode.setTime(cursor.getInt(4));
                    mode.setCount(cursor.getInt(5));
                    mode.setDraw(cursor.getInt(6));
                    mode.setSms(cursor.getString(7));
                    mode.setPicture(cursor.getString(8));
                    return mode;
                }
            }
                while (cursor.moveToNext());
        }
        else
            Log.i("test DBManager", "get mode null");

        mode.setName(null);
        return mode;
    }

    public Schedule getSchedule(int position){
        String string = "SELECT * FROM SCHEDULE ORDER BY START;";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(string, null);
        Schedule schedule=new Schedule();

        int cusorposition=0;

        if(cursor.moveToFirst())
        {
            do {
                if (cusorposition==position) {
                    schedule.setId(cursor.getInt(0));
                    schedule.setStart(cursor.getString(1));
                    schedule.setEnd(cursor.getString(2));
                    schedule.setSun(cursor.getInt(3));
                    schedule.setMon(cursor.getInt(4));
                    schedule.setTue(cursor.getInt(5));
                    schedule.setWed(cursor.getInt(6));
                    schedule.setThu(cursor.getInt(7));
                    schedule.setFri(cursor.getInt(8));
                    schedule.setSat(cursor.getInt(9));
                    schedule.setModename(cursor.getString(10));
                    schedule.setPremodename(cursor.getString(11));
                    schedule.setOnoff(cursor.getInt(12));
                    Log.i("test DBManager", "get schedule "+cursor.getString(1)+", "+cursor.getString(2)+", "+cursor.getString(10));
                    return schedule;
                }
                cusorposition++;
            }
            while (cursor.moveToNext());
        }
        else
            Log.i("test DBManager", "get mode null");

        return null;
    }

    public Schedule getScheduleId(int id) {
        String string = "SELECT * FROM SCHEDULE;";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(string, null);
        Schedule schedule=new Schedule();

        if(cursor.moveToFirst())
        {
            do {
                if (cursor.getInt(0)==id) {
                    schedule.setId(cursor.getInt(0));
                    schedule.setStart(cursor.getString(1));
                    schedule.setEnd(cursor.getString(2));
                    schedule.setSun(cursor.getInt(3));
                    schedule.setMon(cursor.getInt(4));
                    schedule.setTue(cursor.getInt(5));
                    schedule.setWed(cursor.getInt(6));
                    schedule.setThu(cursor.getInt(7));
                    schedule.setFri(cursor.getInt(8));
                    schedule.setSat(cursor.getInt(9));
                    schedule.setModename(cursor.getString(10));
                    schedule.setPremodename(cursor.getString(11));
                    schedule.setOnoff(cursor.getInt(12));
                    Log.i("test DBManager", "get schedule "+cursor.getString(1)+", "+cursor.getString(2)+", "+cursor.getString(10));
                    return schedule;
                }
            }
            while (cursor.moveToNext());
        }
        else
            Log.i("test DBManager", "get mode null");

        return null;

    }

}