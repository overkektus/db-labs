package ped.bstu.by.sqlite3.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;

import ped.bstu.by.sqlite3.DbHelper;
import ped.bstu.by.sqlite3.R;

public class CompareFacultyActivity extends AppCompatActivity {

    private static final String TAG = "SQLite3.CompFacultyAct";

    GridView gvCompareFaculty;

    Long unixDateFrom, unixDateTo;

    DbHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_faculty);

        getExtraDataFromIntent();
        initDB();
        initViews();
        showCompareFaculty();
    }

    private void getExtraDataFromIntent() {
        Intent intent = getIntent();
        unixDateFrom = intent.getLongExtra("dateFrom", 0L);
        unixDateTo = intent.getLongExtra("dateTo", 0L);
    }

    private void initDB() {
        dbHelper = new DbHelper(this, "STUDENTDB", 4);
        database = dbHelper.getWritableDatabase();
    }

    private void initViews() {
        gvCompareFaculty = findViewById(R.id.gvCompareFaculty);
    }

    public void showCompareFaculty() {

        String sqlQuery = "SELECT g.faculty, ROUND(AVG(p.mark), 2) FROM groups g " +
                "INNER JOIN student s ON g.idgroup = s.idgroup " +
                "INNER JOIN progress p ON s.idstudent = p.idstudent " +
                "WHERE p.datemark BETWEEN " + String.valueOf(unixDateFrom) + " and " + String.valueOf(unixDateTo) + " " +
                "GROUP BY g.faculty ORDER BY avg(p.mark) ASC";

        Cursor c = database.rawQuery(sqlQuery, null);
        c.moveToFirst();
        Log.d("UNIX", String.valueOf(unixDateFrom));
        Log.d("UNIX", String.valueOf(unixDateTo));
        Log.d(TAG, String.valueOf(c.getCount()));

        setAdapterGridView(c);

        if(c != null){
            c.close();
        }
    }

    private void setAdapterGridView(Cursor cursor) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getTitleForStudentsTable());
        adapter.addAll(getDataForGridView(cursor));
        gvCompareFaculty.setAdapter(adapter);
    }

    protected ArrayList<String> getTitleForStudentsTable() {
        ArrayList<String> data = new ArrayList<>(Arrays.asList(
                "FACULT",
                "MARK"));
        return data;
    }

    public ArrayList<String> getDataForGridView(Cursor cursor) {
        ArrayList<String> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int facult = 0;
            int mark = 1;
            do {
                data.add(cursor.getString(facult));
                data.add(cursor.getString(mark));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

}
