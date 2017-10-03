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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    String fname = "Base_Lab.txt";

    BufferedWriter bw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FileExist(fname)){
            try {
                LoadFile(fname);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(super.getFilesDir(), fname);
            try {
                f.createNewFile();
                Log.d("Log_02", "Файл " + fname + " создан");
            } catch (IOException e) {
                Log.d("Log_02", "Файл " + fname + " не создан");
            }

            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Создается файл " + fname).setPositiveButton("Да",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("Log_02", "Создание файла " + fname);
                        }
                    });
            AlertDialog ad = b.create();
            ad.show();
        }
    }

    private boolean FileExist(String fname) {
        boolean rc = false;
        File f = new File(super.getFilesDir(), fname);
        if (rc = f.exists()) {
            Log.d("Log_02", "Файл " + fname + " существует");
        } else {
            Log.d("Log_02", "Файл " + fname + " не найден");
        }
        return rc;
    }


    private void LoadFile(String fname) throws FileNotFoundException {
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(fname)));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                Log.d("Lab_02", str);
            }
            ((TextView) findViewById(R.id.textViewData)).setText(str);
            br.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void exists(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
    }

    private BufferedWriter OpenFile(String fname) {
        File f = new File(super.getFilesDir(), fname);
        BufferedWriter bw = null;

        try {
            FileWriter fw = new FileWriter(f, true);
            bw = new BufferedWriter(fw);
        }
        catch (IOException e) {
            Log.d("Log_02", "Файл " + fname + " не открыт " + e.getMessage());
        }

        return bw;
    }

    private void WriteLine(String surname, String name, BufferedWriter bw) {
        if(bw != null) {
            String s = surname + ";" + name + ";" + "\r\n";

            try {
                bw.write(s);
                Log.d("Log_02", "Данные записаны");
            }
            catch (IOException e) {
                Log.d("Log_02", e.getMessage());
            }
        }
    }

    public void onClickSaveData(View v) {
        String secondName = ((EditText) findViewById(R.id.editTextSecondName)).getText().toString();
        String firstName = ((EditText) findViewById(R.id.editTextFirstName)).getText().toString();
        WriteLine(secondName, firstName, bw);
    }

}
