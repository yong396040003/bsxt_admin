package com.yong.bsxt_admin.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yong.bsxt_admin.jutil.AjaxReturnJson;
import com.yong.bsxt_admin.jutil.ReadWriteFile;
import com.yong.bsxt_admin.mapper.BsxtMapper;
import com.yong.bsxt_admin.pojo.Bsxt;
import com.yong.bsxt_admin.service.BsxtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * Description:
 * Date: 13:41 2019/12/25
 *
 * @author yong
 * @see
 */
@Service
public class BsxtServiceImpl implements BsxtService {
    @Autowired
    private BsxtMapper bsxtMapper;

    /**
     * 选题编号默认样式
     */
    private String code = "2D1FF665F916430A8ACC5601C926D191";

    @Override
    public JSONObject selectAllBsxt(int page, int limit) {
        //调用pageHelper分页（物理分页）
        PageHelper.startPage(page, limit);

        List<Bsxt> bsxtList = bsxtMapper.selectAllBsxt();

        PageInfo pageInfo = new PageInfo<>(bsxtList);

        //根据目录读取文件
        for (Bsxt bsxt : bsxtList) {
            ReadWriteFile.readRequireAndBody(bsxt);
        }

        return AjaxReturnJson.ajaxReturnJson.getTableReturnJson(0, pageInfo.getTotal(), limit, bsxtList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject insertBsxt(Bsxt bsxt) {
        //毕设选题处死化
        //设置选题学生默认为0
        bsxt.setSelectPeople(0);
        //生成唯一标识码（样式：2D1FF665F916430A8ACC5601C926D191）
        bsxt.setCode(RandomCode());

        if ("".equals(bsxt.getSelectStudentNum()) || bsxt.getSelectStudentNum() == null) {
            bsxt.setSelectStudentNum("11111111,11111111");
        }

        ReadWriteFile.writeRequireAndBody(bsxt);

        int code = 0;

        String msg = "success";

        try {
            code = bsxtMapper.insertBsxt(bsxt);
        } catch (Exception e) {
            //让前台知道为什么会插入失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    /**
     * 随机一个code
     *
     * @return
     */
    private String RandomCode() {
        String ran = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuilder strCode = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < code.length(); i++) {
            int num = random.nextInt(ran.length());
            strCode.append(ran, num, num + 1);
        }

        return strCode.toString();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject updateBsxt(Bsxt bsxt) {
        int code = 0;

        //写入文件
        ReadWriteFile.writeRequireAndBody(bsxt);

        String msg = "success";

        try {
            code = bsxtMapper.updateBsxt(bsxt);
        } catch (Exception e) {
            //让前台知道为什么会修改失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Override
    public JSONObject selectBsxtByNumberOrName(String search, int page, int limit) {
        //调用pageHelper分页（物理分页）
        PageHelper.startPage(page, limit);

        List<Bsxt> bsxtList = bsxtMapper.selectBsxtByNumberOrName(search);

        PageInfo pageInfo = new PageInfo<>(bsxtList);

        //根据目录读取文件
        for (Bsxt bsxt : bsxtList) {
            ReadWriteFile.readRequireAndBody(bsxt);
        }

        return AjaxReturnJson.ajaxReturnJson.getTableReturnJson(0, pageInfo.getTotal(), limit, bsxtList);
    }

    @Override
    public JSONObject selectBsxtByAccSearch(int page, int limit, Bsxt bsxt) {
        //调用pageHelper分页（物理分页）
        PageHelper.startPage(page, limit);

        List<Bsxt> bsxtList = bsxtMapper.selectBsxtByAccSearch(bsxt);

        PageInfo pageInfo = new PageInfo<>(bsxtList);

        //根据目录读取文件
        for (Bsxt bsxt1 : bsxtList) {
            ReadWriteFile.readRequireAndBody(bsxt1);
        }

        return AjaxReturnJson.ajaxReturnJson.getTableReturnJson(0, pageInfo.getTotal(), limit, bsxtList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject deleteBsxtByNumber(String number) {
        int code = 0;

        String msg = "success";

        try {
            code = bsxtMapper.deleteBsxtByNumber(number);
        } catch (Exception e) {
            //让前台知道为什么会修改失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject bathDeleteBsxtByNumber(String[] data) {
        int code = 0;

        String msg = "success";

        try {
            code = bsxtMapper.bathDeleteBsxtByNumber(data);
        } catch (Exception e) {
            //让前台知道为什么会修改失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject uploadBsxtExcel(List<Bsxt> bsxtList) {
        int code = 0;

        String msg = "success";

        //根据写入
        for (Bsxt bsxt : bsxtList) {
            ReadWriteFile.writeRequireAndBody(bsxt);
        }

        try {
            code = bsxtMapper.uploadBsxtExcel(bsxtList);
        } catch (Exception e) {
            //让前台知道为什么上传失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }
}
