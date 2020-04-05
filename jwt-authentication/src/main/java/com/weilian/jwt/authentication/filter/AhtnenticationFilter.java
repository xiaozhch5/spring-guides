package com.weilian.jwt.authentication.filter;

import com.alibaba.fastjson.JSON;
import com.weilian.jwt.authentication.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest rq = (HttpServletRequest) servletRequest;
        HttpServletResponse rp = (HttpServletResponse) servletResponse;
        RpBase rpBase = new RpBase();
        try {
            //只接受post
            if (!rq.getMethod().equalsIgnoreCase("post")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            String token = rq.getHeader("token");
            if (StringUtils.isEmpty(token)) {
                rpBase.setMsg("无token");
                return;
            }

            //jwt验证
            MoUser moUser = JwtUtil.getValByT(token, WebConfig.Token_EncryKey, WebConfig.Login_User, MoUser.class);
            if (moUser == null) {
                rpBase.setMsg("token已失效");
                return;
            }

            System.out.println("token用户：" + moUser.getNickName());

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception ex) {
        } finally {
            if (!StringUtils.isEmpty(rpBase.getMsg())) {
                rp.setCharacterEncoding("utf-8");
                rpBase.setCode(HttpStatus.BAD_REQUEST.value());
                rp.getWriter().write(JSON.toJSONString(rpBase));
            }
        }
    }
}