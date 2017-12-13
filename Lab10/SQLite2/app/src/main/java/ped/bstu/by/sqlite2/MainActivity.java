package ped.bstu.by.sqlite2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import ped.bstu.by.sqlite2.DbHelper.DbHelper;
import ped.bstu.by.sqlite2.Group.Group;
import ped.bstu.by.sqlite2.Group.GroupAdapter;
import ped.bstu.by.sqlite2.Student.Student;
import ped.bstu.by.sqlite2.Student.StudentAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SQLite2.MainActivity";

    Spinner spinnerGroup;
    Button btnAddGroup, btnRemoveGroup, btnSetHead;

    Spinner spinnerStudent;
    Button btnAddStudent, btnRemoveStudent;

    DbHelper dbHelper;
    SQLiteDatabase database;

    ArrayList<Group> groups;
    GroupAdapter groupAdapter;
    Group selectedGroup;

    ArrayList<Student> students;
    StudentAdapter studentAdapter;
    Student selectedStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialStetho();
        initDB();
        initViews();
    }

    @Override
    protected void onResume(){
        super.onResume();
        setGroupAdapterForSpinner();
        setStudentAdapterForSpinner();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setGroupAdapterForSpinner();
        setStudentAdapterForSpinner();
    }

    private void initViews() {
        spinnerGroup = (Spinner) findViewById(R.id.spinnerGroup);
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGroup = (Group)parent.getItemAtPosition(position);
                Log.d(TAG, selectedGroup.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });
        btnAddGroup = (Button) findViewById(R.id.buttonMainAddGroup);
        btnAddGroup.setOnClickListener(this);
        btnRemoveGroup = (Button) findViewById(R.id.buttonMainRemoveGroup);
        btnRemoveGroup.setOnClickListener(this);
        btnSetHead = (Button) findViewById(R.id.buttonMainSetHead);
        btnSetHead.setOnClickListener(this);

        spinnerStudent = (Spinner) findViewById(R.id.spinnerStudent);
        spinnerStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStudent = (Student)parent.getItemAtPosition(position);
                Log.d(TAG, selectedStudent.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });
        btnAddStudent = findViewById(R.id.buttonMainAddStudent);
        btnAddStudent.setOnClickListener(this);
        btnRemoveStudent = findViewById(R.id.buttonMainRemoveStudent);
        btnRemoveStudent.setOnClickListener(this);
    }

    private void initDB() {
        //this.deleteDatabase("STUDENTSDB");
        dbHelper = new DbHelper(this, "STUDENTSDB", 1);
        database = dbHelper.getWritableDatabase();
    }

    private void setGroupAdapterForSpinner() {
        groups = new ArrayList<>();
        selectGroups(database);
        groupAdapter = new GroupAdapter(this, groups);
        spinnerGroup.setAdapter(groupAdapter);
    }

    private void setStudentAdapterForSpinner() {
        students = new ArrayList<>();
        selectStudents(database);
        studentAdapter = new StudentAdapter(this, students);
        spinnerStudent.setAdapter(studentAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonMainAddGroup:
                intent = new Intent(this, AddGroupActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonMainRemoveGroup:
                int delGroupCount = database.delete(DbHelper.TABLE_GROUPS, "idgroup = " + selectedGroup.getId(), null);
                if (delGroupCount > 0) {
                    setGroupAdapterForSpinner();
                    setStudentAdapterForSpinner();
                    showToastMessage(getApplicationContext(), "Группа успешно удалена!", "success");
                } else {
                    showToastMessage(getApplicationContext(), "Ошибка удаления группы!", "error");
                }
                break;
            case R.id.buttonMainSetHead:
                intent = new Intent(this, SetHeadActivity.class);
                intent.putExtra("Faculty", selectedGroup.getFaculty());
                intent.putExtra("Course", selectedGroup.getCourse());
                intent.putExtra("Name", selectedGroup.getName());
                startActivity(intent);
                break;

            case R.id.buttonMainAddStudent:
                intent = new Intent(this, AddStudentActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonMainRemoveStudent:
                int delStudentCount = database.delete(DbHelper.TABLE_STUDENTS,
                        "studentname = '" + selectedStudent.getName() + "'", null);
                if (delStudentCount > 0) {
                    setStudentAdapterForSpinner();
                    showToastMessage(getApplicationContext(), "Студент с именем " + selectedStudent.getName() + " удален!", "success");
                } else {
                    showToastMessage(getApplicationContext(), "Ошибка удаления студента", "error");
                }
                break;
        }
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

    private boolean selectStudents(SQLiteDatabase database) {
        Cursor c = database.rawQuery("select * from " + DbHelper.TABLE_STUDENTS, null);
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
