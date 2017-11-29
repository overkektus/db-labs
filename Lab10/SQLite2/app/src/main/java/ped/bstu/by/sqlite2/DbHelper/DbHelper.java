package ped.bstu.by.sqlite2.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ped.bstu.by.sqlite2.Group.Group;

/**
 * Created by Egor on 28.10.2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LAB_DB";
    public static final String TABLE_GROUPS = "Groups";
    public static final String TABLE_STUDENTS = "Students";

    public static final String KEY_IDG = "idgroup";
    public static final String KEY_IDS = "idstudent";
    public static final String KEY_F = "faculty";
    public static final String KEY_C = "course";
    public static final String KEY_N = "name";
    public static final String KEY_H = "head";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        db.execSQL(String.format ("PRAGMA foreign_keys = %s","ON"));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_GROUPS);
        db.execSQL("drop table if exists " + TABLE_STUDENTS);
        onCreate(db);
    }

}
