package ped.bstu.by.calendarnotes;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_WRITE = 1001;

    public int curYear, curMonth, curDay;
    public int selectedYear, selectedMonth, selectedDay;

    Button buttonAdd, buttonChange, buttonRemove;
    EditText editTextNote;
    public CalendarView calendar;

    private final static String FILENAME = "note.json";

    private HashMap<String, Note> notes;
    Type itemsListType = new TypeToken<HashMap<String, Note>>() {}.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = (CalendarView)findViewById(R.id.calendarView);
        setCurrentDate();

        editTextNote = (EditText)findViewById(R.id.editTextNote);

        buttonAdd = (Button)findViewById(R.id.buttonAddNote);
        buttonChange = (Button)findViewById(R.id.buttonChangeNote);
        buttonRemove = (Button)findViewById(R.id.buttonRemoveNote);

        if (!FileHelper.fileExist(super.getFilesDir(), FILENAME)) {
            File f = new File(super.getFilesDir(), FILENAME);
            try {
                f.createNewFile();
                Log.d("Log_file", "Файл " + FILENAME + " создан");
            } catch (IOException e) {
                Log.d("Log_file", "Файл " + FILENAME + " не создан");
            }
        }

        openText();
        if(notes == null) notes = new HashMap<String, Note>();

        String key = createKey();
        if(notes.containsKey(key)) {
            changeButtons(true);
            editTextNote.setText(notes.get(key).getText().toString());
        }

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                setSelectedDate(dayOfMonth, month, year);

                String d = String.valueOf(dayOfMonth);
                String m = String.valueOf(month);
                String y = String.valueOf(year);
                if(notes.containsKey(d + m + y)) {
                    changeButtons(true);
                    editTextNote.setText(notes.get(d + m + y).getText());
                } else {
                    changeButtons(false);
                }
            }
        });
    }

    public void saveText(){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(FileHelper.getExternalPath());
            Gson gson = new Gson();
            String json = gson.toJson(notes);
            fos.write(json.getBytes());
        }
        catch(IOException ex) {
            Toasty.error(this, ex.getMessage(), Toast.LENGTH_SHORT, true).show();
        }
        finally {
            try {
                if(fos != null)
                    fos.close();
            }
            catch(IOException ex){
                Toasty.error(this, ex.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }
    }

    public void openText(){
        FileInputStream fin = null;
        File file = FileHelper.getExternalPath();
        if(!file.exists()) return;
        try {
            fin =  new FileInputStream(file);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String json = new String (bytes);
            notes = new Gson().fromJson(json, itemsListType);
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

    private void changeButtons(boolean b) {
        editTextNote.setText("");
        if (b == true) {
            buttonAdd.setVisibility(View.GONE);
            buttonChange.setVisibility(View.VISIBLE);
            buttonRemove.setVisibility(View.VISIBLE);
        }
        else {
            buttonAdd.setVisibility(View.VISIBLE);
            buttonChange.setVisibility(View.GONE);
            buttonRemove.setVisibility(View.GONE);
        }
    }

    private void setSelectedDate(int day, int month, int year) {
        selectedDay = day;
        selectedMonth = month;
        selectedYear = year;
    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH);
        curDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDay = curDay; selectedMonth = curMonth; selectedYear = curYear;
    }

    private String createKey() {
        String key = String.valueOf(selectedDay) +
                String.valueOf(selectedMonth) +
                String.valueOf(selectedYear);
        return key;
    }

    public void onClickAdd(View v) {
        Note note = new Note(editTextNote.getText().toString(), curYear, curMonth, curDay);
        String key = createKey();
        notes.put(key, note);
        changeButtons(true);
        editTextNote.setText(note.getText());
        Toasty.success(MainActivity.this, "Заметка добавлена!", Toast.LENGTH_SHORT, true).show();
        saveText();
    }

    public void onClickChange(View v) {
        String key = createKey();
        String modifyText = editTextNote.getText().toString();
        notes.get(key).setText(modifyText);
        Toasty.info(MainActivity.this, "Заметка изменена!", Toast.LENGTH_SHORT, true).show();
        saveText();
    }

    public void onClickRemove(View v) {
        String key = createKey();
        notes.remove(key);
        changeButtons(false);
        editTextNote.setText("");
        Toasty.error(MainActivity.this, "Заметка была удалена!", Toast.LENGTH_SHORT, true).show();
        saveText();
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

}
