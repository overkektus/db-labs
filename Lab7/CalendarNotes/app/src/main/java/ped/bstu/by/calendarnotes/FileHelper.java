package ped.bstu.by.calendarnotes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Egor on 11.10.2017.
 */

public class FileHelper {

    private final static String FILENAME = "note.json";
    private static final int REQUEST_PERMISSION_WRITE = 1001;

    private static boolean  permissionGranted;

    public static boolean isPermissionGranted() {
        return permissionGranted;
    }

    public static void setPermissionGranted(boolean permissionGranted) {
        FileHelper.permissionGranted = permissionGranted;
    }

    private BufferedWriter bw;
    private static FileReader fr;

    public static boolean fileExist(File filesDir, String FILENAME) {
        boolean rc = false;
        File f = new File(filesDir, FILENAME);
        if (rc = f.exists()) {
            Log.d("Log_02", "Файл " + FILENAME + " существует");
        } else {
            Log.d("Log_02", "Файл " + FILENAME + " не найден");
        }
        return rc;
    }

    public static void WriteLine(String str, File filesDir, String FILENAME) {
        File file = new File(filesDir, FILENAME);
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bufferWriter = new BufferedWriter(fw);
            bufferWriter.write(str);
            bufferWriter.close();
            Log.d("Log_02", "успешно записан");
        }
        catch (IOException e) {
            Log.d("Log_02", e.getMessage());
        }
    }

    public static String LoadFile(File filesDir, String FILENAME) {
        File file = new File(filesDir, FILENAME);
        char buf[] = new char[(int) file.length()];
        try {
            fr = new FileReader(file);
            fr.read(buf);
            Log.d("Log_02", "файл успешно прочитан " + new String(buf));
        }
        catch (IOException e) {
            Log.d("Log_02", "не удалось прочитать файл " + FILENAME);
        }
        Log.d("Log_02", new String(buf));
        return new String(buf);
    }

    public static File getExternalPath() {
        return(new File(Environment.getExternalStorageDirectory(), FILENAME));
    }

    // проверяем, доступно ли внешнее хранилище для чтения и записи
    public static boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        return  Environment.MEDIA_MOUNTED.equals(state);
    }
    // проверяем, доступно ли внешнее хранилище хотя бы только для чтения
    public static boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return  (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    public static boolean checkPermissions(Context context, Activity activity){
        if(!isExternalStorageReadable() || !isExternalStorageWriteable()){
            Toast.makeText(context, "Внешнее хранилище не доступно", Toast.LENGTH_LONG).show();
            return false;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE);
            return false;
        }
        return true;
    }

}