package ped.bstu.by.calendarnotes;

/**
 * Created by Egor on 12.10.2017.
 */

public class Note {

    private static int MAX_NOTE = 10;

    private String text;
    private int year;
    private int month;
    private int day;

    @Override
    public String toString() {
        return "Note{" +
                "text='" + text + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }

    public Note(String text, int year, int month, int day) {
        this.text = text;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static int getMaxNote() {
        return MAX_NOTE;
    }

    public String getText() {
        return text;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}
