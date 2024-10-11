package org.mobai.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

//@WebFilter("/*")   //路径的匹配规则和Servlet一致，这里表示匹配所有请求
public class TestFilter2 implements Filter {
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    System.out.println("我是2号过滤器");
    filterChain.doFilter(servletRequest, servletResponse);
    System.out.println("我是2号过滤器，处理后");
  }
// 这里是重新建立了一个类TestFilter2
}
