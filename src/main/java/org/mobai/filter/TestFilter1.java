package org.mobai.filter;

import jakarta.servlet.*;

import java.io.IOException;

//@WebFilter("/*")   //路径的匹配规则和Servlet一致，这里表示匹配所有请求
public class TestFilter1 implements Filter {
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    System.out.println("我是1号过滤器");
    filterChain.doFilter(servletRequest, servletResponse);
    System.out.println("我是1号过滤器，处理后");
  }
// 这是第一个过滤类TestFilter1
}
