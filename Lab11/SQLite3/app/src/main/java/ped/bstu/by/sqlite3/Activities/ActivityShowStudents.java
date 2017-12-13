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

public class ActivityShowStudents extends AppCompatActivity {

    private static final String TAG = "SQLite3.ActShowStud";

    private final String ACTION_01 = "ShowBestStudent";
    private final String ACTION_02 = "ShowBadStudent";

    TextView tvTitleShowStudents;
    GridView gvStudents;

    Long unixDateFrom, unixDateTo;
    String action;
    String selectedFaculty;

    DbHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);

        getExtraDataFromIntent();
        initDB();
        initViews();
        showStudents(selectedFaculty);
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
        tvTitleShowStudents = findViewById(R.id.tvTitleShowStudents);
        if(action.equals(ACTION_01)) {
            tvTitleShowStudents.setText("Список наилучших студентов для всех групп факультета: ");
        } else {
            tvTitleShowStudents.setText("Список студентов, получивших оценки ниже 4, для всех групп факультета");
        }
        gvStudents = findViewById(R.id.gvStudents);
    }

    private void createView() {
        database.execSQL(
                "CREATE VIEW IF NOT EXISTS viewStudents AS " +
                        "SELECT g.groupname, s.name, f.faculty, p.datemark, p.mark FROM student s " +
                        "INNER JOIN progress p ON p.idstudent = s.idstudent " +
                        "INNER JOIN groups g ON g.idgroup = s.idgroup " +
                        "INNER JOIN faculty f ON f.faculty = g.faculty "
        );
    }

    public void showStudents(String selectedFaculty) {
        createView();
        Cursor c = null;
        String sqlQuery;
        switch (action) {
            case ACTION_01:
                /*
                sqlQuery = "SELECT g.groupname, s.name FROM student s "
                        + "INNER JOIN progress p ON p.idstudent = s.idstudent "
                        + "INNER JOIN groups g ON g.idgroup = s.idgroup "
                        + "INNER JOIN faculty f ON f.faculty = g.faculty "
                        + "WHERE f.faculty like \"" + selectedFaculty + "\" "
                        + "and p.datemark BETWEEN " + unixDateFrom + " and " + unixDateTo
                        + " GROUP BY s.name "
                        + "HAVING avg(p.mark) > 8";
                */
                sqlQuery = "SELECT groupname, name FROM viewStudents "
                        + "WHERE faculty like \"" + selectedFaculty + "\" "
                        + "and datemark BETWEEN " + unixDateFrom + " and " + unixDateTo
                        + " GROUP BY name "
                        + "HAVING avg(mark) > 8";
                c = database.rawQuery(sqlQuery, null);
                setAdapterGridView(c);
                break;
            case ACTION_02:
                /*
                sqlQuery = "SELECT g.groupname, s.name FROM student s" +
                        "    INNER JOIN progress p ON p.idstudent = s.idstudent" +
                        "    INNER JOIN groups g ON g.idgroup = s.idgroup" +
                        "    INNER JOIN faculty f ON f.faculty = g.faculty" +
                        "    WHERE f.faculty like \"" + selectedFaculty + "\" " +
                        "    and p.datemark BETWEEN " + unixDateFrom + " and " + unixDateTo +
                        "    and p.mark < 4 " +
                        "   GROUP BY s.name ";
                */
                sqlQuery = "SELECT groupname, name FROM viewStudents" +
                        "    WHERE faculty like \"" + selectedFaculty + "\" " +
                        "    and datemark BETWEEN " + unixDateFrom + " and " + unixDateTo +
                        "    and mark < 4 " +
                        "   GROUP BY name ";
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
        gvStudents.setAdapter(adapter);
    }

    protected ArrayList<String> getTitleForStudentsTable() {
        ArrayList<String> data = new ArrayList<>(Arrays.asList(
                "GROUP",
                "NAME"));
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
