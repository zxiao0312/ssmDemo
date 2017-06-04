package com.zxiao.service.bean;


import com.zxiao.cm.bean.BaseEntity;

import java.io.Serializable;

/**
* Created by zxiao on 2015-7-16.
*/
public class User  extends BaseEntity {

   private Integer id;

   private String username;

   private Integer age;

    @Override
    public <T extends Serializable> T getPK() {
        return null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }

    public Integer getId() {
       return id;
   }

   public void setId(Integer id) {
       this.id = id;
   }

   public String getUsername() {
       return username;
   }

   public void setUsername(String username) {
       this.username = username == null ? null : username.trim();
   }

   public Integer getAge() {
       return age;
   }

   public void setAge(Integer age) {
       this.age = age;
   }


}