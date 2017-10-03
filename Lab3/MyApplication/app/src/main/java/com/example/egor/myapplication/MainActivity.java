package com.example.egor.myapplication;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import static android.os.Environment.DIRECTORY_MUSIC;

public class MainActivity extends AppCompatActivity {

    File file;
    String m = Environment.getExternalStorageState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickFileType(View v) {
        switch (v.getId()){
            case R.id.FilesDir:
                file = this.getFilesDir();
                break;
            case R.id.Cache:
                file = this.getCacheDir();
                break;
            case R.id.ExternalFiles:
                file = this.getExternalFilesDir(DIRECTORY_MUSIC);
                break;
            case R.id.ExternalCache:
                file = this.getExternalCacheDir();
                break;
            case R.id.ExternalStorage:
                file = Environment.getExternalStorageDirectory();
                break;
            case R.id.ExternalStoragePublic:
                file = Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC);
                break;
        }
        if(file == null){
            ((TextView) findViewById(R.id.AbsolutePath)).setText("");
            ((TextView) findViewById(R.id.Name)).setText("");
            ((TextView) findViewById(R.id.Path)).setText("");
            ((TextView) findViewById(R.id.ReadWrite)).setText("");
            ((TextView) findViewById(R.id.External)).setText("");
            return;
        }
        ((TextView) findViewById(R.id.AbsolutePath)).setText(file.getAbsolutePath());
        ((TextView) findViewById(R.id.Name)).setText(file.getName());
        ((TextView) findViewById(R.id.Path)).setText(file.getPath());
        String canRead;
        if(file.canRead()) {
            canRead = "yes";
        } else {
            canRead = "no";
        }

        String canWrite;
        if(file.canWrite()) {
            canWrite = "yes";
        } else {
            canWrite = "no";
        }
        ((TextView) findViewById(R.id.ReadWrite)).setText(canRead + "/" + canWrite);
        ((TextView) findViewById(R.id.External)).setText(Environment.getExternalStorageState());
    }

}
