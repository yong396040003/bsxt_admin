package com.yong.bsxt_admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.yong.bsxt_admin.jutil.AjaxReturnJson;
import com.yong.bsxt_admin.jutil.ReadExcel;
import com.yong.bsxt_admin.pojo.Bsxt;
import com.yong.bsxt_admin.service.BsxtService;
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
public class BsxtController {
    private final String excel[] = {".xls", ".csv", ".xlsx"};
    @Autowired
    private BsxtService bsxtService;

    /**
     * 插入/修改一条bsxt数据
     *
     * @param bsxt
     * @return
     * @code
     */
    @RequestMapping(value = {"/insertBsxt.do", "/insertBsxt.app"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject insertBsxt(@RequestBody Bsxt bsxt) {
        //根据主键判断是否十插入还是修改 0：插， >0:修改
        //插入数据
        if (bsxt.getId() == 0) {
            return bsxtService.insertBsxt(bsxt);
            //修改数据
        } else {
            return bsxtService.updateBsxt(bsxt);
        }

    }

    /**
     * 查询所有的bsxt信息
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = {"/selectAllBsxt.do", "/selectAllBsxt.app"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject selectAllBsxt(int page, int limit) {
        return bsxtService.selectAllBsxt(page, limit);
    }

    /**
     * 模糊搜索可根据number/name
     *
     * @param search
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = {"/selectBsxtByNumberOrName.do","/selectBsxtByNumberOrName.app"}, method = RequestMethod.GET)
    @ResponseBody
    public JSONObject selectBsxtByNumberOrName(int page, int limit, String search) {
        return bsxtService.selectBsxtByNumberOrName(search, page, limit);
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
    @RequestMapping(value = "selectBsxtByAccSearch.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject selectBsxtByAccSearch(int page, int limit, String number, String name) {
        //匹配前做处理，避免胡乱匹配
        Bsxt bsxt = new Bsxt();

        bsxt.setSelectStudentNum(number);

        bsxt.setTitle(name);

        return bsxtService.selectBsxtByAccSearch(page, limit, bsxt);
    }

    /**
     * 根据code删除bsxt数据
     *
     * @param code
     * @return
     */
    @RequestMapping(value = {"/deleteBsxtByCode.do","/deleteBsxtByCode.app"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteBsxtByCode(String code) {
        return bsxtService.deleteBsxtByNumber(code);
    }

    /**
     * 根据code批量删除bsxt信息
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/bathDeleteBsxtByNumber.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject bathDeleteBsxtByNumber(@RequestParam(value = "data[]") String[] data) {
        return bsxtService.bathDeleteBsxtByNumber(data);
    }

    /**
     * 批量导入数据
     *
     * @param mf
     * @return
     */
    @RequestMapping(value = "/uploadBsxtExcel.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadBsxtExcel(@RequestParam MultipartFile mf) throws IOException {
        //得到文件名
        String fileName = mf.getOriginalFilename();
        //得到输入流
        InputStream inputStream = mf.getInputStream();

        //如果获取到的文件名为空 证明未获取到上传的文件
        if (fileName == null) {
            return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "后台未接受到上传的文件", null);
        }

        List<Bsxt> bsxtList;

        //xls csv xlsx分别解析
        if (fileName.endsWith(excel[0])) {
            try {
                ReadExcel<Bsxt> readExcel = new ReadExcel<>(new Bsxt());
                bsxtList = readExcel.xls(inputStream);
            } catch (Exception e) {
                return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "你使用的xls文件有误\n" + e.getMessage(), null);
            }
        } else if (fileName.endsWith(excel[1])) {
            try {
                ReadExcel<Bsxt> readExcel = new ReadExcel<>(new Bsxt());
                bsxtList = readExcel.csv(inputStream);
            } catch (Exception e) {
                return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "你使用的csv文件有误\n" + e.getMessage(), null);
            }
        } else if (fileName.endsWith(excel[2])) {
            try {
                ReadExcel<Bsxt> readExcel = new ReadExcel<>(new Bsxt());
                bsxtList = readExcel.xlsx(inputStream);
            } catch (Exception e) {
                return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "你使用的xlsx文件有误\n" + e.getMessage(), null);
            }
        } else {
            return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, "未知错误", null);
        }
        inputStream.close();

        String msg = isCheck(bsxtList);

        if (msg != null) {
            return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(0, msg, null);
        }

        return bsxtService.uploadBsxtExcel(bsxtList);
    }

    /**
     * 验证是否合乎规范
     *
     * @param bsxts
     * @return
     */
    private String isCheck(List<Bsxt> bsxts) {
        for (Bsxt bsxt : bsxts) {
            //判断学号是否合乎规范
            if (String.valueOf(bsxt.getSetTeacherNum()).length() != 8) {
                return "教师号：" + bsxt.getSetTeacherNum() + "长度不符合规范，长度默认为8";
            } else if (bsxt.getTitle().length() <= 1) {
                return "题名：" + bsxt.getTitle() + "长度不能小于1";
            }
        }
        return null;
    }

}
