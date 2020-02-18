package com.yong.bsxt_admin.service;

import com.alibaba.fastjson.JSONObject;
import com.yong.bsxt_admin.pojo.Teacher;

import java.util.List;

/**
 * Description:
 * Date: 13:39 2019/12/25
 *
 * @author yong
 * @see
 */
public interface TeacherService {

    /**
     * 分页查询所有学生
     * @param page
     * @param limit
     * @return
     */
    JSONObject selectAllTeacher(int page, int limit);

    /**
     * 插入一条学生数据
     * @param teacher
     * @return
     */
    JSONObject insertTeacher(Teacher teacher);

    /**
     * 修改一名学生信息
     * @param teacher
     * @return
     */
    JSONObject updateTeacher(Teacher teacher);

    /**
     * 模糊搜索可根据number/name
     * @param search
     * @param page
     * @param limit
     * @return
     */
    JSONObject selectTeacherByNumberOrName(String search, int page, int limit);

    /**
     * 精确搜索可根据
     * number name no grade profession classes
     *
     * @param page
     * @param limit
     * @param teacher
     * @return
     */
    JSONObject selectTeacherByAccSearch(int page, int limit, Teacher teacher);

    /**
     * 根据number删除学生数据
     *
     * @param number
     * @return
     */
    JSONObject deleteTeacherByNumber(int number);

    /**
     * 根据number批量删除学生信息
     *
     * @param data
     * @return
     */
    JSONObject bathDeleteTeacherByNumber(int[] data);

    /**
     * 批量导入数据
     *
     * @param teacherList
     * @return
     */
    JSONObject uploadTeacherExcel(List<Teacher> teacherList);

    /**
     * 毕设选题教师名联想
     * @param query
     * @return
     */
    JSONObject selectTeacherByName(String query);

    /**
     * 教师登陆
     * @param number
     * @param password
     * @return
     */
    JSONObject teacherLogin(int number, String password);
}
