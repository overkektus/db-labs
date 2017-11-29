package task;

import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import task.Task;

/**
 * Created by Egor on 20.10.2017.
 */

public class TaskList {

    private static int MAX_COUNT_ON_DATE = 5;
    private static int MAX_COUNT = 20;

    private HashMap<String, ArrayList<Task>> datesHashMap;

    public TaskList() {
        datesHashMap = new HashMap<>();
    }

    public TaskList(HashMap<String, ArrayList<Task>> datesHashMap) {
        this.datesHashMap = datesHashMap;
    }

    public static String genarateKey(Task task) {
        String key = String.valueOf(task.hashCode());
        return key;
    }

    public void add(Task task) {
        String key = genarateKey(task);
        ArrayList<Task> ar = datesHashMap.get(key);
        ar.add(task);
        datesHashMap.put(key, ar);
    }

    public boolean containsKey(String key) {
        return datesHashMap.containsKey(key);
    }

    public void remove(String key, int id) {
        ArrayList<Task> ar = datesHashMap.get(key);
        for (Task item : ar) {
            if(item.getId() == id) ar.remove(item);
        }
    }

    public boolean isDateFulled(String key) {
        ArrayList<Task> arr = datesHashMap.get(key);
        if (MAX_COUNT_ON_DATE > arr.size())
            return false;
        else return true;
    }

    public boolean isFulled() {
        if (MAX_COUNT > datesHashMap.size())
            return false;
        else return true;
    }
}
