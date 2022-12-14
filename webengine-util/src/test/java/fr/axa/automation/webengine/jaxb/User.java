package fr.axa.automation.webengine.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Users")
@XmlType(propOrder = {
        "id",
        "firstName",
        "lastName",
        "age",
        "gender"
})
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String password;

    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @XmlTransient
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + ", gender=" +
                gender + "]";
    }
}