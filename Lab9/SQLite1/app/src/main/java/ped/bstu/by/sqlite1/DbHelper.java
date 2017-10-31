package ped.bstu.by.sqlite1;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Egor on 25.10.2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LAB_DB";
    public static final String TABLE_SIMPLETABLE = "SimpleTable";

    public static final String KEY_ID = "_id";
    public static final String KEY_F= "f";
    public static final String KEY_T = "t";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_SIMPLETABLE + "(" + KEY_ID
                + " integer primary key autoincrement," + KEY_F + " real not null," + KEY_T + " text not null" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_SIMPLETABLE);

        onCreate(db);
    }
}
