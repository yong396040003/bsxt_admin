package com.yong.bsxt_admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.yong.bsxt_admin.jutil.AjaxReturnJson;
import com.yong.bsxt_admin.jutil.MD5;
import com.yong.bsxt_admin.pojo.Manager;
import com.yong.bsxt_admin.pojo.MySession;
import com.yong.bsxt_admin.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Description:
 * Date: 9:46 2019/11/20
 *
 * @author yong
 * @see
 */
@Controller
public class LoginController {
    /**
     * 自定义session对象用来做单点登陆
     */
    public static MySession mySession;
    @Autowired
    private LoginService loginService;

    /**
     * 登陆操作
     *
     * @return
     */
    @PostMapping("/login.do")
    @ResponseBody
    public JSONObject login(long phone, String encode, HttpServletRequest request) {

        Manager manager = new Manager();

        manager.setPhone(phone);
        manager.setPassword(MD5.MD5toString(encode));

        String name = null;

        String msg = "账号或密码错误";
        int code = -1;

        //拿到session
        HttpSession session = request.getSession();
        mySession = MySession.getInstance();

        String sessionId = null;
        long tel = 0;

        if (mySession != null) {
            sessionId = mySession.getSessionId();
            tel = mySession.getPhone();
        }
        //当已经登陆再切换回来时直接跳转
        if (sessionId != null
                && sessionId.equals(session.getId())
                && tel == manager.getPhone()) {
            msg = "登陆成功";
            code = 0;
        } else {
            name = loginService.login(manager);
            if (name != null && sessionId == null) {
                msg = "登陆成功";
                code = 0;
                mySession.setPhone(phone);
                mySession.setSessionId(session.getId());
            } else if (name != null) {
                //异地登陆操作
                msg = "登陆成功";
                code = 1;
            }
        }

        //防止用于频繁登陆
        if (name != null) {
            mySession.setName(name);
        }

        return AjaxReturnJson.ajaxReturnJson.getMsgReturnJson(code, msg, null);
    }

    /**
     * 登陆踢人操作
     *
     * @param manager
     * @param request
     */
    @PostMapping("/loginKick.do")
    @ResponseBody
    public String loginKick(@RequestBody Manager manager, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        mySession.setPhone(manager.getPhone());
        mySession.setSessionId(session.getId());
        return "success";
    }

    /**
     * 退出操作
     *
     */
    @GetMapping("/loginExit.do")
    public String loginExit() {
        mySession = null;
        return "login";
    }
}
