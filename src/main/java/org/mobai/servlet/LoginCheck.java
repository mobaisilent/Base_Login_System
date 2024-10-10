package org.mobai.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.mobai.entity.User;
import org.mobai.util.SqlUtil;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.Map;

@WebServlet("/loginCheck")
public class LoginCheck extends HttpServlet {
  @SneakyThrows
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
//    doPost显然就是处理的Post请求，处理逻辑是只要是非空username和password就登陆成功
    resp.setContentType("text/html;charset=UTF-8");
    //获取POST请求携带的表单数据
    Map<String, String[]> map = req.getParameterMap();
    //先获取Request里面的ParemeterMap然后判断表单是否完整
    if (map.containsKey("username") && map.containsKey("password")) {
      String username = req.getParameter("username");
      String password = req.getParameter("password");
      SqlUtil.doSql(mapper -> {
        User user = mapper.userCheck(username, password);
        System.out.println(user );
        if (user != null) {
          req.getSession().setAttribute("user", user);
          try {
            resp.getWriter().write("登陆成功！");
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      });
      //权限校验（待完善）（之后会学到）
    } else {
        try{
          resp.getWriter().write("登陆失败！");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
    }
  }
}
