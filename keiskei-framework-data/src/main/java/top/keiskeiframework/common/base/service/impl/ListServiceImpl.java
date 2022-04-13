package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.BaseSortVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.mapper.BaseEntityMapper;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.base.util.BaseRequestUtils;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.log.OperateNotifyType;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 树形实体类基础服务实现
 * </p>
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class ListServiceImpl<T extends ListEntity<ID>, ID extends Serializable> extends ServiceImpl<BaseEntityMapper<T, ID>
        , T> implements BaseService<T, ID>, IService<T> {

    protected final static String CACHE_NAME = "CACHE:LIST";
    @Autowired
    private ListServiceImpl<T, ID> listService;

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
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #id", unless = "#result == null")
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
    @OperateNotify(type = OperateNotifyType.SAVE)
    @Lockable(key = "#t.hashCode()")
    public T saveAndNotify(T t) {
        super.save(t);
        return t;
    }

    @Override
    @OperateNotify(type = OperateNotifyType.UPDATE)
    @CachePut(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #t.id")
    public T updateAndNotify(T t) {
        super.updateById(t);
        return t;
    }

    @Override
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
                listService.saveAndNotify(t1);
                listService.saveAndNotify(t2);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #id")
    @OperateNotify(type = OperateNotifyType.DELETE)
    public void deleteByIdAndNotify(ID id) {
        super.removeById(id);
    }

    @Override
    public void validate(T t) {

    }

    @Override
    public Map<String, Double> getChartOptions(ChartRequestDTO chartRequestDTO) {
        return null;
    }


}
