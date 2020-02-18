package com.yong.bsxt_admin.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Description:
 * Date: 16:19 2020/1/1
 *
 * @author yong
 * @see
 */
@Entity
public class Bsxt {
    private int id;
    private String code;
    private String title;
    private String selectStudentNum;
    private Integer selectPeople;
    private int upperPeople;
    private String require;
    private String body;
    private int setTeacherNum;
    /**
     * 选题学生（学生之间用字符串隔开）
     */
    private String name;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "code", nullable = false, length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 50)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "select_student_num", nullable = true, length = 20)
    public String getSelectStudentNum() {
        return selectStudentNum;
    }

    public void setSelectStudentNum(String selectStudentNum) {
        this.selectStudentNum = selectStudentNum;
    }

    @Basic
    @Column(name = "select_people", nullable = true)
    public Integer getSelectPeople() {
        return selectPeople;
    }

    public void setSelectPeople(Integer selectPeople) {
        this.selectPeople = selectPeople;
    }

    @Basic
    @Column(name = "upper_people", nullable = false)
    public int getUpperPeople() {
        return upperPeople;
    }

    public void setUpperPeople(int upperPeople) {
        this.upperPeople = upperPeople;
    }

    @Basic
    @Column(name = "require", nullable = true, length = 30)
    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }

    @Basic
    @Column(name = "body", nullable = true, length = 30)
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bsxt bsxt = (Bsxt) o;
        return id == bsxt.id &&
                upperPeople == bsxt.upperPeople &&
                Objects.equals(code, bsxt.code) &&
                Objects.equals(title, bsxt.title) &&
                Objects.equals(selectStudentNum, bsxt.selectStudentNum) &&
                Objects.equals(selectPeople, bsxt.selectPeople) &&
                Objects.equals(require, bsxt.require) &&
                Objects.equals(body, bsxt.body);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, code, title, selectStudentNum, selectPeople, upperPeople, require, body);
    }

    @Basic
    @Column(name = "set_teacher_num", nullable = false)
    public int getSetTeacherNum() {
        return setTeacherNum;
    }

    public void setSetTeacherNum(int setTeacherNum) {
        this.setTeacherNum = setTeacherNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
