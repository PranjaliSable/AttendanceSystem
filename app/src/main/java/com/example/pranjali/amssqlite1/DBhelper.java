package com.example.pranjali.amssqlite1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.pranjali.amssqlite1.R.id.password;

/**
 * Created by PRANJALI on 29/08/2017.
 */

public class DBhelper extends SQLiteOpenHelper {
    public DBhelper(Context context) {
        super(context,"attendance", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table user (id INTEGER primary key AUTOINCREMENT,name text,username text,password text)");
        db.execSQL("create table s_attendance (id INTEGER primary key AUTOINCREMENT,roll text,date text,attendance text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS s_attendance");

        onCreate(db);
    }






    public boolean insertAttendance(String roll, String date, String attendance) {
        Log.i("From DB",""+roll+date+attendance);
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("roll",roll);
        contentValues.put("date",date);
        contentValues.put("attendance",attendance);
        db.insert("s_attendance",null,contentValues);
        return true;
    }


    public Cursor getAttendance() {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res =  db.rawQuery("select * from s_attendance", null );
        res.moveToFirst();
        Log.i("Database att",res.getString(0)+res.getString(1));
        if(res.getCount()<=0)
            return null;
        return res;
        //return attendance;
    }

    public boolean regUser(String name,String username,String password){
        Log.i("From DB",""+name+username+password);
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",name);
        contentValues.put("username",username);
        contentValues.put("password",password);
        db.insert("user",null,contentValues);
        return true;
    }
    public Cursor getdata(String username,String password){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res =  db.rawQuery("select username,password from user where username='"+username+"' and password='"+password+"'", null );
        res.moveToFirst();
        Log.i("Database",res.getString(0)+res.getString(1));
        if(res.getCount()<=0)
            return null;
        return res;
    }

    public Cursor getdata(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res =  db.rawQuery("select username,password from user", null );
        res.moveToFirst();
        Log.i("Database",res.getString(0)+res.getString(1));
        if(res.getCount()<=0)
            return null;
        return res;
    }
}
