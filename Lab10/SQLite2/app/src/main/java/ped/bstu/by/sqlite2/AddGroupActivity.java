package ped.bstu.by.sqlite2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import ped.bstu.by.sqlite2.DbHelper.DbHelper;
import ped.bstu.by.sqlite2.Group.Group;

public class AddGroupActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinnerFaculties, spinnerCourses;
    EditText editTextNameGroup;
    Button buttonAddGroup;

    String Faculty, Name;
    Integer Course;

    DbHelper dbHelper;
    SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        initViews();
        initDB();
    }

    private void initViews() {
        spinnerFaculties = (Spinner) findViewById(R.id.spinnerFaculties);
        spinnerCourses = (Spinner) findViewById(R.id.spinnerCourses);
        editTextNameGroup = (EditText) findViewById(R.id.editTextNameGroup);
        buttonAddGroup = (Button) findViewById(R.id.buttonAddGroupAct);
        buttonAddGroup.setOnClickListener(this);
    }

    private void initDB() {
        dbHelper = new DbHelper(this, "STUDENTSDB", 1);
        database = dbHelper.getWritableDatabase();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddGroupAct:
                addGroup();
                break;
        }
    }

    private void addGroup() {
        getData();
        putToDB();
        goBack();
    }

    private void getData() {
        Faculty = spinnerFaculties.getSelectedItem().toString();
        Course = Integer.valueOf(spinnerCourses.getSelectedItem().toString());
        Name = editTextNameGroup.getText().toString();
    }

    private void putToDB() {
        ContentValues cv = new ContentValues();

        cv.put("faculty", Faculty);
        cv.put("course", Course);
        cv.put("name", Name);

        long insId = database.insert(dbHelper.TABLE_GROUPS, null, cv);
        if(insId > 0) {
            showToastMessage(getApplicationContext(), "Запись успешно вставлена ID = " + insId, "success");
            database.close();
        } else {
            showToastMessage(getApplicationContext(), "Не удалось вставить запись", "error");
        }
    }


    private void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showToastMessage(Context context, String text, String type) {
        switch (type) {
            case "success":
                Toasty.success(context, text, Toast.LENGTH_SHORT).show();
                break;
            case "warning":
                Toasty.warning(context, text, Toast.LENGTH_SHORT).show();
                break;
            case "info":
                Toasty.info(context, text, Toast.LENGTH_SHORT).show();
                break;
            case "error":
                Toasty.error(context, text, Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
