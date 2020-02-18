package com.yong.bsxt_admin.service;


import com.yong.bsxt_admin.pojo.Manager;

/**
 * Description:
 * Date: 10:36 2019/11/21
 *
 * @author yong
 * @see
 */
public interface LoginService {
    /**
     * 登陆验证
     * 返回登陆用户用户名
     * @param manager
     * @return
     */
    String login(Manager manager);
}
