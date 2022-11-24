package top.keiskeiframework.common.base.jpa.service.impl;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.ITreeEntity;
import top.keiskeiframework.common.base.service.ITreeBaseService;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.TreeEntityUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 基础查询接口
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class JpaTreeServiceImpl<T extends ITreeEntity<ID>, ID extends Serializable> extends AbstractJpaServiceImpl<T, ID> implements ITreeBaseService<T, ID> {


    protected final static String SPILT = "/";
    @Autowired
    private JpaTreeServiceImpl<T, ID> treeService;

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    })
    public boolean removeByIdSingle(ID id) {
        jpaRepository.deleteById(id);
        return true;
    }

    @Override
    @Cacheable(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    public T findOneById(Serializable id) {
        return super.findOneById(id);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    public T saveOne(T t) {
        if (null != t.getParentId()) {
            T parent = treeService.findOneById(t.getParentId());
            if (null == parent) {
                throw new BizException(BizExceptionEnum.ERROR);
            }
            t = jpaRepository.save(t);
            t.setSign(parent.getSign() + SPILT + t.getId());
        } else {
            t = jpaRepository.save(t);
            t.setSign(SPILT + t.getId());
        }

        return super.updateOne(t);
    }

    @Override
    @Caching(
            evict = {@CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")},
            cacheable = {@Cacheable(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")}
    )
    public T updateOne(T t) {
        return treeService.saveOne(t);
    }

    @Override
    @Cacheable(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    public List<T> findList() {
        return super.findList();
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    public boolean updateListByCondition(List<QueryConditionVO> conditions, T t) {
        return super.updateListByCondition(conditions, t);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    public List<T> saveList(List<T> ts) {
        return super.saveList(ts);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public List<T> updateList(List<T> ts) {
        return super.updateList(ts);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    })
    public boolean deleteOneById(ID id) {
        Set<ID> childIds = new TreeEntityUtils<>(treeService.findList()).getChildIds(id);
        for (ID cid : childIds) {
            treeService.removeByIdSingle(cid);
        }
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean deleteListByIds(Collection<ID> ids) {
        return super.deleteListByIds(ids);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean deleteListByColumn(SFunction<T, Serializable> column, Serializable value) {
        return super.deleteListByColumn(column, value);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean deleteListByCondition(List<QueryConditionVO> conditions) {
        return super.deleteListByCondition(conditions);
    }
}
