package com.example.egor.myapplication;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    private final String FILENAME = "Base_Lab.txt";
    private BufferedWriter bw;
    private FileReader fr;
    private TextView textViewData;
    private EditText editTextSecondName;
    private EditText editTextFirstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextSecondName = (EditText) findViewById(R.id.editTextSecondName);
        textViewData = (TextView) findViewById(R.id.textViewData);

        //deleteFile();
        if(FileExist(FILENAME)){
            textViewData.setText(LoadFile(FILENAME));
        } else {
            File f = new File(super.getFilesDir(), FILENAME);
            try {
                f.createNewFile();
                Log.d("Log_02", "Файл " + FILENAME + " создан");
            } catch (IOException e) {
                Log.d("Log_02", "Файл " + FILENAME + " не создан");
            }
            createDialog();
        }
    }

    private void createDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Создается файл " + FILENAME).setPositiveButton("Да",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Log_02", "Создание файла " + FILENAME);
                    }
                });
        AlertDialog ad = b.create();
        ad.show();
    }

    private void deleteFile() {
        File file = new File(super.getFilesDir(), FILENAME);
        file.delete();
        Log.d("Log_02", "Файл " + FILENAME + " был успешно удален!");
    }

    private boolean FileExist(String FILENAME) {
        boolean rc = false;
        File f = new File(super.getFilesDir(), FILENAME);
        if (rc = f.exists()) {
            Log.d("Log_02", "Файл " + FILENAME + " существует");
        } else {
            Log.d("Log_02", "Файл " + FILENAME + " не найден");
        }
        return rc;
    }

    private String LoadFile(String FILENAME) {
        File file = new File(super.getFilesDir(), FILENAME);
        char buf[] = new char[(int) file.length()];
        try {
            fr = new FileReader(file);
            fr.read(buf);
            Log.d("Log_02", "РџРѕР»СѓС‡РёР» С‚РµРєСЃС‚ " + new String(buf));
        }
        catch (IOException e) {
            Log.d("Log_02", "РќРµ СѓРґР°Р»РѕСЃСЊ РїСЂРѕС‡РёС‚Р°С‚СЊ РёР· С„Р°Р№Р»Р° " + FILENAME);
        }
        Log.d("Log_02", new String(buf));
        return new String(buf);
    }

    private BufferedWriter OpenFile(String FILENAME) {
        File f = new File(super.getFilesDir(), FILENAME);
        BufferedWriter bw = null;

        try {
            FileWriter fw = new FileWriter(f, true);
            bw = new BufferedWriter(fw);
        }
        catch (IOException e) {
            Log.d("Log_02", "Файл " + FILENAME + " не открыт " + e.getMessage());
        }

        return bw;
    }

    private void WriteLine(String surname, String name) {
        String str = surname + ";" + name + "\r\n";
        File file = new File(super.getFilesDir(), FILENAME);
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bufferWriter = new BufferedWriter(fw);
            bufferWriter.write(str);
            bufferWriter.close();
            Log.d("Log_02", "Р”Р°РЅРЅС‹Рµ Р·Р°РїРёСЃР°РЅС‹");
        }
        catch (IOException e) {
            Log.d("Log_02", e.getMessage());
        }
    }

    public void onClickSaveData(View v) {
        String secondName = editTextSecondName.getText().toString();
        String firstName = editTextFirstName.getText().toString();
        WriteLine(secondName, firstName);
        textViewData.setText(LoadFile(FILENAME));
    }

}
