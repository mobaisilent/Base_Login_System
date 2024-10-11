package org.mobai.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lombok.SneakyThrows;
import org.mobai.entity.User;
import org.mobai.util.SqlUtil;

import java.io.IOException;
import java.util.Map;

@WebServlet("/homepage")
public class HomePage extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // 抵达get请求说明Cookie检验通过，那么相当于近期刚刚登陆，就直接重定向到了该界面，直接从Cookie中获取值即可
    System.out.println("这里是/homepage GET请求");
    resp.setContentType("text/html;charset=UTF-8");
    resp.getWriter().write("<h1>主页</h1><br>");
    Cookie[] cookies = req.getCookies();
    String username = null;
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("username")) username = cookie.getValue();
    }
    if (username == null) {
      resp.getWriter().write("<script>"
              + "if (confirm('Cookie/Session已失效，请重新登陆')) {"
              + "  window.location.href = 'login';"
              + "} else {"
              + "  window.location.href = 'login';"
              + "}"
              + "</script>");
    } else {
      resp.getWriter().write("登陆成功！" + username);
    }
  }

  @SneakyThrows
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    System.out.println("here is homepage doPost");
//    doPost显然就是处理的Post请求，处理逻辑是只要是非空username和password就登陆成功
    resp.setContentType("text/html;charset=UTF-8");
    resp.getWriter().write("<h1>主页</h1><br>");
    //获取POST请求携带的表单数据
    Map<String, String[]> map = req.getParameterMap();
    String username = req.getParameter("username");
    String password = req.getParameter("password");
    if (map.containsKey("remember-me")) {   //若勾选了勾选框，那么会此表单信息 设置cookie
      Cookie cookie_username = new Cookie("username", username);
      cookie_username.setMaxAge(30);
      Cookie cookie_password = new Cookie("password", password);
      cookie_password.setMaxAge(30);
      resp.addCookie(cookie_username);
      resp.addCookie(cookie_password);
      HttpSession session = req.getSession();
      session.setAttribute("user", username);
    }
    // map.containsKey("username") && map.containsKey("password") && map.get("username").length > 0 && map.get("password").length > 0
    //先获取Request里面的ParemeterMap然后判断表单是否完整
    if (!username.isEmpty() && !password.isEmpty()) {
      System.out.println(username + " " + password);
      SqlUtil.doSql(mapper -> {
        User user = mapper.userCheck(username, password);
        System.out.println(user);
        if (user != null) {
          req.getSession().setAttribute("user", user);
          try {
            resp.getWriter().write("登陆成功！");
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        } else {
          try {
            resp.getWriter().write("登陆失败！");
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      });
      //权限校验（待完善）（之后会学到）
    } else {
      resp.getWriter().write("请输入用户名或者密码！");
    }
  }
}
