package ped.bstu.by.sqlite1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editId = (EditText)findViewById(R.id.editTextId);
        editF = (EditText)findViewById(R.id.editTextF);
        editT = (EditText)findViewById(R.id.editTextT);

    }

    public void Insert(View v) {

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
