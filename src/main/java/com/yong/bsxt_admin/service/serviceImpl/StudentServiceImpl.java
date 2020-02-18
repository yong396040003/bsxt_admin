package com.yong.bsxt_admin.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yong.bsxt_admin.jutil.AjaxReturnJson;
import com.yong.bsxt_admin.jutil.MD5;
import com.yong.bsxt_admin.mapper.StudentMapper;
import com.yong.bsxt_admin.pojo.Student;
import com.yong.bsxt_admin.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description:
 * Date: 13:41 2019/12/25
 *
 * @author yong
 * @see
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public JSONObject selectAllStudent(int page, int limit) {
        //调用pageHelper分页（物理分页）
        PageHelper.startPage(page, limit);

        List<Student> studentList = studentMapper.selectAllStudent();

        PageInfo pageInfo = new PageInfo<>(studentList);

        return AjaxReturnJson.ajaxReturnJson.getTableReturnJson(0, pageInfo.getTotal(), limit, studentList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject insertStudent(Student student) {
        int code = 0;

        String msg = "success";

        try {
            //如果未设置密码，那么就是你的学号否则加密后传到服务器
            if (!"".equals(student.getPassword())) {
                student.setPassword(MD5.MD5toString(student.getPassword()));
            } else {
                student.setPassword(MD5.MD5toString(String.valueOf(student.getNumber())));
            }
            code = studentMapper.insertStudent(student);
        } catch (Exception e) {
            //让前台知道为什么会插入失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject updateStudent(Student student) {
        int code = 0;

        String msg = "success";

        try {
            code = studentMapper.updateStudent(student);
        } catch (Exception e) {
            //让前台知道为什么会修改失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Override
    public JSONObject selectStudentByNumberOrName(String search, int page, int limit) {
        //调用pageHelper分页（物理分页）
        PageHelper.startPage(page, limit);

        List<Student> studentList = studentMapper.selectStudentByNumberOrName(search);

        PageInfo pageInfo = new PageInfo<>(studentList);

        return AjaxReturnJson.ajaxReturnJson.getTableReturnJson(0, pageInfo.getTotal(), limit, studentList);
    }

    @Override
    public JSONObject selectStudentByAccSearch(int page, int limit, Student student) {
        //调用pageHelper分页（物理分页）
        PageHelper.startPage(page, limit);

        List<Student> studentList = studentMapper.selectStudentByAccSearch(student);

        PageInfo pageInfo = new PageInfo<>(studentList);

        return AjaxReturnJson.ajaxReturnJson.getTableReturnJson(0, pageInfo.getTotal(), limit, studentList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject deleteStudentByNumber(int number) {
        int code = 0;

        String msg = "success";

        try {
            code = studentMapper.deleteStudentByNumber(number);
        } catch (Exception e) {
            //让前台知道为什么会修改失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject bathDeleteStudentByNumber(int[] data) {
        int code = 0;

        String msg = "success";

        try {
            code = studentMapper.bathDeleteStudentByNumber(data);
        } catch (Exception e) {
            //让前台知道为什么会删除失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject uploadStudentExcel(List<Student> studentList) {
        int code = 0;

        String msg = "success";

        try {
            for (Student student : studentList) {
                if (!"".equals(student.getPassword())) {
                    student.setPassword(MD5.MD5toString(String.valueOf(student.getNumber())));
                } else {
                    student.setPassword(MD5.MD5toString(student.getPassword()));
                }
            }
            code = studentMapper.uploadStudentExcel(studentList);
        } catch (Exception e) {
            //让前台知道为什么会上传失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Override
    public JSONObject studentLogin(int number, String password) {
        int code = 0;

        String msg = "success";

        try {
            code = studentMapper.selectStudent(number, MD5.MD5toString(password));
        } catch (Exception e) {
            //让前台知道为什么会查询失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }
}
