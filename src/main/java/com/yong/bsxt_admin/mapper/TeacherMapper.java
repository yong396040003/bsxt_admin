package com.yong.bsxt_admin.mapper;

import com.yong.bsxt_admin.pojo.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Description:
 * Date: 13:42 2019/12/25
 *
 * @author yong
 * @see
 */
@Mapper
public interface TeacherMapper {

    /**
     * 查询所有教师信息
     *
     * @return
     */
    @Select("SELECT * FROM teacher")
    List<Teacher> selectAllTeacher();

    /**
     * 插入一条教师数据
     *
     * @param teacher
     * @return
     */
    int insertTeacher(@Param(value = "teacher") Teacher teacher);

    /**
     * 修改一名教师信息
     *
     * @param teacher
     * @return
     */
    int updateTeacher(@Param(value = "teacher") Teacher teacher);

    /**
     * 根据search在number/name中模糊查询
     *
     * @param search
     * @return
     */
    List<Teacher> selectTeacherByNumberOrName(@Param("search") String search);

    /**
     * 精确搜索可根据
     * number name no grade profession classes
     *
     * @param teacher
     * @return
     */
    List<Teacher> selectTeacherByAccSearch(@Param("teacher") Teacher teacher);

    /**
     * 根据number删除教师数据
     *
     * @param number
     * @return
     */
    int deleteTeacherByNumber(@Param("number") int number);

    /**
     * 根据number批量删除教师信息
     *
     * @param data
     * @return
     */
    int bathDeleteTeacherByNumber(@Param("data") int[] data);

    /**
     * 批量导入数据
     *
     * @param teacherList
     * @return
     */
    int uploadTeacherExcel(@Param("teacherList") List<Teacher> teacherList);

    /**
     * 教师名联想
     *
     * @param query
     * @return
     */
    List<Teacher> selectTeacherByName(@Param("query") String query);

    @Select("SELECT COUNT(*) FROM teacher WHERE number = '${number}' AND `password`='${password}'")
    int selectTeacher(@Param("number") int number, @Param("password") String password);
}
