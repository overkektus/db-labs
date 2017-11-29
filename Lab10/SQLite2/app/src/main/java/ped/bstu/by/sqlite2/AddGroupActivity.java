package ped.bstu.by.sqlite2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

    ArrayList<Group> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        initViews();
        dbHelper = new DbHelper(this);
        database = dbHelper.getWritableDatabase();
    }

    private void initViews() {
        spinnerFaculties = (Spinner) findViewById(R.id.spinnerFaculties);
        spinnerCourses = (Spinner) findViewById(R.id.spinnerCourses);
        editTextNameGroup = (EditText) findViewById(R.id.editTextNameGroup);
        buttonAddGroup = (Button)findViewById(R.id.buttonAddGroupAct);
        buttonAddGroup.setOnClickListener(this);
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
        Group g = new Group(Faculty, Course, Name, null);
        long insId = insert(g, database);
        if(insId > 0) {
            Toasty.success(this, "Запись успешно вставлена ID = " + insId, 1000).show();
        } else Toasty.error(this, "Не удалось вставить запись", 1000).show();
    }

    private long insert(Group g, SQLiteDatabase database) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.KEY_F, g.getFaculty());
        cv.put(DbHelper.KEY_C, g.getCourse());
        cv.put(DbHelper.KEY_N, g.getName());
        cv.put(DbHelper.KEY_H, g.getHead());

        long insId = database.insert(DbHelper.TABLE_GROUPS, null, cv);
        return insId;
    }

    private void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

}
