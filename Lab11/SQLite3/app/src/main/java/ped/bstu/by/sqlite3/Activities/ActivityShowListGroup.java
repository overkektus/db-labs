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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ped.bstu.by.sqlite3.DbHelper;
import ped.bstu.by.sqlite3.R;

public class ActivityShowListGroup extends AppCompatActivity {

    private static final String TAG = "SQLite3.DBHelper";

    private final String ACTION_01 = "forStudentOnSubject";
    private final String ACTION_02 = "forStudent";
    private final String ACTION_03 = "forGroup";

    TextView tvSubject;
    Spinner spinnerFaculty, spinnerGroup, spinnerSubject;
    GridView mGroups;

    Long unixDateFrom, unixDateTo;
    String action;

    DbHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_group);

        Intent intent = getIntent();
        unixDateFrom = intent.getLongExtra("dateFrom", 0L);
        unixDateTo = intent.getLongExtra("dateTo", 0L);
        action = intent.getStringExtra("Action");

        Log.d("UNIX", unixDateFrom.toString());
        Log.d("UNIX", unixDateTo.toString());

        initDB();
        initViews();
        selectFaculties();
        selectProgress();
        selectGroups("ФИТ");
    }

    private void initDB() {
        dbHelper = new DbHelper(this, "STUDENTDB", 4);
        database = dbHelper.getWritableDatabase();
    }

    private void selectProgress() {
        String sqlQuery = "SELECT count(idprogress) FROM progress;";
        Cursor c = database.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            DbHelper.logCursor(c);
        }
        c.close();
    }

    private void initViews() {
        tvSubject = findViewById(R.id.tvSubject);
        mGroups = findViewById(R.id.gvGroups);
        spinnerSubject = findViewById(R.id.ShowActSpinnerSubject);
        spinnerGroup = findViewById(R.id.ShowActSpinnerGroup);
        spinnerFaculty = findViewById(R.id.ShowActSpinnerFaculty);
        final List<String> listFaculties = selectFaculties();

        ArrayAdapter<String> dataAdapterFaculty = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listFaculties);
        dataAdapterFaculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFaculty.setAdapter(dataAdapterFaculty);

        spinnerFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)  {
                final String selectedFaculty = parent.getItemAtPosition(position).toString();
                List<String> listGroups = selectGroups(selectedFaculty);

                ArrayAdapter<String> dataAdapterGroup = new ArrayAdapter<>(ActivityShowListGroup.this,
                        android.R.layout.simple_spinner_item, listGroups);
                dataAdapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerGroup.setAdapter(dataAdapterGroup);

                spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        final String selectedGroup = parent.getItemAtPosition(position).toString();
                        List<String> listSubjects = selectSubjects();

                        ArrayAdapter<String> dataAdapterSubject = new ArrayAdapter<>(ActivityShowListGroup.this,
                                android.R.layout.simple_spinner_item, listSubjects);
                        dataAdapterSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        if(action.equals(ACTION_02) || action.equals(ACTION_03)) {
                            showListGroup(selectedFaculty, selectedGroup, null);
                        }

                        spinnerSubject.setAdapter(dataAdapterSubject);

                        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                final String selectedSubject = parent.getItemAtPosition(position).toString();
                                showListGroup(selectedFaculty, selectedGroup, selectedSubject);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) { }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        switch (action) {
            case ACTION_01:
                break;
            case ACTION_02:
                tvSubject.setVisibility(View.GONE);
                spinnerSubject.setVisibility(View.GONE);
                break;
            case ACTION_03:
                tvSubject.setVisibility(View.GONE);
                spinnerSubject.setVisibility(View.GONE);
                break;
        }
    }

    private void setAdapterGridViewACTION01and02(Cursor cursor) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getTitleForGroupsTableACTION01and02());
        adapter.addAll(getDataForGridViewACTION01and02(cursor));
        mGroups.setAdapter(adapter);
    }

    private void setAdapterGridViewACTION03(Cursor cursor) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getTitleForGroupsTableACTION03());
        adapter.addAll(getDataForGridViewACTION03(cursor));
        mGroups.setAdapter(adapter);
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

    private ArrayList<String> selectGroups(String faculty) {
        ArrayList<String> groups = new ArrayList<>();
        String[] selectionArgs = new String[]{faculty.toString()};
        String sqlQuery = "SELECT groupname FROM groups WHERE faculty like ?";
        Cursor c = database.rawQuery(sqlQuery, selectionArgs);
        if(c.moveToFirst()) {
            do {
                groups.add(String.valueOf(c.getInt(0)));
            } while(c.moveToNext());
        }
        c.close();
        return groups;
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

    private void showListGroup(String selectedFaculty, String selectedGroup, String selectedSubject) {
        Cursor c = null;
        String sqlQuery;
        switch (action) {
            case ACTION_01:
                sqlQuery = "SELECT g.faculty, g.course, g.groupname, s.name, ROUND(AVG(p.mark), 2), sb.subject FROM progress p "
                        + "INNER JOIN student s ON p.idstudent = s.idstudent "
                        + "INNER JOIN groups g ON g.idgroup = s.idgroup "
                        + "INNER JOIN subject sb ON p.idsubject = sb.idsubject "
                        + "WHERE g.faculty like \"" + selectedFaculty + "\" and g.groupname like \"" + selectedGroup
                        + "\" and sb.subject like \"" + selectedSubject + "\" "
                        + "and p.datemark BETWEEN " + String.valueOf(unixDateFrom) + " and " + String.valueOf(unixDateTo) +
                        " GROUP BY s.name";
                c = database.rawQuery(sqlQuery, new String[]{});
                c.moveToNext();
                Log.d(TAG, String.valueOf(c.getInt(4)));
                setAdapterGridViewACTION01and02(c);
                break;
            case ACTION_02:
                sqlQuery = "SELECT g.faculty, g.course, g.groupname, s.name, ROUND(AVG(p.mark), 2) FROM progress p "
                        + "INNER JOIN student s ON p.idstudent = s.idstudent "
                        + "INNER JOIN groups g ON g.idgroup = s.idgroup "
                        + "WHERE g.faculty like \"" + selectedFaculty + "\" "
                        + "and g.groupname like \"" + selectedGroup + "\" "
                        + "and p.datemark BETWEEN " + String.valueOf(unixDateFrom) + " and " + String.valueOf(unixDateTo)
                        + " GROUP BY s.name";
                c = database.rawQuery(sqlQuery, new String[]{});
                setAdapterGridViewACTION01and02(c);
                break;
            case ACTION_03:
                sqlQuery = "SELECT g.faculty, g.course, g.groupname, ROUND(AVG(p.mark), 2) FROM progress p "
                        + "INNER JOIN student s ON p.idstudent = s.idstudent "
                        + "INNER JOIN groups g ON g.idgroup = s.idgroup "
                        + "INNER JOIN subject sb ON p.idsubject = sb.idsubject "
                        + "WHERE g.faculty like \"" + selectedFaculty + "\" "
                        + "and g.groupname like \"" + selectedGroup + "\" "
                        + "and p.datemark BETWEEN " + String.valueOf(unixDateFrom) + " and " + String.valueOf(unixDateTo) +
                        " GROUP BY g.groupname";
                c = database.rawQuery(sqlQuery, new String[]{});
                //      DbHelper.logCursor(c);
                setAdapterGridViewACTION03(c);
                break;
        }
        if(c != null) {
            c.close();
        }
    }

    protected ArrayList<String> getTitleForGroupsTableACTION01and02() {
        ArrayList<String> data = new ArrayList<>(Arrays.asList(
                "NAME",
                "MARK"));
        return data;
    }

    protected ArrayList<String> getTitleForGroupsTableACTION03() {
        ArrayList<String> data = new ArrayList<>(Arrays.asList(
                "MARK"
        ));
        return data;
    }

    public ArrayList<String> getDataForGridViewACTION01and02(Cursor cursor) {
        ArrayList<String> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int studentname = 3;
            int mark = 4;
            do {
                data.add(cursor.getString(studentname));
                data.add(cursor.getString(mark));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public ArrayList<String> getDataForGridViewACTION03(Cursor cursor) {
        ArrayList<String> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int mark = 3;
            do {
                data.add(cursor.getString(mark));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

}