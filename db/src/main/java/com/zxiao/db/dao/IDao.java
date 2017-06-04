package com.zxiao.db.dao;
;

import com.zxiao.cm.bean.BaseEntity;
import com.zxiao.cm.bean.PageInfo;
import com.zxiao.cm.bean.PagerControl;
import com.zxiao.db.DBCacheKey;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * User: Sean
 * Date: 2013-5-23 下午1:02
 */
public interface IDao<T extends Serializable, M extends BaseEntity> {
    //把缓存存在内存当中
    public static final int MEMORY = 0;
    //把缓存持久化
    public static final int HARDDISK = 1;

    /**
     *   MEMORY orHARDDISK
     * @return
     */
    public int cacheSaveType();

    /**
     * 判断数据库库字段是否存在
     * @param valiField
     * @return
     */
    public String isExistDBField(String valiField);

    /**
     * 插入操作
     *
     * @param m
     * @return
     */
    int insert(M m);

    /**
     * 根据主键删除
     *
     * @param pk
     * @return
     */
    int delete(T pk);

//    /**
//     * 根据主键删除
//     *
//     * @param entity
//     * @return
//     */
//    int deleteByEntity(M entity);

    /**
     * 更新实体操作
     *
     * @param entity 传入须要更新的实体对
     * @return
     */
    int update(M entity);

    /**
     * 通过传入 pk 获取对象
     *
     * @param pk
     * @return
     */
    M getEntityById(T pk);

    /**
     * 根据主键ID的集合， 数据库IN批量查询数据结果集，返回List
     * @param pks
     * @return
     */
    List<M> getEntityByIds(List<T> pks);

    /**
     * 根据主键ID的集合， 数据库IN批量查询数据结果集，返回以主键值为Key的Map
     * @param pks
     * @param idField
     * @return
     */
    Map<T, M> getEntityByIds(List<T> pks, String idField);
    /**
     * 通过传入 实体参数 获取对象
     *
     * @param entity
     * @return
     */
    M getEntityByObj(M entity);

    M getEntityByIdUseDB(T pk);
    /**
     * 通过传入 实体参数 获取对象
     *
     * @param entity
     * @param whereSql 自定义WhereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     * @return
     */
    M getEntityByObj(M entity, String whereSql);

    /**
     * 通过传入实体参数获取条数
     *
     * @param entity
     * @return
     */
    int getCountByObj(M entity);

    /**
     * 通过传入实体参数 和自定义 whereSql 获取条数
     *
     * @param entity
     * @param whereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     * @return
     */
    int getCountByObj(M entity, String whereSql);
    /**
     * 通过实体参数获取对应的实体列表包含物理分页
     *
     * @param entity   实体对象
     * @param pageInfo 分页对象
     * @param whereSql 自定义WhereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     *                 让id倒序排列 <br/>
     *                 只需要传入 order by id desc 即可
     * @return 返回分页好的实体集合
     */
    PagerControl<M> getPagerByObj(M entity, PageInfo pageInfo, String whereSql);
    /**
     * 通过实体参数获取对应的实体列表包含物理分页
     *
     * @param entity     实体对象
     * @param pageInfo   分页对象
     * @param whereSql   自定义WhereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     * @param orderBySql 排序sql 例如: select * from user order by id desc <br/>
     *                   让id倒序排列 <br/>
     *                   只需要传入 order by id desc 即可
     * @return 返回分页好的实体集合
     */
    PagerControl<M> getPagerByObj(M entity, PageInfo pageInfo, String whereSql, String orderBySql);
    /**
     * 作用读取所有对象<br/>
     * 注意:使用范围主要作用读取配置，地区，或者是一些状态标，数据量小的可以使用
     *
     * @return 返回对象集合
     */
    List<M> getAllEntityObj();
    /**
     * 作用读取对象列表 带实体条件 <br/>
     *
     * @param entity 参数查询
     * @return 返回对象集合
     */
    List<M> getListByObj(M entity);
    /**
     * 作用读取对象列表 带实体条件 <br/>
     *
     * @param entity   参数查询
     * @param whereSql 自定义WhereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     * @return 返回对象集合
     */
    List<M> getListByObj(M entity, String whereSql);
    /**
     * 作用读取对象列表 带实体条件 <br/>
     *
     * @param entity     参数查询
     * @param whereSql   自定义WhereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     * @param orderBySql 不使用传空字符串或者null<br/>
     *                   排序sql 例如: select * from user order by id desc <br/>
     *                   让id倒序排列 <br/>
     *                   只需要传入 id desc 即可
     * @return 返回对象集合
     */
    List<M> getListByObj(M entity, String whereSql, String orderBySql);
    /**
     * 作用读取对象列表 带实体条件 <br/>
     *
     * @param entity   参数查询
     * @param pageInfo 分页对象 不使用传null
     * @param whereSql 自定义WhereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     * @return 返回对象集合
     */
    List<M> getListByObj(M entity, PageInfo pageInfo, String whereSql);
    /**
     * 作用读取对象列表 带实体条件 <br/>
     *
     * @param entity     参数查询
     * @param pageInfo   分页对象 不使用传null
     * @param whereSql   自定义WhereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     * @param orderBySql 不使用传空字符串或者null<br/>
     *                   排序sql 例如: select * from user order by id desc <br/>
     *                   让id倒序排列 <br/>
     *                   只需要传入 id desc 即可
     * @return 返回对象集合
     */
    List<M> getListByObj(M entity, PageInfo pageInfo, String whereSql, String orderBySql);
    /**
     *
     * @param entity     查询参数
     * @param pageInfo   分页对象
     *@param whereSql    条件语句
     * @param orderBySql    多个字段排序
     * @return
     */
    List<M> getListByObjSortByMultiField(M entity, PageInfo pageInfo, String whereSql, String orderBySql);

    boolean deleteCacheByPk(T pk);

    DBCacheKey getCacheKey();


}
