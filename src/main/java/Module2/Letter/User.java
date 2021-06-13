package Module2.Letter;

import java.io.Serializable;
import java.time.LocalDate;

public class User implements Serializable {
    private Long id;
    private String surname;
    private String name;
    private LocalDate birthday;

    public User(Long id, String surname, String name, LocalDate birthday) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.birthday = birthday;
    }

    public User() {

    }

    public User(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
