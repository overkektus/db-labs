package category;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Egor on 20.10.2017.
 */

public class CategoryList {

    private static int MAX_COUNT = 5;

    ArrayList<Category> categories;

    public CategoryList() {
        categories = new ArrayList<>();
    }

    public ArrayList<Category> getList () {
        return categories;
    }

    public boolean add(Category cat) {
        if(categories.indexOf(cat) == -1) {
            categories.add(categories.size(), cat);
            return true;
        } else return false;
    }

    public int indexOf(String cat) {
        int index = -1;
        for (Category item: categories) {
            if(item.getCategory().equals(cat)){
                index = categories.indexOf(item);
                return index;
            }
        }
        return index;
    }

    public void remove(int i) {
        categories.remove(i);
    }

    @Override
    public String toString() {
        return "";
    }

    public boolean isEmpty() {
        if(categories.size() < 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isFulled() {
        if (MAX_COUNT > categories.size())
            return false;
        else return true;
    }

}
