package com.zxiao.service.service.impl;


import com.zxiao.cm.bean.PageInfo;
import com.zxiao.cm.bean.PagerControl;
import com.zxiao.service.bean.User;
import com.zxiao.service.dao.UserDao;
import com.zxiao.service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    /**
     * 插入
     * @param user
     * @return
     */
    public int addUser(User user) {
        if (user == null) {
            return -1;
        }

        try {
            return userDao.insert(user);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * obj查询对象
     * @param obj
     * @return
     */
    public User getUserBy(User obj) {
        if (obj == null) {
            return null;
        }
        try {
            return userDao.getEntityByObj(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * id查询
     * @param pk
     * @return
     */
    public User getUserBy(Integer pk) {
        if (pk == null) {
            return null;
        }

        try {
            return userDao.getEntityById(pk);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 更新单条记录
     * @param obj
     * @return
     */
    public int update(User obj) {
        if (obj == null || obj.getPK() == null) {
            return -1;
        }

        try {
            return userDao.update(obj);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取翻页数据
     * @param user
     * @param pageInfo
     * @param where
     * @param orderBy
     * @return
     */
    public PagerControl<User> getPage(User user, PageInfo pageInfo, String where, String orderBy) {
        try {
            return userDao.getPagerByObj(user, pageInfo, where, orderBy);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<User> getUserList() {
        try {
            return userDao.getAllEntityObj();
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public int getotalUser() {
        try {
            return userDao.getCountByObj(null,null);
        }catch (Exception ex){
            ex.printStackTrace();
            return 0;
        }

    }

}