package com.zxiao.db;

/**
 * Created by Administrator on 2014/9/1.
 */
public interface DBCacheKey {

    Integer getMaximumSize();

    String getKey();

    String getDesc();

    Long getExpLongTime();

}
