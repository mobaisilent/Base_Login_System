package org.mobai.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mobai.mapper.UserMapper;

import java.io.IOException;
import java.util.function.Consumer;

public class SqlUtil {
  private SqlUtil() {
  }

  private static final SqlSessionFactory factory;

  static {
    try {
      factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void doSql(Consumer<UserMapper> consumer) {
    try (SqlSession session = factory.openSession(true)) {
      UserMapper mapper = session.getMapper(UserMapper.class);
      consumer.accept(mapper);
    }
  }
}
