package com.yong.bsxt_admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.yong.bsxt_admin.jutil.AjaxReturnJson;
import com.yong.bsxt_admin.jutil.MD5;
import com.yong.bsxt_admin.jutil.ReadExcel;
import com.yong.bsxt_admin.pojo.Student;
import com.yong.bsxt_admin.service.StudentService;
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
public class StudentController {
    private final String excel[] = {".xls", ".csv", ".xlsx"};
    @Autowired
    private StudentService studentService;

    /**
     * 学生登陆
     *
     * @param number
     * @param password
     * @return
     */
    @RequestMapping(value = {"/studentLogin.do","/studentLogin.app"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject studentLogin(int number, String password) {
        return studentService.studentLogin(number, password);
    }

    /**
     * 插入/修改一条学生数据
     *
     * @param student
     * @return
     * @code
     */
    @RequestMapping(value = {"/insertStudent.do","/insertStudent.app"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject insertStudent(@RequestBody Student student) {
        //根据主键判断是否十插入还是修改 0：插，>0:修改
        //插入数据
        if (student.getId() == 0) {
            return studentService.insertStudent(student);
            //修改数据
        } else {
            return studentService.updateStudent(student);
        }

    }

    /**
     * 查询所有的学生信息
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/selectAllStudent.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject selectAllStudent(int page, int limit) {
        return studentService.selectAllStudent(page, limit);
    }

    /**
     * 模糊搜索可根据number/name
     *
     * @param search
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = {"/selectStudentByNumberOrName.do","/selectStudentByNumberOrName.app"}, method = RequestMethod.GET)
    @ResponseBody
    public JSONObject selectStudentByNumberOrName(int page, int limit, String search) {
        return studentService.selectStudentByNumberOrName(search, page, limit);
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
    @RequestMapping(value = "selectStudentByAccSearch.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject selectStudentByAccSearch(int page, int limit, String number, String name, String no, String grade, String profession, String classes) {
        //匹配前做处理，避免胡乱匹配
        Student student = new Student();
        if (!"".equals(number)) {
            student.setNumber(Integer.parseInt(number));
        }

        if (!"".equals(grade)) {
            student.setGrade(Integer.parseInt(grade));
        }

        student.setName(name);
        student.setNo(no);
        student.setProfession(profession);
        student.setClasses(classes);

        return studentService.selectStudentByAccSearch(page, limit, student);
    }

    /**
     * 根据number删除学生数据
     *
     * @param number
     * @return
     */
    @RequestMapping(value = "deleteStudentByNumber.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteStudentByNumber(String number) {
        System.out.println(number);
        return studentService.deleteStudentByNumber(Integer.parseInt(number));
    }

    /**
     * 根据number批量删除学生信息
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/bathDeleteStudentByNumber.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject bathDeleteStudentByNumber(@RequestParam(value = "data[]") int[] data) {
        return studentService.bathDeleteStudentByNumber(data);
    }

    /**
     * 批量导入数据
     *
     * @param mf
     * @return
     */
    @RequestMapping(value = "/uploadStudentExcel.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadStudentExcel(@RequestParam MultipartFile mf) throws IOException {
        //得到文件名
        String fileName = mf.getOriginalFilename();
        //得到输入流
        InputStream inputStream = mf.getInputStream();

        //如果获取到的文件名为空 证明未获取到上传的文件
        if (fileName == null) {
            return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "后台未接受到上传的文件", null);
        }

        List<Student> studentList;

        //xls csv xlsx分别解析
        if (fileName.endsWith(excel[0])) {
            try {
                ReadExcel<Student> readExcel = new ReadExcel<>(new Student());
                studentList = readExcel.xls(inputStream);
            } catch (Exception e) {
                return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "你使用的xls文件有误\n" + e.getMessage(), null);
            }
        } else if (fileName.endsWith(excel[1])) {
            try {
                ReadExcel<Student> readExcel = new ReadExcel<>(new Student());
                studentList = readExcel.csv(inputStream);
            } catch (Exception e) {
                return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "你使用的csv文件有误\n" + e.getMessage(), null);
            }
        } else if (fileName.endsWith(excel[2])) {
            try {
                ReadExcel<Student> readExcel = new ReadExcel<>(new Student());
                studentList = readExcel.xlsx(inputStream);
            } catch (Exception e) {
                return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "你使用的xlsx文件有误\n" + e.getMessage(), null);
            }
        } else {
            return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "未知错误", null);
        }
        inputStream.close();

        String msg = isCheck(studentList);

        if (msg != null) {
            return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, msg, null);
        }

        return studentService.uploadStudentExcel(studentList);
    }

    /**
     * 验证是否合乎规范
     *
     * @param students
     * @return
     */
    private String isCheck(List<Student> students) {
        for (Student student : students) {
            //判断学号是否合乎规范
            if (String.valueOf(student.getNumber()).length() != 8) {
                return "学号：" + student.getNumber() + "长度不符合规范，长度默认为8";
            } else if (student.getNumber() / 1000000 != student.getGrade() % 100) {
                return "学号：" + student.getNumber() + "前两位需与年级后两位相同";
            } else if (student.getName().length() <= 1) {
                return "姓名：" + student.getName() + "长度不能小于1";
            } else if (!student.getName().matches("[\u4E00-\u9FA5]+")) {
                return "姓名：" + student.getName() + "得为全中文";
            } else {
                student.setPassword("");
            }
        }
        return null;
    }

}
