package com.yong.bsxt_admin.mapper;

import com.yong.bsxt_admin.pojo.Student;
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
public interface StudentMapper {

    /**
     * 查询所有学生信息
     *
     * @return
     */
    @Select("SELECT * FROM student")
    List<Student> selectAllStudent();

    /**
     * 插入一条学生数据
     *
     * @param student
     * @return
     */
    int insertStudent(@Param(value = "student") Student student);

    /**
     * 修改一名学生信息
     *
     * @param student
     * @return
     */
    int updateStudent(@Param(value = "student") Student student);

    /**
     * 根据search在number/name中模糊查询
     *
     * @param search
     * @return
     */
    List<Student> selectStudentByNumberOrName(@Param("search") String search);

    /**
     * 精确搜索可根据
     * number name no grade profession classes
     *
     * @param student
     * @return
     */
    List<Student> selectStudentByAccSearch(@Param("student") Student student);

    /**
     * 根据number删除学生数据
     *
     * @param number
     * @return
     */
    int deleteStudentByNumber(@Param("number") int number);

    /**
     * 根据number批量删除学生信息
     *
     * @param data
     * @return
     */
    int bathDeleteStudentByNumber(@Param("data") int[] data);

    /**
     * 批量导入数据
     *
     * @param studentList
     * @return
     */
    int uploadStudentExcel(@Param("studentList") List<Student> studentList);

    /**
     * 查询该学生
     *
     * @param number
     * @param password
     * @return
     */
    @Select("SELECT COUNT(*) FROM student WHERE number = '${number}' AND `password`='${password}'")
    int selectStudent(@Param("number") int number, @Param("password") String password);
}
