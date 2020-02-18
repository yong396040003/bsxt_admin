package com.yong.bsxt_admin.pojo;

import java.io.Serializable;

/**
 * Description:
 * Date: 11:03 2019/11/25
 *
 * @author yong
 * @see
 */
public class MySession implements Serializable {
    private static MySession mySession = new MySession();
    /**
     * 手机号
     */
    private long phone;
    /**
     * sessionId
     */
    private String sessionId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 加密后密码
     */
    private String encode;

    public MySession() {
    }

    public MySession(String name, String sessionId) {

    }

    public static MySession getInstance() {
        return mySession;
    }

    public static MySession getMySession() {
        return mySession;
    }

    public static void setMySession(MySession mySession) {
        MySession.mySession = mySession;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }
}
