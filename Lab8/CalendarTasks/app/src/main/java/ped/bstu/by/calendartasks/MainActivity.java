package ped.bstu.by.calendartasks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Calendar;

import FileHelper.FileHelper;
import category.Category;
import category.CategoryList;
import es.dmoral.toasty.Toasty;
import ped.bstu.by.calendartasks.ActivityCategory.activity_addCategory;
import ped.bstu.by.calendartasks.ActivityCategory.activity_changeCategory;
import ped.bstu.by.calendartasks.ActivityCategory.activity_removeCategory;
import ped.bstu.by.calendartasks.ActivityTask.activity_add;
import ped.bstu.by.calendartasks.ActivityTask.activity_change;
import ped.bstu.by.calendartasks.ActivityTask.activity_remove;
import task.TaskList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String FILENAME_CATEGORY = "category.json";
    Type itemsListType = new TypeToken<CategoryList>() {}.getType();
    File FileDir = Environment.getExternalStorageDirectory();

    Gson gson = new Gson();

    final int REQUEST_CODE_ADD_CATEGORY = 20;
    final int REQUEST_CODE_CHANGE_CATEGORY = 21;
    final int REQUEST_CODE_REMOVE_CATEGORY = 22;

    private static final int REQUEST_PERMISSION_WRITE = 1001;

    private final static String FILENAME = "tasks.xml";

    public CalendarView calendar;
    public int curYear, curMonth, curDay;
    public int selectedYear, selectedMonth, selectedDay;

    Button btnAddTask, btnChangeTask, btnRemoveTask;
    Button btnAddCategory, btnChangeCategory, btnRemoveCategory;

    TaskList tasks;

    CategoryList categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        if (!FileHelper.fileExist(super.getFilesDir(), FILENAME_CATEGORY)) {
            File f = new File(super.getFilesDir(), FILENAME_CATEGORY);
            try {
                f.createNewFile();
                categoryList = new CategoryList();
                Log.d("MainActivity(onCreate)", "Файл " + FILENAME_CATEGORY + " создан");
            } catch (IOException e) {
                Log.d("MainActivity(onCreate)", "Файл " + FILENAME_CATEGORY + " не создан" +
                        " ошибка: " + e.getMessage());
            }
        } else {
            String data = FileHelper.ReadJSON(FileDir, FILENAME_CATEGORY);
            if(data.equals("")) {
                categoryList = gson.fromJson(data, itemsListType);
            }
        }
        if (categoryList == null) categoryList = new CategoryList();
        changeCategoryButtons();
    }

    private void initViews() {
        //For Task
        btnAddTask = findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(this);
        btnChangeTask = findViewById(R.id.btnChangeTask);
        btnChangeTask.setOnClickListener(this);
        btnRemoveTask = findViewById(R.id.btnRemoveTask);
        btnRemoveTask.setOnClickListener(this);

        //For Category
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(this);
        btnChangeCategory = findViewById(R.id.btnChangeCategory);
        btnChangeCategory.setOnClickListener(this);
        btnRemoveCategory = findViewById(R.id.btnRemoveCategory);
        btnRemoveCategory.setOnClickListener(this);

        calendar = findViewById(R.id.calendarView);
        setCurrentDate();

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                setSelectedDate(dayOfMonth, month, year);

                String d = String.valueOf(dayOfMonth);
                String m = String.valueOf(month);
                String y = String.valueOf(year);

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnAddTask:

                break;
            case R.id.btnChangeTask:
                createIntent(activity_change.class);
                break;
            case R.id.btnRemoveTask:
                createIntent(activity_remove.class);
                break;

            case R.id.btnAddCategory:
                intent = new Intent(this, activity_addCategory.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_CATEGORY);
                break;
            case R.id.btnChangeCategory:
                intent = new Intent(this, activity_changeCategory.class);
                startActivityForResult(intent, REQUEST_CODE_CHANGE_CATEGORY);
                break;
            case R.id.btnRemoveCategory:
                intent = new Intent(this, activity_removeCategory.class);
                startActivityForResult(intent, REQUEST_CODE_REMOVE_CATEGORY);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MainActivity(onActRes)", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADD_CATEGORY:
                    String category = data.getStringExtra("category");
                    if(!categoryList.isFulled()) {
                        if(categoryList.add(new Category(category))) {
                            FileHelper.WriteJSON(FileDir, FILENAME_CATEGORY, categoryList);
                            changeCategoryButtons();
                            Toasty.success(this, "Категория успешно добавлена", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toasty.error(this, "Такая категория уже существует", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toasty.warning(this,"Достигнуто максимальное кол-во категорий", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case REQUEST_CODE_CHANGE_CATEGORY:
                    String oldCategory = data.getStringExtra("oldCategory");
                    String newCategory = data.getStringExtra("newCategory");
                    Log.d("MainActivity(RC_CHANGE)", "oldCategory: " + oldCategory + " " + "newCategory: " + newCategory);
                    int index = categoryList.indexOf(oldCategory);
                    categoryList.remove(index);
                    categoryList.add(new Category(newCategory));
                    FileHelper.WriteJSON(FileDir, FILENAME_CATEGORY, categoryList);
                    break;
                case REQUEST_CODE_REMOVE_CATEGORY:
                    String cat = data.getStringExtra("category");
                    Log.d("MainActivity(RC_REMOVE)", "rmCategory: " + cat);
                    int indexRM = categoryList.indexOf(cat);
                    Log.d("MainActivity(RC_REMOVE)", "indexRM: " + indexRM);
                    if(indexRM >= 0) {
                        categoryList.remove(indexRM);
                        Toasty.success(this,"Категория удалена", Toast.LENGTH_SHORT).show();
                    } else {
                        Toasty.error(this,"Ошибка удаления категории", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private void changeTaskButtons() {
        if(tasks.isFulled()) {
            btnAddTask.setVisibility(View.GONE);
            btnChangeTask.setVisibility(View.GONE);
            btnRemoveTask.setVisibility(View.GONE);
            Toasty.warning(this, "Создано максимальное число задача!", Toast.LENGTH_SHORT);
        } else {
            if(tasks.containsKey(createKey())) {
                btnChangeTask.setVisibility(View.VISIBLE);
                btnRemoveTask.setVisibility(View.VISIBLE);
                if(tasks.isDateFulled(createKey())) {
                    btnAddTask.setVisibility(View.VISIBLE);
                } else {
                    Toasty.warning(this, "Создано максимальное число задача на выбранную дату!", Toast.LENGTH_SHORT);
                }
            } else {
                btnAddTask.setVisibility(View.VISIBLE);
                btnChangeTask.setVisibility(View.VISIBLE);
                btnRemoveTask.setVisibility(View.VISIBLE);
            }
        }
    }

    private void changeCategoryButtons() {
        btnAddCategory.setVisibility(categoryList.isFulled() ? View.GONE : View.VISIBLE);
        Log.d("isEmpty", "" + categoryList.isEmpty());
        btnChangeCategory.setVisibility(categoryList.isEmpty() ? View.GONE : View.VISIBLE);
        btnRemoveCategory.setVisibility(categoryList.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private String createKey() {
        String key = String.valueOf(selectedDay) +
                String.valueOf(selectedMonth) +
                String.valueOf(selectedYear);
        return key;
    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH);
        curDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDay = curDay; selectedMonth = curMonth; selectedYear = curYear;
    }

    private void setSelectedDate(int day, int month, int year) {
        selectedDay = day;
        selectedMonth = month;
        selectedYear = year;
    }

    private void createIntent(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("key", createKey());
        startActivity(intent);
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
