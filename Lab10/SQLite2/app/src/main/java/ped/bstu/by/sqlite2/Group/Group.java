package ped.bstu.by.sqlite2.Group;

/**
 * Created by Egor on 12.11.2017.
 */

public class Group {

    private Long id;
    private String Faculty;
    private int Course;
    private String Name;
    private String Head;

    public Group(Long id, String faculty, int course, String name, String head) {
        this.id = id;
        this.Faculty = faculty;
        this.Course = course;
        this.Name = name;
        setHead(head);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", Faculty='" + Faculty + '\'' +
                ", Course=" + Course +
                ", Name='" + Name + '\'' +
                ", Head='" + Head + '\'' +
                '}';
    }
}
