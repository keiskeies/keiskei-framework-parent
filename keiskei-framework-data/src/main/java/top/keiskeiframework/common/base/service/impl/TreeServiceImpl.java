package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.util.Assert;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.BaseSortVO;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.mapper.BaseEntityMapper;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.base.util.BaseRequestUtils;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.enums.log.OperateNotifyType;
import top.keiskeiframework.common.util.TreeEntityUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 基础查询接口
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class TreeServiceImpl<T extends TreeEntity<ID>, ID extends Serializable>
        extends ServiceImpl<BaseEntityMapper<T, ID>, T>
        implements BaseService<T, ID>, IService<T> {


    protected final static String SPILT = "/";
    protected final static String CACHE_NAME = "CACHE:TREE";

    @Autowired
    private TreeServiceImpl<T, ID> treeService;


    @Override
    public IPage<T> page(BaseRequestVO<T, ID> request, BasePageVO<T, ID> page) {
        return this.page(page.getPageable(), BaseRequestUtils.getQueryWrapper(request, getEntityClass()));
    }

    @Override
    public List<T> findAll(BaseRequestVO<T, ID> request) {
        return this.list(BaseRequestUtils.getQueryWrapper(request, getEntityClass()));
    }

    @Override
    public List<T> findAll() {
        return this.list(BaseRequestUtils.getQueryWrapper(null, null));
    }

    @Override
    public List<T> findAll(T t) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>(t);
        return this.list(queryWrapper);
    }

    @Override
    public List<T> findAllByColumn(String column, Serializable value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(column, value);
        return this.list(queryWrapper);
    }

    @Override
    public Long count(BaseRequestVO<T, ID> request) {
        return this.count(BaseRequestUtils.getQueryWrapper(request, getEntityClass()));
    }

    @Override
    public T findById(ID id) {
        return super.getById(id);
    }

    @Override
    public T findByColumn(String column, Serializable value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(column, value);
        return this.getOne(queryWrapper);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    @OperateNotify(type = OperateNotifyType.SAVE)
    @Lockable(key = "#t.hashCode()")
    public T saveAndNotify(T t) {
        if (null != t.getParentId()) {
            T parent = treeService.findById(t.getParentId());
            Assert.notNull(parent, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
            super.save(t);
            t.setSign(parent.getSign() + t.getId() + SPILT);

        } else {
            super.save(t);
            t.setSign(t.getId() + SPILT);
        }
        super.save(t);
        return t;
    }


    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    @OperateNotify(type = OperateNotifyType.UPDATE)
    public T updateAndNotify(T t) {
        Assert.notNull(t.getId(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
        if (null != t.getParentId()) {
            T parent = treeService.findById(t.getParentId());
            Assert.notNull(parent, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
            t.setSign(parent.getSign() + t.getId() + SPILT);
        } else {
            t.setSign(t.getId() + SPILT);
        }

        super.save(t);
        return t;
    }


    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    public void changeSort(BaseSortVO<ID> baseSortVO) {
        try {
            Class<T> clazz = getEntityClass();
            Field[] fields = clazz.getDeclaredFields();
            T t1 = clazz.newInstance();
            T t2 = clazz.newInstance();
            boolean hasSort = false;
            for (Field field : fields) {
                if ("id".equals(field.getName())) {
                    field.setAccessible(true);
                    field.set(t1, baseSortVO.getId1());
                    field.set(t2, baseSortVO.getId2());
                } else {
                    OrderBy sortBy = field.getAnnotation(OrderBy.class);
                    if (null != sortBy) {
                        hasSort = true;
                        field.setAccessible(true);
                        field.set(t1, baseSortVO.getSortBy2());
                        field.set(t2, baseSortVO.getSortBy1());
                    }
                }
            }
            if (hasSort) {
                treeService.saveAndNotify(t1);
                treeService.saveAndNotify(t2);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    @OperateNotify(type = OperateNotifyType.DELETE)
    public void deleteByIdAndNotify(ID id) {
        Set<ID> childIds = new TreeEntityUtils<>(treeService.findAll()).getChildIds(id);
        for (ID cid : childIds) {
            super.removeById(cid);
        }
    }

    @Override
    public void validate(T t) {

    }

    @Override
    public Map<String, Double> getChartOptions(ChartRequestDTO chartRequestDTO) {
        return null;
    }


}
