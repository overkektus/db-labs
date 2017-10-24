package ped.bstu.by.contacts;

/**
 * Created by Egor on 12.10.2017.
 */

public class Person {

    private String FirstName;
    private String SecondName;
    private String Phone;
    private String Birthday;

    @Override
    public String toString() {
        return "Person{" +
                "FirstName='" + FirstName + '\'' +
                ", SecondName='" + SecondName + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Birthday='" + Birthday + '\'' +
                '}';
    }

    public Person(String firstName, String secondName, String phone, String birthday) {
        FirstName = firstName;
        SecondName = secondName;
        Phone = phone;
        Birthday = birthday;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getSecondName() {
        return SecondName;
    }

    public void setSecondName(String secondName) {
        SecondName = secondName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }
}
