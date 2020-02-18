package com.yong.bsxt_admin.filter;

import com.yong.bsxt_admin.controller.LoginController;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Description:对于没有登陆的用户进行拦截操作
 * Date: 9:43 2019/11/20
 *
 * @author yong
 * @see
 */
@Component
@WebFilter(urlPatterns = "/**", filterName = "loginFilter")
public class LoginFilter implements Filter {

    /**
     * 初始化
     *
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) {

    }

    /**
     * 拦截操作
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取访问路径
        String path = request.getServletPath();
        HttpSession session = request.getSession();
        //首先对path进行判断 若为null直接转发到登陆页面
        String m = "/";
        String acString = ".ac";
        String doString = ".do";
        String loginHtml = "/login.ac";
        String loginDo = "/login.do";
        String loginKick = "/loginKick.do";
        String notFound = "/error.ac";
        String sessionId = null;

        if (LoginController.mySession != null) {
            sessionId = LoginController.mySession.getSessionId();
        }

        if (path == null || path.equals(m)) {
            response.sendRedirect(loginHtml);
        }
        //如果是手机就放行
        else if (path.endsWith(".app")) {
            filterChain.doFilter(request, response);
        }
        //首先对所有静态资源放行 若路径最后两位不为do和ac放行
        else if (!path.endsWith(acString) && !path.endsWith(doString)) {
            filterChain.doFilter(request, response);
        }
        //如果session不为null或者只是访问登陆界面放行
        else if (loginDo.equals(path)
                || loginHtml.equals(path)
                || notFound.equals(path)
                || loginKick.equals(path)) {
            filterChain.doFilter(request, response);
        }
        //管理员已登陆可放行
        else if (sessionId != null && sessionId.equals(session.getId())) {
            filterChain.doFilter(request, response);
        } else {
            response.sendRedirect(loginHtml);
        }

    }

    /**
     * 拦截结束
     */
    @Override
    public void destroy() {
    }
}
