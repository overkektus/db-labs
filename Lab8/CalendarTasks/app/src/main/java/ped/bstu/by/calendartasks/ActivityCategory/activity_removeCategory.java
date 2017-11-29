package ped.bstu.by.calendartasks.ActivityCategory;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import FileHelper.FileHelper;
import category.Category;
import category.CategoryList;
import ped.bstu.by.calendartasks.R;

public class activity_removeCategory extends AppCompatActivity implements View.OnClickListener {

    private final static String FILENAME_CATEGORY = "category.json";
    Type itemsListType = new TypeToken<CategoryList>() {}.getType();
    File FileDir = Environment.getExternalStorageDirectory();

    List<String> dataForSpinner = new ArrayList<>();

    Spinner spinner;
    Button btnRemove;

    CategoryList categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_category);
        String data = FileHelper.ReadJSON(FileDir, FILENAME_CATEGORY);
        categoryList = new Gson().fromJson(data, itemsListType);
        initViews();
    }

    private void initViews() {
        spinner = findViewById(R.id.spinnerRemoveCategory);
        Adapter adapter = createAdapter();
        spinner.setAdapter((SpinnerAdapter) adapter);
        btnRemove = findViewById(R.id.btnRemoveCategoryRemove);
        btnRemove.setOnClickListener(this);
    }

    private Adapter createAdapter() {
        ArrayList<Category> categoryArrayList = categoryList.getList();
        for (Category item : categoryArrayList) {
            dataForSpinner.add(item.getCategory());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                dataForSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.putExtra("category", spinner.getSelectedItem().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
