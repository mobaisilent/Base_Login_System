import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.mobai.entity.User;
import org.mobai.mapper.UserMapper;
import org.mobai.util.SqlUtil;

import java.io.IOException;

public class TestMain {
  @Test
  public void test() throws IOException {
    SqlUtil.doSql(mapper -> {
      User user = mapper.userCheck("mobai", "mobai");
      System.out.println(user);
    });
  }
}
