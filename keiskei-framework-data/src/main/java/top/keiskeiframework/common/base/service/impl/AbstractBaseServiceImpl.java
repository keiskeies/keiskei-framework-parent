package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.BaseSortVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.injector.dto.ManyToManyResult;
import top.keiskeiframework.common.base.mapper.BaseEntityMapper;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.base.util.BaseRequestUtils;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.log.OperateNotifyType;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.util.SpringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public abstract class AbstractBaseServiceImpl<T extends ListEntity> extends ServiceImpl<BaseEntityMapper<T>
        , T> implements BaseService<T>, IService<T> {

    @Autowired
    private BaseService<T> baseService;

    protected void getManyToMany(T t) {
        boolean hasManyToMany = false;

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (null == oneToMany) {
                continue;
            }
            JoinTable joinTable = field.getAnnotation(JoinTable.class);
            if (null != joinTable) {
                hasManyToMany = true;
                break;
            }
        }
        if (!hasManyToMany) {
            return;
        }

        List<ManyToManyResult> manyTomMany = this.baseMapper.findManyToMany(t);
        if (!CollectionUtils.isEmpty(manyTomMany)) {
            Map<String, List<ManyToManyResult>> manyTomManyMap =
                    manyTomMany.stream().collect(Collectors.groupingBy(ManyToManyResult::getEntity));
            for (Map.Entry<String, List<ManyToManyResult>> entry : manyTomManyMap.entrySet()) {
                BaseService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entry.getKey()) + "ServiceImpl", BaseService.class);
                List<ListEntity> joinResult = new ArrayList<>(entry.getValue().size());

                String fieldName = null;
                for (ManyToManyResult manyToManyResult : entry.getValue()) {
                    ListEntity e = baseService.findById(manyToManyResult.getSecondId());
                    if (null == e) {
                        continue;
                    }
                    joinResult.add(e);
                    fieldName = manyToManyResult.getFieldName();
                }

                try {
                    assert fieldName != null;
                    Field field = t.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(t, joinResult);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    protected void getOneToMany(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (null == oneToMany) {
                continue;
            }
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null == joinColumn) {
                continue;
            }
            Type type = field.getGenericType();
            ParameterizedType pt = (ParameterizedType) type;
            Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

            Long id = t.getId();

            String entity = clz.getSimpleName();
            String firstId = BeanUtils.humpToUnderline(joinColumn.name());
            BaseService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", BaseService.class);
            List<?> joinResult = baseService.findAllByColumn(firstId, id);

            if (!CollectionUtils.isEmpty(joinResult)) {
                field.setAccessible(true);
                try {
                    field.set(t, joinResult);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void getOneToOne(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            if (null == oneToOne) {
                continue;
            }
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null == joinColumn) {
                continue;
            }
            Type type = field.getGenericType();
            ParameterizedType pt = (ParameterizedType) type;
            Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

            Long id = t.getId();

            String entity = clz.getSimpleName();
            String firstId = BeanUtils.humpToUnderline(joinColumn.name());
            BaseService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", BaseService.class);
            ListEntity joinResult = baseService.findByColumn(firstId, id);

            if (null != joinResult) {
                field.setAccessible(true);
                try {
                    field.set(t, joinResult);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void getManyToOne(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ManyToOne manyToMany = field.getAnnotation(ManyToOne.class);
            if (null == manyToMany) {
                continue;
            }
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null == joinColumn) {
                continue;
            }

            Class<?> clz = field.getType();
            long value;
            try {
                Field joinField = t.getClass().getDeclaredField(joinColumn.name());
                joinField.setAccessible(true);
                value = joinField.getLong(t);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
                continue;
            }

            String entity = clz.getSimpleName();
            BaseService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", BaseService.class);
            ListEntity joinResult = baseService.findById(value);

            if (null != joinResult) {
                field.setAccessible(true);
                try {
                    field.set(t, joinResult);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public IPage<T> page(BaseRequestVO<T> request, BasePageVO<T> page) {
        return this.page(new Page<>(page.getPage(), page.getSize()), BaseRequestUtils.getQueryWrapper(request, getEntityClass()));

    }

    @Override
    public List<T> findAll(BaseRequestVO<T> request) {
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
    public Long count(BaseRequestVO<T> request) {
        return this.count(BaseRequestUtils.getQueryWrapper(request, getEntityClass()));
    }

    @Override
    public T findById(Long id) {
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
    public T updateAndNotify(T t) {
        super.updateById(t);
        return t;
    }

    @Override
    public void changeSort(BaseSortVO baseSortVO) {
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
                baseService.updateById(t1);
                baseService.updateById(t2);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByIdAndNotify(Long id) {
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
