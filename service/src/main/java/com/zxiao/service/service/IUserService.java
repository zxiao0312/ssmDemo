package com.zxiao.service.service;


import com.zxiao.cm.bean.PageInfo;
import com.zxiao.cm.bean.PagerControl;
import com.zxiao.service.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IUserService {
    /**
     * 插入
     * @param user
     * @return
     */
    public int addUser(User user) ;
    /**
     * obj查询对象
     * @param obj
     * @return
     */
    public User getUserBy(User obj);
    /**
     * id查询
     * @param pk
     * @return
     */
    public User getUserBy(Integer pk) ;

    /**
     * 更新单条记录
     * @param obj
     * @return
     */
    public int update(User obj) ;

    /**
     * 获取翻页数据
     * @param user
     * @param pageInfo
     * @param where
     * @param orderBy
     * @return
     */
    public PagerControl<User> getPage(User user, PageInfo pageInfo, String where, String orderBy) ;

    List<User> getUserList();

    int getotalUser();
}