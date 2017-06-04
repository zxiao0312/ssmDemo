package com.zxiao.service.dao.impl;

import com.zxiao.db.dao.BaseDao;
import com.zxiao.service.bean.User;
import com.zxiao.service.dao.UserDao;
import org.springframework.stereotype.Repository;


/**
 * Created by ZhangJian on 2015-7-16.
 */
@Repository
public class UserDaoImpl extends BaseDao<Integer, User> implements UserDao {
}
