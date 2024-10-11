package org.mobai.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.mobai.entity.User;
import org.mobai.util.SqlUtil;

import java.io.IOException;

@WebServlet("/login")
public class Login extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();
    String user= (String) session.getAttribute("user");
    if(user == null) {
      resp.sendRedirect("login");
      return;
    }
    Cookie[] cookies = req.getCookies();
    System.out.println("这里是/login cookies检验");
    String username = null;
    String password = null;
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("username")) username = cookie.getValue();
      if (cookie.getName().equals("password")) password = cookie.getValue();
    }
    if (username != null && password != null) {
      // 这才是Cookie的信息完整的情况
      String finalUsername = username;
      String finalPassword = password;
      System.out.println("username: " + finalUsername + " password: " + finalPassword);
      SqlUtil.doSql(mapper -> {
        if (mapper.userCheck(finalUsername, finalPassword) != null) {
          System.out.println("这里是/login cookies检验 直接免登陆 设置session");
          req.getSession().setAttribute("user", finalUsername);
          try {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<script>"
                    + "if (confirm('您已经登陆过了，点击确认到达主页，点击取消重新登陆')) {"
                    + "  window.location.href = 'homepage';"
                    + "} else {"
                    + "  document.cookie = 'username=; Max-Age=0';"
                    + "  document.cookie = 'password=; Max-Age=0';"
                    + "  window.location.href = 'login';"
                    + "}"
                    + "</script>");
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      });
    } else {
      System.out.println("这里是/login GET请求 代理login.html");
      req.getRequestDispatcher("/login.html").forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    System.out.println("这里是/login POST请求 跳转到GET");
    this.doGet(req, resp);
  }
}