package ped.bstu.by.happybirthday;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    TextView currentDateTime;
    TextView textViewName, textViewSurname, textViewPhone;
    Calendar dateAndTime = Calendar.getInstance();

    private final static String FILENAME = "data.json";
    File path;

    private ArrayList<Person> persons;
    Type itemsListType = new TypeToken<ArrayList<Person>>() {}.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentDateTime = (TextView)findViewById(R.id.currentDateTime);
        textViewName = (TextView)findViewById(R.id.textViewName);
        textViewSurname = (TextView)findViewById(R.id.textViewSurname);
        textViewPhone = (TextView)findViewById(R.id.textViewPhone);
        path = new File(Environment.getExternalStorageDirectory(), FILENAME);
        openFile();
    }

    public void setDate(View v) {
        new DatePickerDialog(MainActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
        clearFields();
    }

    public void clearFields() {
        textViewName.setText("");
        textViewSurname.setText("");
        textViewPhone.setText("");
    }

    private void setInitialDateTime() {
        currentDateTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    public void onClickSearch(View v) {
        String searchDate = currentDateTime.getText().toString();
        for(Person p : persons) {
            String date = p.getBirthday().toString();
            if(searchDate.equals(date)) {
                textViewName.setText(p.getFirstName());
                textViewSurname.setText(p.getSecondName());
                textViewPhone.setText(p.getPhone());

                Toasty.success(this, "Контакт найден!", Toast.LENGTH_SHORT, true).show();
                return;
            }
        }
        Toasty.warning(this, "Контакт не найден!", Toast.LENGTH_SHORT, true).show();
    }

    public void openFile(){
        persons = new ArrayList<Person>();
        FileInputStream fin = null;
        File file = path;
        if(!file.exists()) return;
        try {
            fin =  new FileInputStream(file);
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

}
