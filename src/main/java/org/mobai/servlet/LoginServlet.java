package org.mobai.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet implements Servlet {


  @SneakyThrows
  @Override
  public void init(ServletConfig servletConfig) {
    System.out.println("here");
  }

  @Override
  public ServletConfig getServletConfig() {
    return null;
  }

  @Override
  public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
    System.out.println("here");
  }

  @Override
  public String getServletInfo() {
    return "";
  }

  @Override
  public void destroy() {

  }
}
