package www.huasuoworld.com.dao.impl;

import www.huasuoworld.com.dao.MyDao;

public class MyDaoImpl implements MyDao {

  @Override
  public String hello() {
    return "hello world! this is MyDaoImpl!";
  }
}
