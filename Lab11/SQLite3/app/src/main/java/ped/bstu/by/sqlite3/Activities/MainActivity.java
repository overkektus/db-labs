package ped.bstu.by.sqlite3.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.facebook.stetho.Stetho;

import ped.bstu.by.sqlite3.Activities.ActivityShowListGroup;
import ped.bstu.by.sqlite3.DbHelper;
import ped.bstu.by.sqlite3.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinnerActions, spinnerPeriod, spinnerFaculties1, getSpinnerFaculties2;

    DbHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialStetho();
        setContentView(R.layout.activity_main);
        initDB();
        initViews();
    }

    private void initViews() {
        spinnerActions = findViewById(R.id.spinnerAction);
        spinnerPeriod = findViewById(R.id.spinnerPeriod);
        spinnerFaculties1 = findViewById(R.id.spinnerFaculties1);
        findViewById(R.id.btnShowListGroup).setOnClickListener(this);
    }

    private void initDB() {
        dbHelper = new DbHelper(this, "STUDENTDB", 4);
        database = dbHelper.getWritableDatabase();
        //dbHelper.onUpgrade(database, 1, 1);
        //this.deleteDatabase("STUDENTSDB");
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.btnShowListGroup:
                showListGroup();
                break;
            case R.id.btnShowBestGroup:
                showBestGroup();
                break;
            case R.id.btnShowBadStudent:
                showBadStudent();
                break;
        }
    }

    private void showListGroup() {
        Intent intent = new Intent(this, ActivityShowListGroup.class);
        String action = (String)spinnerActions.getSelectedItem();
        String period = (String)spinnerPeriod.getSelectedItem();
        switch (action) {
            case "каждого студента по предмету":
                intent.putExtra("Action", "forStudentOnSubject");
                intent.putExtra("Period", period);
                startActivity(intent);
                break;
            case "студента в целом":
                intent.putExtra("Action", "forStudent");
                intent.putExtra("Period", period);
                startActivity(intent);
                break;
            case "группы в целом":
                intent.putExtra("Action", "forGroup");
                intent.putExtra("Period", period);
                startActivity(intent);
                break;
        }
    }

    private void showBestGroup() {

    }

    private void showBadStudent() {

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
