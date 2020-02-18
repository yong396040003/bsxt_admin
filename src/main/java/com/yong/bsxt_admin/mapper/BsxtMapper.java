package com.yong.bsxt_admin.mapper;

import com.yong.bsxt_admin.pojo.Bsxt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Description:
 * Date: 13:42 2019/12/25
 *
 * @author yong
 * @see
 */
@Mapper
public interface BsxtMapper {

    /**
     * 查询所有bsxt信息
     *
     * @return
     */
    @Select("        SELECT bsxt.`id`, CONCAT_WS (\",\",student.`name`,stu.`name`) AS `name`,\n" +
            "        `code`,`title`,`select_student_num`,\n" +
            "        `set_teacher_num`,`select_people`,`upper_people`,`require`,`body`\n" +
            "        FROM `bsxt`,`student`,`student` AS stu\n" +
            "        WHERE \n" +
            "        student.`number`= SUBSTRING_INDEX(bsxt.`select_student_num`,\",\",1)\n" +
            "        AND stu.`number`=SUBSTRING_INDEX(bsxt.`select_student_num`,\",\",-1)")
    List<Bsxt> selectAllBsxt();

    /**
     * 插入一条bsxt数据
     *
     * @param bsxt
     * @return
     */
    int insertBsxt(@Param(value = "bsxt") Bsxt bsxt);

    /**
     * 修改一名bsxt信息
     *
     * @param bsxt
     * @return
     */
    int updateBsxt(@Param(value = "bsxt") Bsxt bsxt);

    /**
     * 根据search在number/name中模糊查询
     *
     * @param search
     * @return
     */
    List<Bsxt> selectBsxtByNumberOrName(@Param("search") String search);

    /**
     * 精确搜索可根据
     * number name no grade profession classes
     *
     * @param bsxt
     * @return
     */
    List<Bsxt> selectBsxtByAccSearch(@Param("bsxt") Bsxt bsxt);

    /**
     * 根据number删除bsxt数据
     *
     * @param number
     * @return
     */
    int deleteBsxtByNumber(@Param("code") String number);

    /**
     * 根据number批量删除bsxt信息
     *
     * @param data
     * @return
     */
    int bathDeleteBsxtByNumber(@Param("data") String[] data);

    /**
     * 批量导入数据
     *
     * @param bsxtList
     * @return
     */
    int uploadBsxtExcel(@Param("bsxtList") List<Bsxt> bsxtList);
}
