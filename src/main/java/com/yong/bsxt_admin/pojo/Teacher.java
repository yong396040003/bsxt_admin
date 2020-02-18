package com.yong.bsxt_admin.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Description:
 * Date: 18:11 2019/12/23
 *
 * @author yong
 * @see
 */
@Entity
public class Teacher {
    private int id;
    private int number;
    private String name;
    private String password;
    private String sex;
    private String no;
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
    @Column(name = "phone", nullable = false)
    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 20)
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
        Teacher teacher = (Teacher) o;
        return id == teacher.id &&
                number == teacher.number &&
                phone == teacher.phone &&
                Objects.equals(name, teacher.name) &&
                Objects.equals(password, teacher.password) &&
                Objects.equals(sex, teacher.sex) &&
                Objects.equals(no, teacher.no) &&
                Objects.equals(email, teacher.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, number, name, password, sex, no, phone, email);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", no='" + no + '\'' +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                '}';
    }
}
