package com.yong.bsxt_admin.mapper;

import com.yong.bsxt_admin.pojo.Manager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Description:
 * Date: 10:38 2019/11/21
 *
 * @author yong
 * @see
 */
@Mapper
public interface LoginMapper {
    /**
     * 登陆验证
     *
     * @param manager
     * @return
     */
    @Select("SELECT userName FROM `manager` WHERE phone = \"${manager.phone}\" AND `password` = \"${manager.password}\"")
    String login(@Param("manager") Manager manager);
}
