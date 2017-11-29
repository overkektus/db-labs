package ped.bstu.by.sqlite3.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ped.bstu.by.sqlite3.DbHelper;
import ped.bstu.by.sqlite3.R;

public class ActivityShowStudents extends AppCompatActivity {

    Spinner spinnerFaculty;

    Long unixDateFrom, unixDateTo;
    String action;

    DbHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);

        Intent intent = getIntent();
        unixDateFrom = intent.getLongExtra("dateFrom", 0L);
        unixDateTo = intent.getLongExtra("dateTo", 0L);
        action = intent.getStringExtra("Action");

        initDB();
        initViews();
    }

    private void initDB() {
        dbHelper = new DbHelper(this, "STUDENTDB", 4);
        database = dbHelper.getWritableDatabase();
    }

    private void initViews() {
        spinnerFaculty = findViewById(R.id.ShowActSpinnerFaculty);
        final List<String> listFaculties = selectFaculties();

        ArrayAdapter<String> dataAdapterFaculty = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listFaculties);
        dataAdapterFaculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFaculty.setAdapter(dataAdapterFaculty);

        spinnerFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedFaculty = parent.getItemAtPosition(position).toString();
                showStudents(selectedFaculty);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private ArrayList<String> selectFaculties() {
        ArrayList<String> faculties = new ArrayList<>();
        String sqlQuery = "SELECT faculty FROM faculty;";
        Cursor c = database.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            do {
                faculties.add(c.getString(0));
            } while(c.moveToNext());
        }
        c.close();
        return faculties;
    }

    public void showStudents(String selectedFaculty) {
        Cursor c = null;
        String sqlQuery;
        switch (action) {
            case "ShowBestStudent":
                sqlQuery = "SELECT g.name, s.name, avg(p.mark) FROM student s "
                        + "INNER JOIN progress p ON p.idstudent = s.idstudent "
                        + "INNER JOIN groups g ON g.idgroup = s.idgroup "
                        + "INNER JOIN faculty f ON f.faculty = g.faculty "
                        + "WHERE f.faculty like \"" + selectedFaculty + "\" "
                        + "and p.date BETWEEN " + unixDateFrom + " and " + unixDateTo + ";";
                c = database.rawQuery(sqlQuery, null);
                DbHelper.logCursor(c);
                break;
            case "ShowBadStudent":
                sqlQuery = "";
                c = database.rawQuery(sqlQuery, null);
                DbHelper.logCursor(c);
                break;
        }
        if(c != null){
            c.close();
        }
    }

}
