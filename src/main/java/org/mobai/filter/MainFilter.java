package org.mobai.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpFilter;
import org.mobai.entity.User;

import java.io.IOException;

// 像这种过滤器/* 太广泛了，只是测试用 把这个就先注释了。
//@WebFilter("/*")
public class MainFilter extends HttpFilter {
  @Override
  protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
    String url = req.getRequestURL().toString();
    System.out.println("Flag1 is MainFilter");
    //判断是否为静态资源
    if(!url.endsWith(".js") && !url.endsWith(".css") && !url.endsWith(".png")){
      HttpSession session = req.getSession();
      User user = (User) session.getAttribute("user");
      //判断是否未登陆
      if(user == null && !url.endsWith("login")){
        System.out.println("Flag2 is MainFilter");
        res.sendRedirect("login");
        return;
      }
    }
    //交给过滤链处理
    //    chain.doFilter(req, res);
    //    加上这句会导致无限重定向
  }
}