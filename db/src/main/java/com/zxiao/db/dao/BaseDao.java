package com.zxiao.db.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zxiao.cm.bean.BaseEntity;
import com.zxiao.cm.bean.PageInfo;
import com.zxiao.cm.bean.PagerControl;
import com.zxiao.cm.exception.ServiceException;
import com.zxiao.db.DBCacheKey;
import com.zxiao.db.bean.QueryPageDTO;
import com.zxiao.db.dao.IDao;
import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sean on 13-5-22.
 */
@SuppressWarnings("unchecked")
public abstract class BaseDao<T extends Serializable, M extends BaseEntity>
        extends SqlSessionDaoSupport implements IDao<T, M> {

    @Autowired
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected static final String insertSelective = ".insertSelective";
    protected static final String updateSelective = ".updateByPrimaryKeySelective";
    protected static final String selectByPrimaryKey = ".selectByPrimaryKey";

    protected static final String selectByPrimaryKeys = ".selectByPrimaryKeys";

    protected static final String getListByEntityAndPageInfo = ".getListByEntityAndPageInfo";
    protected static final String getTotalByEntity = ".getTotalByEntity";
    protected static final String deleteByPrimaryKey = ".deleteByPrimaryKey";
    //  protected static final String deleteByEntity = ".deleteByEntity";

    private Cache<T, M> cache = null;


    //用作排序验证对比
    protected Set<String> entityFields = new HashSet<String>();

    protected Class<M> curClassType = null;

    @Override
    public DBCacheKey getCacheKey() {
        return null;
    }

    //默认为缓存数据保存在内存当中
    @Override
    public int cacheSaveType() {
        return MEMORY;
    }

    private void setupField( Field[]  fields){
        for (Field field :fields) {
            if ("serialVersionUID".equals(field.getName())) continue;
            StringBuilder sb = new StringBuilder();
            for (char c : field.getName().toCharArray()) {
                if (Character.isUpperCase(c)) {
                    sb.append("_").append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }
            entityFields.add(sb.toString());
        }
    }


    public BaseDao() {
        if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
            curClassType = (Class<M>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[1];
            if (!"BaseEntity".equals(curClassType.getSuperclass().getSimpleName())) {
                setupField(curClassType.getSuperclass().getDeclaredFields());
            }
            setupField( curClassType.getDeclaredFields());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("#########   init Dao:" + getClass().getName() + "    " + " interface:" + getClass().getInterfaces()[0].getName());
        }

        //init cache
        if (getCacheKey() != null) {
            this.cache = CacheBuilder
                    .newBuilder()
                    .maximumSize(getCacheKey().getMaximumSize())
                    .expireAfterWrite(getCacheKey().getExpLongTime(), TimeUnit.SECONDS)
                    .build();
        }
    }


    @Override
    public boolean deleteCacheByPk(T pk) {
        boolean optSuc = false;
        if (getCacheKey() != null) {
            if (this.cache.asMap().containsKey(pk)) {
                this.cache.asMap().remove(pk);
            }
        }
        return optSuc;
    }


    public String getOrderBySql(PageInfo pageInfo) {
        if (pageInfo == null) return null;
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(pageInfo.getOrderField()) && entityFields.contains(pageInfo.getOrderField())) {
            sb.append("order by ")
                    .append(pageInfo.getOrderField())
                    .append(" ")
                    .append("desc".equals(pageInfo.getOrderDirection()) ? "desc" : "asc");
        }
        return sb.toString();
    }

    /**
     * 对应 Mapper NameSpace名称 做匹配
     *
     * @return
     */
    public String getMapperNameSpace() {
        return getClass().getInterfaces()[0].getName();
    }

    @Override
    public int insert(M entity) {
        try {
            return getSqlSession().insert(getMapperNameSpace() + insertSelective, entity);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".insert: {}", entity == null ? "" : entity.toString(), e);
        }
    }

    @Override
    public int update(M entity) {
        int resultInt = getSqlSession().update(getMapperNameSpace() + updateSelective, entity);
        if (resultInt > 0 && getCacheKey() != null) {
            if (this.cache.asMap().containsKey(entity.getPK())) {
                M queryObj = getSqlSession().selectOne(getMapperNameSpace() + selectByPrimaryKey, entity.getPK());
                if (queryObj != null) {
                    this.cache.put((T) entity.getPK(), queryObj);
                }
            }
        }

        try {
            return resultInt;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".update: {}", entity == null ? "" : entity.toString(), e);
        }
    }

    @Override
    public int delete(T pk) {
        deleteCacheByPk(pk);
        try {
            return getSqlSession().delete(getMapperNameSpace() + deleteByPrimaryKey, pk);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".delete: {}", pk.toString(), e);
        }
    }

