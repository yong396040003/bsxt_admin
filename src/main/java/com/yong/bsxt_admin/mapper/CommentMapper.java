package com.yong.bsxt_admin.mapper;

import com.yong.bsxt_admin.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评论区
 */
@Mapper
public interface CommentMapper {

    int insertComment(@Param(value = "comment") Comment comment);

    List<Comment> selectCommentByCode(@Param("code") String code);

    int deleteCommentByTime(@Param("time") String time);

    int doCommentByDone(@Param("done") String done);

    List<Comment> selectAllComment();

    int bathDeleteCommentByTime(@Param("data") String[] data);

    @Select("SELECT `name` FROM student WHERE number = '${number}'")
    String selectStudentByNumber(@Param("number") String number);

    @Select("SELECT `name` FROM teacher WHERE number = '${number}'")
    String selectTeacherByNumber(@Param("number") String number);
}
