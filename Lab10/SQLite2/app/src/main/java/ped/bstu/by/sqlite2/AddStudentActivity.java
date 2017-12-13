package ped.bstu.by.sqlite2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import ped.bstu.by.sqlite2.DbHelper.DbHelper;
import ped.bstu.by.sqlite2.Group.Group;
import ped.bstu.by.sqlite2.Group.GroupAdapter;

public class AddStudentActivity extends AppCompatActivity {

    EditText etStudentnName;
    Spinner spinnerGroup;

    ArrayList<Group> groups;
    GroupAdapter groupAdapter;

    Long choosedIDgroud;

    DbHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        initDB();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        groups = new ArrayList<>();
        selectGroups(database);
        groupAdapter = new GroupAdapter(this, groups);
        spinnerGroup.setAdapter(groupAdapter);
    }

    private void initDB() {
        dbHelper = new DbHelper(this, "STUDENTSDB", 1);
        database = dbHelper.getWritableDatabase();
    }

    private void initViews() {
        spinnerGroup = findViewById(R.id.spinnerGroupAddStudentActivity);
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final Group selectedGroup = (Group)parent.getItemAtPosition(position);
                String sqlQuery = "SELECT idgroup FROM groups" +
                        " WHERE faculty like \"" + selectedGroup.getFaculty() + "\" " +
                        " and course = " + selectedGroup.getCourse() + " " +
                        " and name like \"" + selectedGroup.getName() + "\";";
                Cursor c = database.rawQuery(sqlQuery, null);
                DbHelper.logCursor(c);
                c.moveToFirst();
                choosedIDgroud = c.getLong(0);
                c.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });
        etStudentnName = findViewById(R.id.etStudentName);
        findViewById(R.id.btnAddStudent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put("idgroup", choosedIDgroud);
                cv.put("studentname", etStudentnName.getText().toString());

                Long res = database.insert(DbHelper.TABLE_STUDENTS, null, cv);
                if(res > 0) showToastMessage(getApplicationContext(), "Студент успешно добавлен","success");
                else showToastMessage(getApplicationContext(), "Ошибка добавления студента", "error");
            }
        });
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