//    @Override
//    public int deleteByEntity(M entity) {
//        try {
//            return getSqlSession().delete(getMapperNameSpace() + deleteByEntity, entity);
//        } catch (Exception e) {
//            throw new ServiceException("service-" + getMapperNameSpace() + ".deleteByEntity: {}", entity.toString(), e);
//        }
//    }

    @Override
    public Map<T, M> getEntityByIds(List<T> pks, String idField) {
        if (pks == null || pks.size() == 0) {
            return null;
        }
        try {
            return getSqlSession().selectMap(getMapperNameSpace() + selectByPrimaryKeys, pks, idField);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getEntityByIds: {}", pks.get(0).toString(), e);
        }
    }

    @Override
    public List<M> getEntityByIds(List<T> pks) {
        if (pks == null || pks.size() == 0) {
            return null;
        }
        try {
            return getSqlSession().selectList(getMapperNameSpace() + selectByPrimaryKeys, pks);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getEntityByIds: {}", pks.get(0).toString(), e);
        }
    }

    public String isExistDBField(String valiField) {
        if (entityFields.contains(valiField)) {
            return valiField;
        } else {
            throw new ServiceException("service-" + getMapperNameSpace() + ".validateDBField: {" + valiField + "}");
        }
    }

    @Override
    public M getEntityById(T pk) {
        M returnObj = null;

        if (getCacheKey() != null) {
            returnObj = this.cache.asMap().get(pk);
            if (returnObj != null)
                return returnObj;
        }

        returnObj = getSqlSession().selectOne(getMapperNameSpace() + selectByPrimaryKey, pk);

        if (getCacheKey() != null && returnObj != null) {
            this.cache.put(pk, returnObj);
        }

        return returnObj;
    }

    @Override
    public M getEntityByIdUseDB(T pk) {
        return getSqlSession().selectOne(getMapperNameSpace() + selectByPrimaryKey, pk);
    }

    @Override
    public M getEntityByObj(M entity) {
        try {
            return getEntityByObj(entity, null);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getEntityByObj: {}", entity == null ? "" : entity.toString(), e);
        }
    }


    @Override
    public M getEntityByObj(M entity, String whereSql) {
        try {
            return getSqlSession().selectOne(getMapperNameSpace() + getListByEntityAndPageInfo, QueryPageDTO.getQuery(entity, null, whereSql));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getEntityByObj: {},{}", new String[]{entity == null ? "" : entity.toString(), whereSql}, e);
        }
    }

    @Override
    public int getCountByObj(M entity) {
        try {
            return getCountByObj(entity, null);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {}", entity == null ? "" : entity.toString(), e);
        }
    }

    @Override
    public int getCountByObj(M entity, String whereSql) {
        try {

            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + getTotalByEntity, QueryPageDTO.getQuery(entity, null, whereSql, null));
            return (Integer) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {},{}", new String[]{entity == null ? "" : entity.toString(), whereSql}, e);
        }
    }

    @Override
    public PagerControl<M> getPagerByObj(M entity, PageInfo pageInfo, String whereSql, String orderBySql) {
        PagerControl<M> pagerControl = new PagerControl<M>();
        pageInfo.startTime();
        List<M> list = new ArrayList<M>();
        int total = 0;
        try {
            total = getCountByObj(entity, whereSql);
            if (total > 0) {
                list = getListByObj(entity, pageInfo, whereSql, orderBySql);
            }
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getPagerByObj-" + e.getMessage() + ": {},{},{},{}", new String[]{entity == null ? "" : entity.toString(),
                    pageInfo.toString(), whereSql, orderBySql}, e);
        }

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
    }


    @Override
    public PagerControl<M> getPagerByObj(M entity, PageInfo pageInfo, String whereSql) {
        return getPagerByObj(entity, pageInfo, whereSql, null);
    }

    @Override
    public List<M> getAllEntityObj() {
        return getListByObj(null, null);
    }

    @Override
    public List<M> getListByObj(M entity) {
        return getListByObj(entity, null, null, null);
    }

    @Override
    public List<M> getListByObj(M entity, String whereSql) {
        return getListByObj(entity, null, whereSql, null);
    }

    @Override
    public List<M> getListByObj(M entity, String whereSql, String orderBySql) {
        return getListByObj(entity, null, whereSql, orderBySql);
    }

    @Override
    public List<M> getListByObj(M entity, PageInfo pageInfo, String whereSql) {
        return getListByObj(entity, pageInfo, whereSql, null);
    }


    @Override
    public List<M> getListByObjSortByMultiField(M entity, PageInfo pageInfo, String whereSql, String orderBySql) {
        try {
            return getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo, QueryPageDTO.getQuery(entity, pageInfo, whereSql, orderBySql));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByObj: {},{},{},{}", new String[]{entity == null ? "" : entity.toString(),
                    pageInfo.toString(), whereSql, orderBySql}, e);
        }
    }

    @Override
    public List<M> getListByObj(M entity, PageInfo pageInfo, String whereSql, String orderBySql) {
        try {
            return getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo,
                    QueryPageDTO.getQuery(entity, pageInfo, whereSql,
                            pageInfo != null && !StringUtils.isEmpty(pageInfo.getOrderField())
                                    ? getOrderBySql(pageInfo) : orderBySql
                    )
            );
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByObj: {},{},{},{}",
                    new String[]{entity == null ? "" : entity.toString(),
                            (pageInfo != null) ? pageInfo.toString() : "", whereSql, orderBySql}, e
            );
        }
    }
}
