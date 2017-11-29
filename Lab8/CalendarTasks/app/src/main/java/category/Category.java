package category;

import java.util.ArrayList;

/**
 * Created by Egor on 20.10.2017.
 */

public class Category {

    private String category;

    public Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Category{" +
                "category='" + category + '\'' +
                '}';
    }
}
