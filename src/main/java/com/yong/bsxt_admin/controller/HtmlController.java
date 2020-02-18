package com.yong.bsxt_admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description:返回指定页面类
 * Date: 12:16 2019/11/10
 *
 * @author yong
 * @see
 */
@Controller
public class HtmlController {
    @RequestMapping("/{html}.ac")
    public String index(@PathVariable String html, Model model) {
        //登陆成功
        if ("index".equals(html)) {
            model.addAttribute("name", LoginController.mySession.getName());
        }
        return html;
    }

    @RequestMapping("/{path}/{html}.ac")
    public String index(@PathVariable String path, @PathVariable String html) {
        return path + "/" + html;
    }
}
