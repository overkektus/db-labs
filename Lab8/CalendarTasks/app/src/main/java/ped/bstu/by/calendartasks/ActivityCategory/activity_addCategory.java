package ped.bstu.by.calendartasks.ActivityCategory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ped.bstu.by.calendartasks.R;

public class activity_addCategory extends AppCompatActivity implements View.OnClickListener {

    EditText etAddCategory;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        etAddCategory = findViewById(R.id.editTextAddCategory);
        btnAdd = findViewById(R.id.btnAddCategoryAdd);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("category", etAddCategory.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
