package ped.bstu.by.sqlite2.Student;

/**
 * Created by Egor on 12.11.2017.
 */

public class Student {

    private Long id;
    private Long id_g;
    private String Name;

    public Student(Long id, Long id_g, String name) {
        this.id = id;
        this.id_g = id_g;
        this.Name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_g() {
        return id_g;
    }

    public void setId_g(Long id_g) {
        this.id_g = id_g;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
