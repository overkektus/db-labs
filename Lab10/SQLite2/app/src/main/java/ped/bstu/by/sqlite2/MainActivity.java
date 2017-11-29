package ped.bstu.by.sqlite2;

import android.content.ContentValues;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinnerGroup;
    Button btnAddGroup, btnRemoveGroup, btnSetHead;

    DbHelper dbHelper;
    SQLiteDatabase database;

    ArrayList<Group> groups;
    GroupAdapter groupAdapter;

    ArrayList<Student> students;

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
        groups = new ArrayList<>();
        selectGroup(database);
        groupAdapter = new GroupAdapter(this, groups);
        spinnerGroup.setAdapter(groupAdapter);
    }

    private void initViews() {
        spinnerGroup = (Spinner)findViewById(R.id.spinner);
        btnAddGroup = (Button)findViewById(R.id.buttonMainAddGroup);
        btnAddGroup.setOnClickListener(this);
        btnRemoveGroup = (Button)findViewById(R.id.buttonMainRemoveGroup);
        btnRemoveGroup.setOnClickListener(this);
        btnSetHead = (Button)findViewById(R.id.buttonMainSetHead);
        btnSetHead.setOnClickListener(this);
    }

    private void initDB() {
        dbHelper = new DbHelper(this);
        database = dbHelper.getWritableDatabase();
        //dbHelper.onUpgrade(database, 1, 1);
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
                Group g = (Group) spinnerGroup.getSelectedItem();
                groups.remove(g);
                removeGroupFromDB(g, database);
                Toasty.success(this,"Запись успешно удалена", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonMainSetHead:
                intent = new Intent(this, SetHeadActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void removeGroupFromDB(Group group, SQLiteDatabase database) {
        database.execSQL("delete from " + DbHelper.TABLE_GROUPS + " where " +
                dbHelper.KEY_F + " like " + "\"" + group.getFaculty() + "\"" + " and " +
                dbHelper.KEY_N + " like " + "\"" + group.getName() + "\"" + " and " +
                dbHelper.KEY_C + " like " + "\"" + String.valueOf(group.getCourse()) + "\"");
    }

    private int selectGroup(SQLiteDatabase database) {
        String SelectArg[] = new String[]{null};
        Cursor c = database.rawQuery("select * from " + DbHelper.TABLE_GROUPS, null);
        if(c != null) {
            while(c.moveToNext()) {
                groups.add(new Group(
                    c.getString(1),
                    c.getInt(2),
                    c.getString(3),
                    c.getString(4)
                ));
            }
        } else return -1;
        return 1;
    }

    private boolean initStudents(SQLiteDatabase database) {
        Cursor c = database.rawQuery("select * from " + DbHelper.TABLE_GROUPS, null);
        if(c != null) {
            if(c.moveToFirst()) {
                Student student = new Student(
                        c.getLong(0),
                        c.getLong(1),
                        c.getString(2)
                );
                students.add(student);
            }
            return true;
        } else return false;
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
