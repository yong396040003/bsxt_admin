package com.yong.bsxt_admin.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yong.bsxt_admin.jutil.AjaxReturnJson;
import com.yong.bsxt_admin.jutil.MD5;
import com.yong.bsxt_admin.mapper.TeacherMapper;
import com.yong.bsxt_admin.pojo.Teacher;
import com.yong.bsxt_admin.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Date: 13:41 2019/12/25
 *
 * @author yong
 * @see
 */
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public JSONObject selectAllTeacher(int page, int limit) {
        //调用pageHelper分页（物理分页）
        PageHelper.startPage(page, limit);

        List<Teacher> teacherList = teacherMapper.selectAllTeacher();

        PageInfo pageInfo = new PageInfo<>(teacherList);

        return AjaxReturnJson.ajaxReturnJson.getTableReturnJson(0, pageInfo.getTotal(), limit, teacherList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject insertTeacher(Teacher teacher) {
        int code = 0;

        String msg = "success";

        try {
            if ("".equals(teacher.getPassword())) {
                teacher.setPassword(MD5.MD5toString(String.valueOf(teacher.getNumber())));
            } else {
                teacher.setPassword(MD5.MD5toString(teacher.getPassword()));
            }
            code = teacherMapper.insertTeacher(teacher);
        } catch (Exception e) {
            //让前台知道为什么会插入失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject updateTeacher(Teacher teacher) {
        int code = 0;

        String msg = "success";

        try {
            code = teacherMapper.updateTeacher(teacher);
        } catch (Exception e) {
            //让前台知道为什么会修改失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Override
    public JSONObject selectTeacherByNumberOrName(String search, int page, int limit) {
        //调用pageHelper分页（物理分页）
        PageHelper.startPage(page, limit);

        List<Teacher> teacherList = teacherMapper.selectTeacherByNumberOrName(search);

        PageInfo pageInfo = new PageInfo<>(teacherList);

        return AjaxReturnJson.ajaxReturnJson.getTableReturnJson(0, pageInfo.getTotal(), limit, teacherList);
    }

    @Override
    public JSONObject selectTeacherByAccSearch(int page, int limit, Teacher teacher) {
        //调用pageHelper分页（物理分页）
        PageHelper.startPage(page, limit);

        List<Teacher> teacherList = teacherMapper.selectTeacherByAccSearch(teacher);

        PageInfo pageInfo = new PageInfo<>(teacherList);

        return AjaxReturnJson.ajaxReturnJson.getTableReturnJson(0, pageInfo.getTotal(), limit, teacherList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject deleteTeacherByNumber(int number) {
        int code = 0;

        String msg = "success";

        try {
            code = teacherMapper.deleteTeacherByNumber(number);
        } catch (Exception e) {
            //让前台知道为什么会修改失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject bathDeleteTeacherByNumber(int[] data) {
        int code = 0;

        String msg = "success";

        try {
            code = teacherMapper.bathDeleteTeacherByNumber(data);
        } catch (Exception e) {
            //让前台知道为什么会修改失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject uploadTeacherExcel(List<Teacher> teacherList) {
        int code = 0;

        String msg = "success";

        try {
            for (Teacher teacher : teacherList) {
                if (!"".equals(teacher.getPassword())) {
                    teacher.setPassword(MD5.MD5toString(String.valueOf(teacher.getNumber())));
                } else {
                    teacher.setPassword(MD5.MD5toString(teacher.getPassword()));
                }
            }
            code = teacherMapper.uploadTeacherExcel(teacherList);
        } catch (Exception e) {
            //让前台知道为什么会修改失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Override
    public JSONObject selectTeacherByName(String query) {
        List<Teacher> teacherList = teacherMapper.selectTeacherByName(query);
        List<String> stringList = new ArrayList<>();

        if (teacherList.size() != 0) {
            for (Teacher teacher : teacherList) {
                stringList.add(teacher.getName() + "(" + teacher.getNumber() + ")");
            }
        }

        JSONObject jsonObject = new JSONObject();
        if (stringList.size() == 0) {
            stringList.add("未找到对应教师数据");
            jsonObject.put("suggestions", stringList);
        } else {
            jsonObject.put("suggestions", stringList);
        }
        return jsonObject;
    }

    @Override
    public JSONObject teacherLogin(int number, String password) {
        int code = 0;

        String msg = "success";

        try {
            code = teacherMapper.selectTeacher(number, MD5.MD5toString(password));
        } catch (Exception e) {
            //让前台知道为什么会修改失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }
}
