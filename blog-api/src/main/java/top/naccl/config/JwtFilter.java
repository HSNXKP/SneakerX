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
 * @Author: wdd
 * @Date: 2020-07-21
 */

public class JwtFilter extends GenericFilterBean  {


    private static final String[] AUTH_WHITELIST = {
            // admin接口是后台管理接口
            "/admin",
            // user接口是前台接口
            "/user"
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
        // 放行其他请求
        filterChain.doFilter(request, servletResponse);



    }
}



//package top.naccl.config;
//
//import io.jsonwebtoken.Claims;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.GenericFilterBean;
//import top.naccl.entity.Url;
//import top.naccl.mapper.UrlMapper;
//import top.naccl.model.vo.Result;
//import top.naccl.util.JacksonUtils;
//import top.naccl.util.JwtUtils;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
//
///**
// * @Description: 前台登录 JWT请求过滤器
// * @Author: wdd
// * @Date: 2020-07-21
// */
//
//public class JwtFilter extends GenericFilterBean {
//
//    @Autowired
//    private UrlMapper urlMapper;
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        String jwt = request.getHeader("Authorization");
//        if (JwtUtils.judgeTokenIsExist(jwt)) {
//            try {
//                Claims claims = JwtUtils.getTokenBody(jwt);
//                String username = claims.getSubject();
//                List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
//                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
//                SecurityContextHolder.getContext().setAuthentication(token);
//            } catch (Exception e) {
//                e.printStackTrace();
//                response.setContentType("application/json;charset=utf-8");
//                Result result = Result.create(403, "凭证已失效，请重新登录！");
//                PrintWriter out = response.getWriter();
//                out.write(JacksonUtils.writeValueAsString(result));
//                out.flush();
//                out.close();
//                return;
//            }
//        }
//
//        // 角色权限验证
//        boolean hasPermission = false;
//        String requestURI = request.getRequestURI();
//        if (SecurityContextHolder.getContext().getAuthentication() != null) {
//            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            if (principal instanceof UserDetails) {
//                UserDetails userDetails = (UserDetails) principal;
//
//                for (GrantedAuthority authority : userDetails.getAuthorities()) {
//                    String role = authority.getAuthority();
//                    List<Url> urls = urlMapper.getUrlsByRole(role);
//                    for (Url url : urls) {
//                        if (requestURI.equals(url.getPath())) {
//                            hasPermission = true;
//                            break;
//                        }
//                    }
//
//                    if (hasPermission) {
//                        break;
//                    }
//                }
//            }
//        }
//
//        if (hasPermission) {
//            filterChain.doFilter(servletRequest, servletResponse);
//        } else {
//            response.setContentType("application/json;charset=utf-8");
//            Result result = Result.create(403, "您没有权限访问该资源！");
//            PrintWriter out = response.getWriter();
//            out.write(JacksonUtils.writeValueAsString(result));
//            out.flush();
//            out.close();
//        }
//    }
//}