package com.yong.bsxt_admin.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Description:
 * Date: 15:00 2019/12/27
 *
 * @author yong
 * @see
 */
@Entity
public class Student {
    private int id;
    private int number;
    private String name;
    private String password;
    private String sex;
    private String no;
    private int grade;
    private String profession;
    private String classes;
    private long phone;
    private String email;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "number", nullable = false)
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 10)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 20)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "sex", nullable = false, length = 4)
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "no", nullable = false, length = 25)
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Basic
    @Column(name = "grade", nullable = false)
    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Basic
    @Column(name = "profession", nullable = false, length = 25)
    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @Basic
    @Column(name = "classes", nullable = false, length = 8)
    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    @Basic
    @Column(name = "phone", nullable = false)
    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 20)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id &&
                number == student.number &&
                grade == student.grade &&
                phone == student.phone &&
                Objects.equals(name, student.name) &&
                Objects.equals(password, student.password) &&
                Objects.equals(sex, student.sex) &&
                Objects.equals(no, student.no) &&
                Objects.equals(profession, student.profession) &&
                Objects.equals(classes, student.classes) &&
                Objects.equals(email, student.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, number, name, password, sex, no, grade, profession, classes, phone, email);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", no='" + no + '\'' +
                ", grade=" + grade +
                ", profession='" + profession + '\'' +
                ", classes='" + classes + '\'' +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                '}';
    }
}
