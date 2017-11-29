package ped.bstu.by.sqlite2.Group;

/**
 * Created by Egor on 12.11.2017.
 */

public class Group {

    private String Faculty;
    private int Course;
    private String Name;
    private String Head;

    public Group(String faculty, int course, String name, String head) {
        this.Faculty = faculty;
        this.Course = course;
        this.Name = name;
        this.Head = head;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }

    public int getCourse() {
        return Course;
    }

    public void setCourse(int course) {
        Course = course;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getHead() {
        return Head;
    }

    public void setHead(String head) {
        Head = head;
    }
}
