package com.yong.bsxt_admin.service.serviceImpl;

import com.yong.bsxt_admin.mapper.LoginMapper;
import com.yong.bsxt_admin.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Date: 10:37 2019/11/21
 *
 * @author yong
 * @see
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;


    @Override
    public String login(com.yong.bsxt_admin.pojo.Manager manager) {
        return loginMapper.login(manager);
    }
}
