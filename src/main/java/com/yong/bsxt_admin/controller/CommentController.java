package com.yong.bsxt_admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.yong.bsxt_admin.pojo.Comment;
import com.yong.bsxt_admin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 手机端（评论区）
 */
@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 插入一条评论区
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertComment.app", method = RequestMethod.POST)
    public JSONObject insertComment(@RequestBody Comment comment) {
        return commentService.insertComment(comment);
    }

    /**
     * 查询所有的评论
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/selectAllComment.do", "/selectAllComment.app"}, method = RequestMethod.POST)
    public JSONObject selectAllComment(int page, int limit) {
        return commentService.selectAllComment(page, limit);
    }

    /**
     * 根据code查询评论
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectCommentByCode.app", method = RequestMethod.POST)
    public JSONObject selectCommentByCode(int page, int limit, String code) {
        return commentService.selectCommentByCode(page, limit, code);
    }

    /**
     * 根据时间time删除评论
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"deleteCommentByTime.do", "/deleteCommentByTime.app"}, method = RequestMethod.POST)
    public JSONObject deleteCommentByTime(String time) {
        return commentService.deleteCommentByTime(time);
    }

    /**
     * 根据时间time批量删除
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/bathDeleteCommentByTime.do", method = RequestMethod.POST)
    public JSONObject bathDeleteCommentByTime(@RequestParam(value = "data[]") String[] data) {
        return commentService.bathDeleteCommentByTime(data);
    }

    /**
     * 点赞/取消点赞
     * done:
     * add(添加)
     * del(删除)
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/doCommentByDone.app", method = RequestMethod.POST)
    public JSONObject doCommentByDone(String done) {
        return commentService.doCommentByDone(done);
    }
}
