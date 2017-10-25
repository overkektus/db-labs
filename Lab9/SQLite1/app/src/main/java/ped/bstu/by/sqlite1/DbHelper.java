package ped.bstu.by.sqlite1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Egor on 25.10.2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Lab_DB.db";
    private static final int SCHEMA = 1;
    static final String TABLE = "SimpleTable";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_F = "f";
    public static final String COLUMN_T = "t";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + " ( "
                + COLUMN_ID + " integer primary key autoincrement,"
                + COLUMN_F + " float,"
                + COLUMN_T +" text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
