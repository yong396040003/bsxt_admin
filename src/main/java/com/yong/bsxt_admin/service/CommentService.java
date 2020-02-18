package com.yong.bsxt_admin.service;

import com.alibaba.fastjson.JSONObject;
import com.yong.bsxt_admin.pojo.Comment;

/**
 * 评论
 */
public interface CommentService {

    JSONObject insertComment(Comment comment);

    JSONObject selectCommentByCode(int page, int limit, String code);

    JSONObject deleteCommentByTime(String time);

    JSONObject doCommentByDone(String done);

    JSONObject selectAllComment(int page, int limit);

    JSONObject bathDeleteCommentByTime(String[] data);
}
