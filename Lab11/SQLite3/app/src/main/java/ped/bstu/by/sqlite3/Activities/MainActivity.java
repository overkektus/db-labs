package ped.bstu.by.sqlite3.Activities;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.facebook.stetho.Stetho;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ped.bstu.by.sqlite3.DbHelper;
import ped.bstu.by.sqlite3.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private static final String TAG = "MainActivity";

    Spinner spinnerActions, spinnerActionAnalysis, spinnerPeriod,
            spinnerFacultiesBestStudent, spinnerFacultiesBadStudent,
            spinnerFacultiesAnalysis, spinnerFacultiesCompare;

    EditText etPeriodShowListGroupFrom, etPeriodShowListGroupTo;

    DbHelper dbHelper;
    SQLiteDatabase database;

    Date dateFrom, dateTo;
    Long unixDateFrom = 0L, unixDateTo = 0L;
    String selectedFacultyForBestStudent, selectedFacultyForBadStudent, selectedFacultyForAnalysis, selectedFacultyForCompare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialStetho();
        initDB();
        initViews();
    }

    private void initViews() {
        spinnerActions = findViewById(R.id.spinnerAction);
        spinnerActionAnalysis = findViewById(R.id.spinnerActionAnalysis);
        spinnerPeriod = findViewById(R.id.spinnerPeriod);
        spinnerFacultiesBestStudent = findViewById(R.id.spinnerFacultiesBestStudent);
        spinnerFacultiesBestStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFacultyForBestStudent = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });

        spinnerFacultiesBadStudent = findViewById(R.id.spinnerFacultiesBadStudent);
        spinnerFacultiesBadStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFacultyForBadStudent = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });

        spinnerFacultiesAnalysis = findViewById(R.id.spinnerFacultiesAnalysis);
        spinnerFacultiesAnalysis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFacultyForAnalysis = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });

        final List<String> listFaculties = selectFaculties();

        ArrayAdapter<String> dataAdapterFaculty = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listFaculties);
        dataAdapterFaculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFacultiesBestStudent.setAdapter(dataAdapterFaculty);
        spinnerFacultiesBadStudent.setAdapter(dataAdapterFaculty);
        spinnerFacultiesAnalysis.setAdapter(dataAdapterFaculty);

        findViewById(R.id.btnShowListGroup).setOnClickListener(this);
        findViewById(R.id.btnShowBestStudent).setOnClickListener(this);
        findViewById(R.id.btnShowBadStudent).setOnClickListener(this);
        findViewById(R.id.btnAnalysis).setOnClickListener(this);
        findViewById(R.id.btnCompareFaculty).setOnClickListener(this);

        etPeriodShowListGroupFrom = findViewById(R.id.etPeriodShowListGroupFrom);
        etPeriodShowListGroupTo = findViewById(R.id.etPeriodShowListGroupTo);

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

    private void initDB() {
        this.deleteDatabase("STUDENTDB");
        dbHelper = new DbHelper(this, "STUDENTDB", 4);
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowListGroup:
                getSelectedDate();
                showListGroup();
                break;
            case R.id.btnShowBestStudent:
                getSelectedDate();
                showStudents("ShowBestStudent");
                break;
            case R.id.btnShowBadStudent:
                getSelectedDate();
                showStudents("ShowBadStudent");
                break;
            case R.id.btnAnalysis:
                getSelectedDate();
                showAnalysis();
                break;
            case R.id.btnCompareFaculty:
                getSelectedDate();
                showCompare();
                break;
        }
    }

    private void getSelectedDate() {
        if(etPeriodShowListGroupFrom.getText().toString().equals("") || etPeriodShowListGroupTo.getText().toString().equals("")) {
            String period = (String)spinnerPeriod.getSelectedItem();
            switch (period){
                case "за предыдущую неделю":
                    dateTo = Calendar.getInstance().getTime();
                    unixDateTo = Long.parseLong(String.valueOf(dateTo.getTime()).substring(0, 10));
                    unixDateFrom = unixDateTo - 7 * 86400;
                    break;
                case "за предыдущую месяц":
                    dateTo = Calendar.getInstance().getTime();
                    unixDateTo = Long.parseLong(String.valueOf(dateTo.getTime()).substring(0, 10));
                    unixDateFrom = unixDateTo - 31 * 86400;
                    break;
                case "за предыдущую квартал":
                    dateTo = Calendar.getInstance().getTime();
                    unixDateTo = Long.parseLong(String.valueOf(dateTo.getTime()).substring(0, 10));
                    unixDateFrom = unixDateTo - 91 * 86400;
                    break;
                case "за предыдущую год":
                    dateTo = Calendar.getInstance().getTime();
                    unixDateTo = Long.parseLong(String.valueOf(dateTo.getTime()).substring(0, 10));
                    unixDateFrom = unixDateTo - 365 * 86400;
                    break;
            }
        } else {
            try {
                dateFrom = simpleDateFormat.parse(etPeriodShowListGroupFrom.getText().toString());
                dateTo = simpleDateFormat.parse(etPeriodShowListGroupTo.getText().toString());
                unixDateFrom = Long.parseLong(String.valueOf(dateFrom.getTime()).substring(0, 10));
                unixDateTo = Long.parseLong(String.valueOf(dateTo.getTime()).substring(0, 10));
                Log.d("DATE", unixDateFrom.toString());
                Log.d("DATE", unixDateTo.toString());
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        Log.d("DATE", unixDateFrom.toString());
        Log.d("DATE", unixDateTo.toString());

    }

    private void showListGroup() {
        Intent intent = new Intent(this, ActivityShowListGroup.class);
        String action = (String)spinnerActions.getSelectedItem();
        switch (action) {
            case "каждого студента по предмету":
                intent.putExtra("Action", "forStudentOnSubject");
                intent.putExtra("dateFrom", unixDateFrom);
                intent.putExtra("dateTo", unixDateTo);
                startActivity(intent);
                break;
            case "студента в целом":
                intent.putExtra("Action", "forStudent");
                intent.putExtra("dateFrom", unixDateFrom);
                intent.putExtra("dateTo", unixDateTo);
                startActivity(intent);
                break;
            case "группы в целом":
                intent.putExtra("Action", "forGroup");
                intent.putExtra("dateFrom", unixDateFrom);
                intent.putExtra("dateTo", unixDateTo);
                startActivity(intent);
                break;
        }
    }

    private void showStudents(String action) {
        Intent intent = new Intent(this, ActivityShowStudents.class);
        intent.putExtra("Action", action);
        intent.putExtra("dateFrom", unixDateFrom);
        intent.putExtra("dateTo", unixDateTo);
        switch (action) {
            case "ShowBestStudent":
                intent.putExtra("SelectedFaculty", selectedFacultyForBestStudent);
                break;
            case "ShowBadStudent":
                intent.putExtra("SelectedFaculty", selectedFacultyForBadStudent);
                break;
        }
        startActivity(intent);
    }

    private void showAnalysis() {
        String action = (String)spinnerActionAnalysis.getSelectedItem();
        String faculty = (String)spinnerFacultiesAnalysis.getSelectedItem();
        Intent intent = new Intent(this, ActivityAnalysis.class);
        intent.putExtra("SelectedFaculty", faculty);
        intent.putExtra("dateFrom", unixDateFrom);
        intent.putExtra("dateTo", unixDateTo);
        switch (action) {
            case "по предмету":
                intent.putExtra("Action", "CompareBySubject");
                break;
            case "в целом":
                intent.putExtra("Action", "Compare");
                break;
        }
        startActivity(intent);
    }

    private void showCompare() {
        Intent intent = new Intent(this, CompareFacultyActivity.class);
        intent.putExtra("dateFrom", unixDateFrom);
        intent.putExtra("dateTo", unixDateTo);
        startActivity(intent);
    }

    private void initialStetho() {
        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

        // Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        );

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
    }

}
