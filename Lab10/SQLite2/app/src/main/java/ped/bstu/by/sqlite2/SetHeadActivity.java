package ped.bstu.by.sqlite2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import ped.bstu.by.sqlite2.DbHelper.DbHelper;
import ped.bstu.by.sqlite2.Group.Group;
import ped.bstu.by.sqlite2.Group.GroupAdapter;
import ped.bstu.by.sqlite2.Student.Student;
import ped.bstu.by.sqlite2.Student.StudentAdapter;

public class SetHeadActivity extends AppCompatActivity {

    Spinner spinnerGroup, spinnerStudent;
    Button btnSetHead;

    DbHelper dbHelper;
    SQLiteDatabase database;

    String Faculty, Course, Name;

    ArrayList<Group> groups;
    GroupAdapter groupAdapter;
    Group selectedGroup;

    ArrayList<Student> students;
    StudentAdapter studentAdapter;
    Student selectedStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_head);

        getExtraDataFromIntent();
        initDB();
        initViews();
    }

    private void getExtraDataFromIntent() {
        Intent intent = getIntent();
        Faculty = intent.getStringExtra("Faculty" );
        Course = intent.getStringExtra("Course" );
        Name = intent.getStringExtra("Name");
    }

    private void initDB() {
        dbHelper = new DbHelper(this, "STUDENTSDB", 1);
        database = dbHelper.getWritableDatabase();
    }

    private void initViews() {
        spinnerStudent = findViewById(R.id.spinnerStudentSH);
        spinnerStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStudent = (Student)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinnerGroup = findViewById(R.id.spinnerGroupSH);
        setGroupAdapterForSpinner();
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGroup = (Group)parent.getItemAtPosition(position);
                setStudentAdapterForSpinner(selectedGroup.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });

        findViewById(R.id.btnSetHead).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHead();
            }
        });
    }

    private void setHead() {
        ContentValues cv = new ContentValues();
        cv.put("head", selectedStudent.getName());
        int res = database.update(DbHelper.TABLE_GROUPS, cv, "idgroup=?", new String[]{selectedGroup.getId().toString()});
        if(res > 0) showToastMessage(getApplicationContext(), "Староста назначен!", "success");
        else showToastMessage(getApplicationContext(), "Ошибка в назначении старосты!", "error");
    }

    private void setGroupAdapterForSpinner() {
        groups = new ArrayList<>();
        selectGroups(database);
        groupAdapter = new GroupAdapter(this, groups);
        spinnerGroup.setAdapter(groupAdapter);
    }

    private boolean selectGroups(SQLiteDatabase database) {
        Cursor c = database.rawQuery("select * from " + DbHelper.TABLE_GROUPS, null);
        DbHelper.logCursor(c);
        if(c != null) {
            c.moveToFirst();
            do {
                Group group = new Group(
                        c.getLong(0),
                        c.getString(1),
                        c.getInt(2),
                        c.getString(3),
                        c.getString(4)
                );
                groups.add(group);
            } while (c.moveToNext());
            return true;
        } else {
            return false;
        }
    }

    private void setStudentAdapterForSpinner(Long idgroup) {
        students = new ArrayList<>();
        selectStudents(database, idgroup);
        studentAdapter = new StudentAdapter(this, students);
        spinnerStudent.setAdapter(studentAdapter);
    }

    private boolean selectStudents(SQLiteDatabase database, Long idgroup) {
        Cursor c = database.rawQuery("select * from " + DbHelper.TABLE_STUDENTS + " where idgroup = " + idgroup, null);
        DbHelper.logCursor(c);
        if(c != null) {
            c.moveToFirst();
            do {
                Student student = new Student(
                        c.getLong(1),
                        c.getString(2)
                );
                students.add(student);
            } while (c.moveToNext());
            return true;
        } else {
            return false;
        }
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
