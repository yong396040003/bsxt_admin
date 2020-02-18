package com.yong.bsxt_admin.jutil;

import com.alibaba.fastjson.JSONObject;

/**
 * Description:自定义返回参数json
 * Date: 16:23 2019/12/24
 *
 * @author yong
 * @see
 */
public class AjaxReturnJson {

    public static AjaxReturnJson ajaxReturnJson;

    static {
        ajaxReturnJson = new AjaxReturnJson();
    }

    private AjaxReturnJson() {
    }

    /**
     * 返回普通消息
     *
     * @return
     */
    public JSONObject getMsgReturnJson(int code, String msg, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        return jsonObject;
    }


    /**
     * 获取返回结果table
     *
     * @return
     */
    public JSONObject getTableReturnJson(int code, long count, int total, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("count", count);
        jsonObject.put("total", total);
        jsonObject.put("data", data);
        return jsonObject;
    }

}
