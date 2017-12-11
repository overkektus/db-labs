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
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ped.bstu.by.sqlite3.DbHelper;
import ped.bstu.by.sqlite3.R;

public class ActivityAnalysis extends AppCompatActivity {

    private static final String TAG = "SQLite3.ActAnalysis";

    private final String ACTION_01 = "Compare";
    private final String ACTION_02 = "CompareBySubject";

    TextView tvTitle;
    Spinner spinnerSubject;
    GridView gvAnalysis;

    Long unixDateFrom, unixDateTo;
    String action;
    String selectedFaculty, selectedSubject;

    DbHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        getExtraDataFromIntent();
        initDB();
        initViews();
    }

    private void getExtraDataFromIntent() {
        Intent intent = getIntent();
        unixDateFrom = intent.getLongExtra("dateFrom", 0L);
        unixDateTo = intent.getLongExtra("dateTo", 0L);
        action = intent.getStringExtra("Action");
        selectedFaculty = intent.getStringExtra("SelectedFaculty");
    }

    private void initDB() {
        dbHelper = new DbHelper(this, "STUDENTDB", 4);
        database = dbHelper.getWritableDatabase();
    }

    private void initViews() {
        spinnerSubject = findViewById(R.id.AnalysisActSpinnerSubject);
        final List<String> listFaculties = selectSubjects();

        ArrayAdapter<String> dataAdapterFaculty = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listFaculties);
        dataAdapterFaculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSubject.setAdapter(dataAdapterFaculty);

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedSubject = parent.getItemAtPosition(position).toString();
                showCompareFaculty(selectedFaculty, selectedSubject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });
        tvTitle = findViewById(R.id.tvTitleAnalysis);
        switch (action) {
            case ACTION_01:
                tvTitle.setText("Сравнительный анализ по группам (в целом)");
                spinnerSubject.setVisibility(View.GONE);
                break;
            case ACTION_02:
                tvTitle.setText("Сравнительный анализ по группам (по предмету)");
                break;
        }
        gvAnalysis = findViewById(R.id.gvAnalysis);
    }

    private ArrayList<String> selectSubjects() {
        ArrayList<String> subjects = new ArrayList<>();
        String sqlQuery = "SELECT subject FROM subject";
        Cursor c = database.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            do {
                subjects.add(c.getString(0));
            } while(c.moveToNext());
        }
        c.close();
        return subjects;
    }

    public void showCompareFaculty(String selectedFaculty, String selectedSubject) {
        Cursor c = null;
        String sqlQuery;
        switch (action) {
            case ACTION_01:
                sqlQuery = "SELECT g.groupname, ROUND(AVG(p.mark), 2) FROM groups g " +
                        "INNER JOIN student s ON g.idgroup = s.idgroup " +
                        "INNER JOIN progress p ON s.idstudent = p.idstudent " +
                        "WHERE p.datemark BETWEEN " + String.valueOf(unixDateFrom) + " and " + String.valueOf(unixDateTo) + " " +
                        "and g.faculty like '" + selectedFaculty + "' " +
                        "GROUP BY s.idgroup";
                c = database.rawQuery(sqlQuery, null);
                setAdapterGridView(c);
                break;
            case ACTION_02:
                sqlQuery = "SELECT g.groupname, ROUND(AVG(p.mark), 2) FROM groups g " +
                        "INNER JOIN student s ON g.idgroup = s.idgroup " +
                        "INNER JOIN progress p ON s.idstudent = p.idstudent " +
                        "INNER JOIN subject sb ON p.idsubject = sb.idsubject " +
                        "WHERE p.datemark BETWEEN " + String.valueOf(unixDateFrom) + " and " + String.valueOf(unixDateTo) + " " +
                        "and g.faculty like '" + selectedFaculty + "' " +
                        "and sb.subject like '" + selectedSubject + "' " +
                        "GROUP BY s.idgroup";
                c = database.rawQuery(sqlQuery, null);
                setAdapterGridView(c);
                break;
        }

        if(c != null){
            c.close();
        }
    }

    private void setAdapterGridView(Cursor cursor) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getTitleForStudentsTable());
        adapter.addAll(getDataForGridView(cursor));
        gvAnalysis.setAdapter(adapter);
    }

    protected ArrayList<String> getTitleForStudentsTable() {
        ArrayList<String> data = new ArrayList<>(Arrays.asList(
                "GROUP",
                "MARK"));
        return data;
    }

    public ArrayList<String> getDataForGridView(Cursor cursor) {
        ArrayList<String> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int group = 0;
            int mark = 1;
            do {
                data.add(cursor.getString(group));
                data.add(cursor.getString(mark));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

}
