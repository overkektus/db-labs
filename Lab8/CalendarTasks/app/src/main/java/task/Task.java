package task;

import java.util.HashMap;

import category.Category;
/**
 * Created by Egor on 20.10.2017.
 */

public class Task {

    private int id;
    private String text;
    private int year;
    private int month;
    private int day;
    private Category category;

    private static int count = 0;

    public Task(String text, int year, int month, int day, Category cat) {
        this.id = count;
        this.text = text;
        this.year = year;
        this.month = month;
        this.day = day;
        this.category = cat;
        count++;
    }

    public int getId() { return id; }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Category getCategory() { return category; }

    public void setCategory(Category cat) { this.category = cat; }

    @Override
    public String toString() {
        return "Task{" +
                "text='" + text + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", category=" + category.toString() +
                '}';
    }

}
