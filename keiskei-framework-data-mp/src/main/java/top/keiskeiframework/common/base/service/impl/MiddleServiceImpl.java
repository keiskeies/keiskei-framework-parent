package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import top.keiskeiframework.common.annotation.annotation.Lockable;
import top.keiskeiframework.common.base.entity.IMiddleEntity;
import top.keiskeiframework.common.base.service.IMiddleService;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/12 20:06
 */
public class MiddleServiceImpl
        <T extends IMiddleEntity<ID1, ID2>, ID1 extends Serializable, ID2 extends Serializable, M extends BaseMapper<T>>
        extends AbstractServiceImpl<T, String, M>
        implements IMiddleService<T, ID1, ID2>, IService<T> {
    @Autowired
    private MiddleServiceImpl<T, ID1, ID2, M> middleService;


    @Override
    @Cacheable(cacheNames = CACHE_MIDDLE_NAME, key = "targetClass.name + ':ID1:' + #id1")
    public List<T> getById1(Serializable id1) {
        return middleService.findListByColumn("id1", id1);
    }

    @Override
    @Cacheable(cacheNames = CACHE_MIDDLE_NAME, key = "targetClass.name + ':ID2:' + #id2")
    public List<T> getById2(Serializable id2) {
        return middleService.findListByColumn("id2", id2);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Cacheable(cacheNames = CACHE_MIDDLE_NAME, key = "targetClass.name + ':ID1:' + #ts[0].id1")
    @Lockable(key = "targetClass.name + ':' + #ts[0].id1")
    public List<T> saveOrUpdateById1(List<T> ts) {
        middleService.removeById1(ts.get(0).getId1());
        middleService.saveBatch(ts);
        return ts;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Cacheable(cacheNames = CACHE_MIDDLE_NAME, key = "targetClass.name + ':ID2:' + #ts[0].id2")
    @Lockable(key = "targetClass.name + ':' + #ts[0].id2")
    public List<T> saveOrUpdateById2(List<T> ts) {
        middleService.removeById2(ts.get(0).getId2());
        middleService.saveBatch(ts);
        return ts;
    }


    @Override
    @CacheEvict(cacheNames = CACHE_MIDDLE_NAME, key = "targetClass.name + ':ID1:' + #id1")
    public Boolean removeById1(ID1 id1) {
        return middleService.deleteListByColumn("id1", id1);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_MIDDLE_NAME, key = "targetClass.name + ':ID1:' + #id2")
    public Boolean removeById2(ID2 id2) {
        return middleService.deleteListByColumn("id2", id2);
    }

}