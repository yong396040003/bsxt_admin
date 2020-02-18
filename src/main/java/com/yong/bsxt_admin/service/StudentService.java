package com.yong.bsxt_admin.service;

import com.alibaba.fastjson.JSONObject;
import com.yong.bsxt_admin.pojo.Student;

import java.util.List;

/**
 * Description:
 * Date: 13:39 2019/12/25
 *
 * @author yong
 * @see
 */
public interface StudentService {

    /**
     * 分页查询所有学生
     * @param page
     * @param limit
     * @return
     */
    JSONObject selectAllStudent(int page,int limit);

    /**
     * 插入一条学生数据
     * @param student
     * @return
     */
    JSONObject insertStudent(Student student);

    /**
     * 修改一名学生信息
     * @param student
     * @return
     */
    JSONObject updateStudent(Student student);

    /**
     * 模糊搜索可根据number/name
     * @param search
     * @param page
     * @param limit
     * @return
     */
    JSONObject selectStudentByNumberOrName(String search, int page, int limit);

    /**
     * 精确搜索可根据
     * number name no grade profession classes
     *
     * @param page
     * @param limit
     * @param student
     * @return
     */
    JSONObject selectStudentByAccSearch(int page,int limit,Student student);

    /**
     * 根据number删除学生数据
     *
     * @param number
     * @return
     */
    JSONObject deleteStudentByNumber(int number);

    /**
     * 根据number批量删除学生信息
     *
     * @param data
     * @return
     */
    JSONObject bathDeleteStudentByNumber(int[] data);

    /**
     * 批量导入数据
     *
     * @param studentList
     * @return
     */
    JSONObject uploadStudentExcel(List<Student> studentList);

    /**
     * 学生登陆
     * @param number
     * @param password
     * @return
     */
    JSONObject studentLogin(int number, String password);
}
