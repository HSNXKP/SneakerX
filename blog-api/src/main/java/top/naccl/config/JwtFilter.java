package top.naccl.config;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import top.naccl.constant.JwtConstants;
import top.naccl.entity.Url;
import top.naccl.mapper.UrlMapper;
import top.naccl.model.vo.Result;
import top.naccl.service.UrlService;
import top.naccl.service.impl.UrlServiceImpl;
import top.naccl.util.JacksonUtils;
import top.naccl.util.JwtUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 前台登录 JWT请求过滤器
 * @Author: Naccl
 * @Date: 2020-07-21
 */

public class JwtFilter extends GenericFilterBean  {


    private static final String[] AUTH_WHITELIST = {
            // admin接口是后台管理接口 放行到下一个过滤器
            "/admin",
            // 动态接口
            "/bolgTitleById",
            // 日志接口
            "/archives",
    };


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        for (String s : AUTH_WHITELIST) {
            if (request.getRequestURI().startsWith(request.getContextPath() + s )){
                String jwt = request.getHeader("Authorization");
                if (JwtUtils.judgeTokenIsExist(jwt)) {
                    try {
                        Claims claims = JwtUtils.getTokenBody(jwt);
                        String username = claims.getSubject();
                        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(token);
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.setContentType("application/json;charset=utf-8");
                        Result result = Result.create(403, "凭证已失效，请重新登录！");
                        PrintWriter out = response.getWriter();
                        out.write(JacksonUtils.writeValueAsString(result));
                        out.flush();
                        out.close();
                        return;
                    }
                }
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        filterChain.doFilter(request, servletResponse);

//        if (!request.getRequestURI().startsWith(request.getContextPath() + "/admin") && !request.getRequestURI().startsWith(request.getContextPath() + "/bolgTitleById") && !request.getRequestURI().startsWith(request.getContextPath() + "/archives")){
//            filterChain.doFilter(request, servletResponse);
//            return;
//        }


    }
}