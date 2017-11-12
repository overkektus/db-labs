package ped.bstu.by.sqlite1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.DropBoxManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editId, editT, editF;

    Button btnInsert, btnDelete, btnUpdate, btnSelect, btnSelectRaw;

    DbHelper dbHelper;

    Long id;
    String t;
    Float f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialStetho();

        editId = (EditText)findViewById(R.id.editTextId);
        editF = (EditText)findViewById(R.id.editTextF);
        editT = (EditText)findViewById(R.id.editTextT);

        btnInsert = (Button) findViewById(R.id.buttonInsert);
        btnInsert.setOnClickListener(this);

        btnDelete = (Button) findViewById(R.id.buttonDelete);
        btnDelete.setOnClickListener(this);

        btnSelect = (Button) findViewById(R.id.buttonSelect);
        btnSelect.setOnClickListener(this);

        btnSelectRaw = (Button) findViewById(R.id.buttonSelectRaw);
        btnSelectRaw.setOnClickListener(this);

        btnUpdate = (Button) findViewById(R.id.buttonUpdate);
        btnUpdate.setOnClickListener(this);

        dbHelper = new DbHelper(this);

        editId.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editF.setText("");
                editT.setText("");
            }
        });
    }


    @Override
    public void onClick(View v) {
        getValues();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        switch (v.getId()) {
            case R.id.buttonInsert:
                long insId = insert(cv, database);
                if(insId > 0) {
                    Toasty.success(this, "Запись успешно вставлена ID = " + insId, 1000).show();
                } else Toasty.error(this, "Не удалось вставить запись", 1000).show();
                break;

            case R.id.buttonSelect:
                Long selId = Long.parseLong(editId.getText().toString());
                if(!select(database)) {
                    Toasty.warning(this, "Не удалось найти запись с ID = " + selId, 1000).show();
                }
                break;

            case R.id.buttonSelectRaw:
                Long selRawId = Long.parseLong(editId.getText().toString());
                int selRawResult = selectRaw(database);
                if(selRawResult < 0) {
                    Toasty.warning(this, "Не удалось найти запись с ID = " + selRawId, 1000).show();
                }
                break;

            case R.id.buttonUpdate:
                if(update(cv, database) > 0) {
                    Toasty.success(this, "Запись успешно обновлена", 1000).show();
                } else Toasty.error(this, "Запись НЕ обновлена", 1000).show();
                break;

            case R.id.buttonDelete:
                if(delete(cv, database) > 0) {
                    Toasty.success(this, "Запись успешно удалена", 1000).show();
                } else Toasty.error(this, "Запись НЕ удалена", 1000).show();
                break;
        }

    }

    private void getValues() {
        if(!editId.getText().toString().equalsIgnoreCase(""))
            id = Long.parseLong(editId.getText().toString());
        if(!editF.getText().toString().equalsIgnoreCase(""))
            f = Float.parseFloat(editF.getText().toString());
        if(!editT.getText().toString().equalsIgnoreCase(""))
            t = editT.getText().toString();
    }

    private boolean select(SQLiteDatabase database) {
        boolean result = false;
        String selection = "_id = ?";
        String[] selectionArgs = new String[]{id.toString()};
        Cursor c = database.query(dbHelper.TABLE_SIMPLETABLE, null, selection, selectionArgs, null, null ,null);
        if(c != null) {
            if(c.moveToFirst()) {
                editId.setText(String.valueOf(c.getLong(0)));
                editF.setText(String.valueOf(c.getFloat(1)));
                editT.setText(c.getString(2));
            }
            result = true;
        }
        return result;
    }

    private int selectRaw(SQLiteDatabase database) {
        String[] selectionArgs = new String[]{id.toString()};
        Cursor c = database.rawQuery("select * from " + DbHelper.TABLE_SIMPLETABLE + " where " +
                dbHelper.KEY_ID + "=?", selectionArgs);
        if(c != null) {
            if(c.moveToFirst()) {
                editId.setText(String.valueOf(c.getLong(0)));
                editF.setText(String.valueOf(c.getFloat(1)));
                editT.setText(c.getString(2));
            }
            return 1;
        } else return -1;
    }

    private long insert(ContentValues cv, SQLiteDatabase database) {
        cv.put(dbHelper.KEY_F, f);
        cv.put(dbHelper.KEY_T, t);

        long insId = database.insert(dbHelper.TABLE_SIMPLETABLE, null, cv);
        return insId;
    }

    private long update(ContentValues cv, SQLiteDatabase database) {
        long updCount = 0;
        if(id != null) {
            cv.put(DbHelper.KEY_F, f);
            cv.put(DbHelper.KEY_T, t);
            updCount = database.update(DbHelper.TABLE_SIMPLETABLE, cv,
                    DbHelper.KEY_ID + "= ?", new String[] {id.toString()});
        }
        return updCount;
    }

    private long delete(ContentValues cv, SQLiteDatabase database) {
        long delCount = 0;
        if(id != null) {
            delCount = database.delete(DbHelper.TABLE_SIMPLETABLE, DbHelper.KEY_ID + "= " + id, null);
        }
        return delCount;
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
