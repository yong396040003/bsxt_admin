package com.yong.bsxt_admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.yong.bsxt_admin.jutil.AjaxReturnJson;
import com.yong.bsxt_admin.jutil.ReadExcel;
import com.yong.bsxt_admin.pojo.Teacher;
import com.yong.bsxt_admin.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Description:
 * Date: 21:21 2019/12/24
 *
 * @author yong
 * @see
 */
@Controller
public class TeacherController {
    private final String excel[] = {".xls", ".csv", ".xlsx"};
    @Autowired
    private TeacherService teacherService;


    /**
     * 教师登陆
     *
     * @param number
     * @param password
     * @return
     */
    @RequestMapping(value = {"/teacherLogin.do", "/teacherLogin.app"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject teacherLogin(int number, String password) {
        return teacherService.teacherLogin(number, password);
    }

    /**
     * 插入/修改一条教师数据
     *
     * @param teacher
     * @return
     * @code
     */
    @RequestMapping(value = {"/insertTeacher.do", "/insertTeacher.app"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject insertTeacher(@RequestBody Teacher teacher) {
        //根据主键判断是否十插入还是修改 0：插，>0:修改
        //插入数据
        if (teacher.getId() == 0) {
            return teacherService.insertTeacher(teacher);
            //修改数据
        } else {
            return teacherService.updateTeacher(teacher);
        }

    }

    /**
     * 查询所有的教师信息
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = {"/selectAllTeacher.do", "/selectAllTeacher.app"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject selectAllTeacher(int page, int limit) {
        return teacherService.selectAllTeacher(page, limit);
    }

    /**
     * 模糊搜索可根据number/name
     *
     * @param search
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = {"selectTeacherByNumberOrName.do", "selectTeacherByNumberOrName.app"}, method = RequestMethod.GET)
    @ResponseBody
    public JSONObject selectTeacherByNumberOrName(int page, int limit, String search) {
        return teacherService.selectTeacherByNumberOrName(search, page, limit);
    }

    /**
     * 精确搜索可根据
     * number name no grade profession classes
     *
     * @param page
     * @param limit
     * @param number
     * @return
     */
    @RequestMapping(value = "selectTeacherByAccSearch.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject selectTeacherByAccSearch(int page, int limit, String number, String name, String no) {
        //匹配前做处理，避免胡乱匹配
        Teacher teacher = new Teacher();
        if (!"".equals(number)) {
            teacher.setNumber(Integer.parseInt(number));
        }

        teacher.setName(name);
        teacher.setNo(no);

        return teacherService.selectTeacherByAccSearch(page, limit, teacher);
    }

    /**
     * 根据number删除教师数据
     *
     * @param number
     * @return
     */
    @RequestMapping(value = "deleteTeacherByNumber.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteTeacherByNumber(String number) {
        System.out.println(number);
        return teacherService.deleteTeacherByNumber(Integer.parseInt(number));
    }

    /**
     * 根据number批量删除教师信息
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/bathDeleteTeacherByNumber.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject bathDeleteTeacherByNumber(@RequestParam(value = "data[]") int[] data) {
        return teacherService.bathDeleteTeacherByNumber(data);
    }


    /**
     * 根据教师姓名联想
     *
     * @param query
     * @return
     */
    @RequestMapping(value = "/selectTeacherByName.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject selectTeacherByName(String query) {
        return teacherService.selectTeacherByName(query);
    }

    /**
     * 批量导入数据
     *
     * @param mf
     * @return
     */
    @RequestMapping(value = "/uploadTeacherExcel.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadTeacherExcel(@RequestParam MultipartFile mf) throws IOException {
        //得到文件名
        String fileName = mf.getOriginalFilename();
        //得到输入流
        InputStream inputStream = mf.getInputStream();

        //如果获取到的文件名为空 证明未获取到上传的文件
        if (fileName == null) {
            return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "后台未接受到上传的文件", null);
        }

        List<Teacher> teacherList;

        //xls csv xlsx分别解析
        if (fileName.endsWith(excel[0])) {
            try {
                ReadExcel<Teacher> readExcel = new ReadExcel<>(new Teacher());
                teacherList = readExcel.xls(inputStream);
            } catch (Exception e) {
                return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "你使用的xls文件有误\n" + e.getMessage(), null);
            }
        } else if (fileName.endsWith(excel[1])) {
            try {
                ReadExcel<Teacher> readExcel = new ReadExcel<>(new Teacher());
                teacherList = readExcel.csv(inputStream);
            } catch (Exception e) {
                return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "你使用的csv文件有误\n" + e.getMessage(), null);
            }
        } else if (fileName.endsWith(excel[2])) {
            try {
                ReadExcel<Teacher> readExcel = new ReadExcel<>(new Teacher());
                teacherList = readExcel.xlsx(inputStream);
            } catch (Exception e) {
                return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "你使用的xlsx文件有误\n" + e.getMessage(), null);
            }
        } else {
            return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "未知错误", null);
        }
        inputStream.close();

        System.out.println(teacherList.toString());

        String msg = isCheck(teacherList);

        if (msg != null) {
            return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, msg, null);
        }

        return teacherService.uploadTeacherExcel(teacherList);
    }

    /**
     * 验证是否合乎规范
     *
     * @param teachers
     * @return
     */
    private String isCheck(List<Teacher> teachers) {
        for (Teacher teacher : teachers) {
            //判断学号是否合乎规范
            if (String.valueOf(teacher.getNumber()).length() != 10) {
                return "教师号：" + teacher.getNumber() + "长度不符合规范，长度默认为8";
            } else if (teacher.getName().length() <= 1) {
                return "教师姓名：" + teacher.getName() + "长度不能小于1";
            } else if (!teacher.getName().matches("[\u4E00-\u9FA5]+")) {
                return "教师姓名：" + teacher.getName() + "得为全中文";
            } else {
                teacher.setPassword("");
            }
        }
        return null;
    }


}
