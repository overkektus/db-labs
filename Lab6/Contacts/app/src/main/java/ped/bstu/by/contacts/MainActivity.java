package ped.bstu.by.contacts;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity  {

    private static final int REQUEST_PERMISSION_WRITE = 1001;

    private final String FILENAME = "data.json";
    public File NO_ACC_FILE;
    public File ACC_FILE;

    public int curYear, curMonth, curDay;
    public int selectedYear, selectedMonth, selectedDay;

    TextView currentDate;
    Calendar dateAndTime = Calendar.getInstance();

    private EditText editTextFirstName;
    private EditText editTextSecondName;
    private EditText editTextPhone;
    private EditText editTextNameSearch;
    private EditText editeTextSurnameSearch;
    private TextView date;

    private ArrayList<Person> persons;

    Type itemsListType = new TypeToken<ArrayList<Person>>() {}.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentDate = (TextView)findViewById(R.id.textViewDate);
        editTextFirstName = (EditText) findViewById(R.id.editTextName);
        editTextSecondName = (EditText) findViewById(R.id.editTextSurname);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextNameSearch = (EditText) findViewById(R.id.editTextNameSearch);
        editeTextSurnameSearch = (EditText) findViewById(R.id.editTextSurnameSearch);
        date = (TextView) findViewById(R.id.textViewDate);
        setInitialDate();
        NO_ACC_FILE = new File(super.getFilesDir(), FILENAME);
        ACC_FILE = new File(Environment.getExternalStorageDirectory(), FILENAME);
        openFile();
    }

    public void openFile(){
        persons = new ArrayList<Person>();
        FileInputStream fin = null;
        if(!NO_ACC_FILE.exists()) return;
        try {
            fin =  new FileInputStream(ACC_FILE);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String json = new String (bytes);
            persons = new Gson().fromJson(json, itemsListType);
        }
        catch(IOException ex) {
            Toasty.error(this, ex.getMessage(), Toast.LENGTH_SHORT, true).show();
        }
        finally {
            try {
                if(fin != null)
                    fin.close();
            }
            catch(IOException ex){
                Toasty.error(this, ex.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }
    }

    public void saveFile(View v){
        FileOutputStream fosA = null, fosN = null;
        try {
            fosA = new FileOutputStream(ACC_FILE);
            fosN = new FileOutputStream(NO_ACC_FILE);
            Gson gson = new Gson();
            String json = gson.toJson(persons);
            fosA.write(json.getBytes());
            fosN.write(json.getBytes());
        }
        catch(IOException ex) {
            Toasty.error(this, ex.getMessage(), Toast.LENGTH_SHORT, true).show();
        }
        finally {
            try {
                if(fosA != null)
                    fosA.close();
                if(fosN != null)
                    fosN.close();
                Toasty.info(this, "Файл успешно сохранен!", Toast.LENGTH_SHORT, true).show();
            }
            catch(IOException ex){
                Toasty.error(this, ex.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }
    }

    public void setDate(View v) {
        new DatePickerDialog(MainActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialDate() {
        currentDate.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    private Person getPerson() {
        Person person = new Person(
                editTextFirstName.getText().toString(),
                editTextSecondName.getText().toString(),
                editTextPhone.getText().toString(),
                date.getText().toString()
        );

        return person;
    }

    public void onClickSearch(View v) {
        String name = editTextNameSearch.getText().toString();
        String surname = editeTextSurnameSearch.getText().toString();
        clearSearchFields();
        for(Person p : persons) {
            if(p.getFirstName().equals(name) && p.getSecondName().equals(surname)) {
                showSearchResult(p);
                return;
            }
        }
        showSearchResult(null);
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };

    public void onClickAdd(View v) {
        Person pers = getPerson();
        persons.add(persons.size(), pers);
        Toasty.success(this, "Контак добавлен!", Toast.LENGTH_SHORT, true).show();
        clearAddFields();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case REQUEST_PERMISSION_WRITE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    FileHelper.setPermissionGranted(true);
                    Toasty.success(MainActivity.this, "Разрешения получены", Toast.LENGTH_SHORT, true).show();
                }
                else{
                    Toasty.error(MainActivity.this, "Необходимо дать разрешения!", Toast.LENGTH_SHORT, true).show();
                }
                break;
        }
    }

    private void showSearchResult(Person p) {
        String message;
        if(p != null) {
            message = p.getFirstName() + " " +
                    p.getSecondName() + " " +
                    p.getBirthday();
        } else message = "Контакт не найден!";

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Результат поиска")
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("ок",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void clearAddFields() {
        editTextFirstName.setText("");
        editTextSecondName.setText("");
        editTextPhone.setText("");
    }

    private void clearSearchFields() {
        editTextNameSearch.setText("");
        editeTextSurnameSearch.setText("");
    }

}
