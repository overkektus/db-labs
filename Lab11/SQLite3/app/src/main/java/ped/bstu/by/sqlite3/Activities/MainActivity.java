package ped.bstu.by.sqlite3.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.facebook.stetho.Stetho;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ped.bstu.by.sqlite3.Activities.ActivityShowListGroup;
import ped.bstu.by.sqlite3.DbHelper;
import ped.bstu.by.sqlite3.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    Spinner spinnerActions, spinnerPeriod, spinnerFaculties1, getSpinnerFaculties2;
    EditText etPeriodShowListGroupFrom, etPeriodShowListGroupTo;

    DbHelper dbHelper;
    SQLiteDatabase database;

    Date dateFrom, dateTo;
    Long unixDateFrom = 0L, unixDateTo = 0L;

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
        spinnerPeriod = findViewById(R.id.spinnerPeriod);
        spinnerFaculties1 = findViewById(R.id.spinnerFaculties1);
        findViewById(R.id.btnShowListGroup).setOnClickListener(this);
        etPeriodShowListGroupFrom = findViewById(R.id.etPeriodShowListGroupFrom);
        etPeriodShowListGroupTo = findViewById(R.id.etPeriodShowListGroupTo);
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
            case R.id.btnShowBestGroup:
                getSelectedDate();
                showStudents("ShowBestStudent");
                break;
            case R.id.btnShowBadStudent:
                getSelectedDate();
                showStudents("ShowBadStudent");
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
        Intent intent = new Intent(this, ActivityShowListGroup.class);
        intent.putExtra("Action", action);
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
