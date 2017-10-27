package ped.bstu.by.sqlite1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText editId, editT, editF;

    DbHelper sqlHelper;
    SQLiteDatabase db;
    Cursor cursor;

    String t;
    Long id;
    Float f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editId = (EditText)findViewById(R.id.editTextId);
        editF = (EditText)findViewById(R.id.editTextF);
        editT = (EditText)findViewById(R.id.editTextT);

    }

    private void getValues() {
        id = Long.parseLong(editId.getText().toString());
        f = Float.parseFloat(editF.getText().toString());
        t = editT.getText().toString();
    }

    public void Insert(View v) {
        getValues();

        ContentValues cv = new ContentValues();
        cv.put(sqlHelper.COLUMN_ID, id);
        cv.put(sqlHelper.COLUMN_F, f);
        cv.put(sqlHelper.COLUMN_T, t);

        db.insert(sqlHelper.TABLE, null, cv);
    }

    public void Select(View v) {

    }

    public void SelectRaw(View v) {

    }

    public void Update(View v) {

    }

    public void Delete(View v) {

    }

}
