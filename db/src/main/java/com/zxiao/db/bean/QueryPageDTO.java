package com.zxiao.db.bean;
import com.zxiao.cm.bean.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiongyu
 * Date: 14-3-3
 * Time: 下午1:29
 * To change this template use File | Settings | File Templates.
 */
public final class QueryPageDTO<Entity extends Serializable> {

    private Entity entity;
    private List<?> entities;
    private PageInfo pageInfo;
    private String whereSql;
    private String orderBy;


    private QueryPageDTO(Entity entity, PageInfo pageInfo, String whereSql, String orderBy) {
        this.entity = entity;
        this.pageInfo = pageInfo;
        this.whereSql = whereSql;
        this.orderBy = orderBy;
    }

    private QueryPageDTO(List<?> entities, PageInfo pageInfo, String whereSql, String orderBy) {
        this.entities = entities;
        this.pageInfo = pageInfo;
        this.whereSql = whereSql;
        this.orderBy = orderBy;
    }

    public static <Entity extends Serializable> QueryPageDTO getQuery(Entity entity) {
        return getQuery(entity, null, null, null);
    }

    public static <Entity extends Serializable> QueryPageDTO getQuery(Entity entity, PageInfo pageInfo) {
        return getQuery(entity, pageInfo, null, null);
    }

    public static <Entity extends Serializable> QueryPageDTO getQuery(Entity entity, PageInfo pageInfo, String whereSql) {
        return getQuery(entity, pageInfo, whereSql, null);
    }

    public static <Entity extends Serializable> QueryPageDTO getQuery(Entity entity, PageInfo pageInfo, String whereSql, String orderBy) {
        return new QueryPageDTO(entity, pageInfo, whereSql, orderBy);
    }

    public static <Entity extends Serializable> QueryPageDTO getQuery(List<?> entities) {
        return new QueryPageDTO(entities, null, null, null);
    }

    public List<?> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getWhereSql() {
        return whereSql;
    }

    public void setWhereSql(String whereSql) {
        this.whereSql = whereSql;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
