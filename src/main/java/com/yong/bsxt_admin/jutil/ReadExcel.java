package com.yong.bsxt_admin.jutil;

import com.yong.bsxt_admin.pojo.Bsxt;
import com.yong.bsxt_admin.pojo.Student;
import com.yong.bsxt_admin.pojo.Teacher;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * java读取excel文件(.xls,xlsx,csv)工具类
 * Date: 16:20 2019/7/8
 *
 * @author yong
 * @see
 */
public class ReadExcel<T> {
    private T pojo;

    public ReadExcel(T pojo) {
        this.pojo = pojo;
    }

    /**
     * 解析xlsx
     */
    public List<T> xlsx(InputStream inputStream) throws IOException {
        List<T> tList = new ArrayList<>();
        //解析工作簿
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
        //解析工作表
        int size = xssfWorkbook.getNumberOfSheets();
        //循环处理读取的每一个工作表的数据
        for (int i = 0; i < size; i++) {
            XSSFSheet hssfSheet = xssfWorkbook.getSheetAt(i);
            //获取有效行数
            int rowCount = hssfSheet.getLastRowNum();
            for (int j = 1; j <= rowCount; j++) {
                //获取每一行
                XSSFRow row = hssfSheet.getRow(j);
                if (row == null) {
                    break;
                }
                //获取每一行有效列数
                int colCount = row.getLastCellNum();

                Object obj = getPojo(pojo);

                for (int k = 0; k < colCount; k++) {
                    String par;
                    XSSFCell cell = row.getCell(k);
                    if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                        cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    }
                    par = cell.getStringCellValue();
                    insertPojo(k, obj, par);
                }
                tList.add((T) obj);
            }
        }

        xssfWorkbook.close();

        return tList;
    }

    /**
     * 解析csv
     */
    public List<T> csv(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String str;
        List<T> tList = new ArrayList<>();
        Boolean isOne = true;
        while ((str = bufferedReader.readLine()) != null) {
            if (isOne) {
                isOne = false;
                continue;
            }

            Object obj = getPojo(pojo);

            String[] par = str.split(",");
            for (int i = 0; i < par.length; i++) {
                insertPojo(i, obj, par[i]);
            }
            tList.add((T) obj);
        }
        inputStream.close();
        inputStreamReader.close();
        bufferedReader.close();

        return tList;
    }

    /**
     * 解析xls
     */
    public List<T> xls(InputStream inputStream) throws IOException {
        List<T> tList = new ArrayList<>();
        //解析工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
        //解析工作表
        int size = hssfWorkbook.getNumberOfSheets();
        //循环处理读取的每一个工作表的数据
        for (int i = 0; i < size; i++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(i);
            //获取有效行数
            int rowCount = hssfSheet.getLastRowNum();
            for (int j = 0; j <= rowCount; j++) {
                //获取每一行
                HSSFRow row = hssfSheet.getRow(j);
                if (row == null) {
                    break;
                }

               Object obj = getPojo(pojo);

                //获取每一行有效列数
                int colCount = row.getLastCellNum();
                for (int k = 0; k < colCount; k++) {
                    String par;
                    HSSFCell cell = row.getCell(k);
                    if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                        cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    }
                    par = cell.getStringCellValue();
                    insertPojo(k, obj, par);
                }

                tList.add((T) obj);
            }
        }

        hssfWorkbook.close();

        return tList;
    }

    public void insertPojo(int k, Object pojo, String par) {
        if (pojo instanceof Student) {
            setStudentPar(k, (Student) pojo, par);
        } else if (pojo instanceof Teacher) {
            setTeacherPar(k, (Teacher) pojo, par);
        } else if (pojo instanceof Bsxt) {
            setBsxtPar(k, (Bsxt) pojo, par);
        }
    }

    public Object getPojo(T pojo) {
        Object obj = null;
        if (pojo instanceof Student) {
            obj = new Student();
        } else if (pojo instanceof Teacher) {
            obj = new Teacher();
        } else if (pojo instanceof Bsxt) {
            obj = new Bsxt();
        }
        return obj;
    }

    //'number', 'name','sex','no',
    // 'grade','profession','classes','phone','email','password'

    /**
     * 设置学生属性
     *
     * @param k
     * @param student
     * @param par
     */
    public void setStudentPar(int k, Student student, String par) {
        switch (k) {
            case 0:
                student.setNumber(Integer.parseInt(par));
                break;
            case 1:
                student.setName(par);
                break;
            case 2:
                student.setSex(par);
                break;
            case 3:
                student.setNo(par);
                break;
            case 4:
                student.setGrade(Integer.parseInt(par));
                break;
            case 5:
                student.setProfession(par);
                break;
            case 6:
                student.setClasses(par);
                break;
            case 7:
                student.setPhone(Long.parseLong(par));
                break;
            case 8:
                student.setEmail(par);
                break;
            case 9:
                student.setPassword(par);
                break;
            default:
                break;
        }
    }

    /**
     * 设置教师信息
     * @param k
     * @param teacher
     * @param par
     */
    public void setTeacherPar(int k, Teacher teacher, String par) {
        switch (k) {
            case 0:
                teacher.setNumber(Integer.parseInt(par));
                break;
            case 1:
                teacher.setName(par);
                break;
            case 2:
                teacher.setSex(par);
                break;
            case 3:
                teacher.setNo(par);
                break;
            case 4:
                teacher.setPhone(Long.parseLong(par));
                break;
            case 5:
                teacher.setEmail(par);
                break;
            case 6:
                teacher.setPassword(par);
                break;
            default:
                break;
        }
    }

    public void setBsxtPar(int k, Bsxt bsxt, String par) {
        switch (k) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                break;
            case 15:

                break;
            default:
                break;
        }
    }
}
