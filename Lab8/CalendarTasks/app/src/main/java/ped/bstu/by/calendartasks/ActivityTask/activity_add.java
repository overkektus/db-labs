package ped.bstu.by.calendartasks.ActivityTask;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import FileHelper.FileHelper;
import es.dmoral.toasty.Toasty;
import ped.bstu.by.calendartasks.R;


public class activity_add extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_WRITE = 1001;

    private final static String FILENAME = "tasks.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case REQUEST_PERMISSION_WRITE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    FileHelper.setPermissionGranted(true);
                    Toasty.success(activity_add.this, "Разрешения получены", Toast.LENGTH_SHORT, true).show();
                }
                else{
                    Toasty.error(activity_add.this, "Необходимо дать разрешения!", Toast.LENGTH_SHORT, true).show();
                }
                break;
        }
    }
}
