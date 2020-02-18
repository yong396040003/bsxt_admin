package com.yong.bsxt_admin.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yong.bsxt_admin.jutil.AjaxReturnJson;
import com.yong.bsxt_admin.mapper.CommentMapper;
import com.yong.bsxt_admin.pojo.Comment;
import com.yong.bsxt_admin.pojo.Student;
import com.yong.bsxt_admin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public JSONObject insertComment(Comment comment) {
        int code = 0;

        String msg = "success";

        String name = "";

        String number = String.valueOf(comment.getNumber());
        if (number.length() == 8) {
            name = commentMapper.selectStudentByNumber(number);
        } else if (number.length() == 10) {
            name = commentMapper.selectTeacherByNumber(number);
        }

        if (!"".equals(name)) {
            comment.setName(name);
        }

        try {
            code = commentMapper.insertComment(comment);
        } catch (Exception e) {
            //让前台知道为什么会插入失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Override
    public JSONObject selectCommentByCode(int page, int limit, String code) {
        //调用pageHelper分页（物理分页）
        PageHelper.startPage(page, limit);

        List<Comment> commentList = commentMapper.selectCommentByCode(code);

        PageInfo pageInfo = new PageInfo<>(commentList);

        return AjaxReturnJson.ajaxReturnJson.getTableReturnJson(0, pageInfo.getTotal(), limit, commentList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject deleteCommentByTime(String time) {
        int code = 0;

        String msg = "success";

        try {
            code = commentMapper.deleteCommentByTime(time);
        } catch (Exception e) {
            //让前台知道为什么会修改失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Override
    public JSONObject doCommentByDone(String done) {
        int code = 0;

        String msg = "success";

        try {
            code = commentMapper.doCommentByDone(done);
        } catch (Exception e) {
            //让前台知道为什么会插入失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    @Override
    public JSONObject selectAllComment(int page, int limit) {
        //调用pageHelper分页（物理分页）
        PageHelper.startPage(page, limit);

        List<Comment> commentList = commentMapper.selectAllComment();

        PageInfo pageInfo = new PageInfo<>(commentList);

        return AjaxReturnJson.ajaxReturnJson.getTableReturnJson(0, pageInfo.getTotal(), limit, commentList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject bathDeleteCommentByTime(String[] data) {
        int code = 0;

        String msg = "success";

        try {
            code = commentMapper.bathDeleteCommentByTime(data);
        } catch (Exception e) {
            //让前台知道为什么会修改失败
            msg = e.getMessage();
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }
}
