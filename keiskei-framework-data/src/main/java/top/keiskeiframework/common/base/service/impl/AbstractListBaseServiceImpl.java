package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.cache.service.CacheStorageService;
import top.keiskeiframework.common.annotation.data.BatchCacheField;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.injector.dto.ManyToManyResult;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.base.service.ListBaseService;
import top.keiskeiframework.common.base.util.BaseRequestUtils;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.util.SpringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 树形实体类基础服务实现
 * </p>
 *
 * @param <T>  .
 * @param <ID> .
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public abstract class AbstractListBaseServiceImpl<T extends ListEntity<ID>, ID extends Serializable>
        extends AbstractBaseServiceImpl<T, ID>
        implements ListBaseService<T, ID>, IService<T> {

    @Autowired
    private ListBaseService<T, ID> listService;
    @Autowired
    private CacheStorageService cacheStorageService;
    protected final static String CACHE_COLUMN_LIST = "CACHE:COLUMN_LIST";


    @Override
    @Cacheable(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id", unless = "#result == null")
    public T getByIdCache(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean save(T t) {
        listService.saveCache(t);
        return true;
    }


    @Override
    @Lockable(key = "targetClass.name + ':' + #t.hashCode()")
    public T saveCache(T t) {
        super.save(t);
        cleanColumnCache(t);
        saveManyToMany(t);
        saveOneToMany(t);
        saveManyToOne(t);
        saveOneToOne(t);
        return t;
    }


    @Override
    public List<T> listByColumn(String column, Serializable value) {
        Class<T> tClass = getEntityClass();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            BatchCacheField batchCacheField = field.getAnnotation(BatchCacheField.class);
            if (null != batchCacheField) {
                String cacheField = BeanUtils.humpToUnderline(field.getName());
                column = BeanUtils.humpToUnderline(column);
                if (cacheField.equals(column)) {
                    return listService.listByColumnCache(column, value);
                }
            }
        }
        return super.listByColumn(column, value);
    }


    @Override
    @Cacheable(cacheNames = CACHE_COLUMN_LIST, key = "targetClass.name + ':' + #column + ':' + #value", unless = "#result == null")
    public List<T> listByColumnCache(String column, Serializable value) {
        return super.listByColumn(column, value);
    }



    @Override
    public boolean updateById(T t) {
        listService.updateByIdCache(t);
        return true;
    }

    @Override
    @CachePut(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id", unless = "#result == null")
    public T updateByIdCache(T t) {
        T told = listService.getByIdCache(t.getId());
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            BatchCacheField batchCacheField = field.getAnnotation(BatchCacheField.class);
            if (null != batchCacheField) {
                try {
                    field.setAccessible(true);
                    Object oldValue = field.get(told);
                    Object newValue = field.get(t);
                    String cacheField = BeanUtils.humpToUnderline(field.getName());
                    cacheStorageService.del(CACHE_COLUMN_LIST + ":" + this.getClass().getName() + ":" + cacheField + ":" + oldValue);
                    cacheStorageService.del(CACHE_COLUMN_LIST + ":" + this.getClass().getName() + ":" + cacheField + ":" + newValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        BeanUtils.copyPropertiesIgnoreNull(t, told);
        super.updateById(told);
        updateManyToMany(told);
        updateManyToOne(told);
        updateOneToMany(told);
        updateOneToOne(told);


        return told;
    }


    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    public boolean removeById(Serializable id) {
        T t = listService.getByIdCache(id);
        if (null != t) {
            cleanColumnCache(t);
            deleteManyToMany(t);
            deleteOneToOne(t);
            deleteOneToMany(t);
            deleteManyToOne(t);
            return super.removeById(id);
        }
        return true;
    }

    @Override
    public boolean removeByColumn(String column, Serializable value) {
        Class<T> tClass = getEntityClass();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            BatchCacheField batchCacheField = field.getAnnotation(BatchCacheField.class);
            if (null != batchCacheField) {
                String cacheField = BeanUtils.humpToUnderline(field.getName());
                if (cacheField.equals(column)) {
                    listService.removeByColumnCache(column, value);
                    return true;
                }
            }
        }
        List<T> ts = listService.listByColumn(column, value);
        for (T t : ts) {
            listService.removeById(t.getId());
        }
        return true;
    }

    @Override
    public boolean removeByCondition(List<QueryConditionVO> conditions) {
        Class<T> tClass = getEntityClass();
        QueryWrapper<T> queryWrapper = BaseRequestUtils.getQueryWrapperByConditions(conditions, tClass);
        List<T> ts = listService.list(queryWrapper);
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            BatchCacheField batchCacheField = field.getAnnotation(BatchCacheField.class);
            if (null != batchCacheField) {
                field.setAccessible(true);
                String cacheField = BeanUtils.humpToUnderline(field.getName());
                for (T t : ts) {
                    try {
                        Object obj = field.get(t);
                        if (null != obj) {
                            cacheStorageService.del(CACHE_COLUMN_LIST + ":" + this.getClass().getName() + ":" + cacheField + ":" + obj);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        for (T t : ts) {
            super.removeById(t.getId());
            cacheStorageService.del(CACHE_COLUMN_LIST + ":" + this.getClass().getName() + ":" + t.getId());
        }
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_COLUMN_LIST, key = "targetClass.name + ':' + #column + ':' + #value")
    public void removeByColumnCache(String column, Serializable value) {
        List<T> ts = listService.listByColumnCache(column, value);
        for (T t : ts) {
            listService.removeById(t.getId());
        }
    }


    protected void cleanColumnCache(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        T old = null;
        for (Field field : fields) {
            BatchCacheField batchCacheField = field.getAnnotation(BatchCacheField.class);
            if (null != batchCacheField) {
                try {
                    if (null == old) {
                        old = listService.getByIdCache(t.getId());
                    }
                    field.setAccessible(true);
                    Object newValue = field.get(t);
                    String cacheField = BeanUtils.humpToUnderline(field.getName());
                    cacheStorageService.del(CACHE_COLUMN_LIST + ":" + this.getClass().getName() + ":" + cacheField + ":" + newValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void getManyToMany(T t) {
        if (null == t) {
            return;
        }
        boolean hasManyToMany = false;

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ManyToMany oneToMany = field.getAnnotation(ManyToMany.class);
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
                BaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entry.getKey()) +
                        "ServiceImpl", BaseService.class);
                List<ListEntity<?>> joinResult = new ArrayList<>(entry.getValue().size());

                String fieldName = null;
                for (ManyToManyResult manyToManyResult : entry.getValue()) {
                    ListEntity<?> e = baseService.getById((Serializable) manyToManyResult.getJoinId());
                    if (null == e) {
                        continue;
                    }
                    joinResult.add(e);
                    fieldName = manyToManyResult.getFieldName();
                }
                if (null != fieldName) {
                    try {
                        Field field = t.getClass().getDeclaredField(fieldName);
                        field.setAccessible(true);
                        field.set(t, joinResult);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected void saveManyToMany(T t) {
        if (null == t) {
            return;
        }
        boolean hasManyToMany = false;

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ManyToMany oneToMany = field.getAnnotation(ManyToMany.class);
            if (null == oneToMany) {
                continue;
            }
            JoinTable joinTable = field.getAnnotation(JoinTable.class);
            if (null != joinTable) {
                hasManyToMany = true;
                break;
            }

        }
        if (hasManyToMany) {
            this.baseMapper.saveManyToMany(t);
        }
    }

    protected void updateManyToMany(T t) {
        if (null == t) {
            return;
        }
        boolean hasManyToMany = false;

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ManyToMany oneToMany = field.getAnnotation(ManyToMany.class);
            if (null == oneToMany) {
                continue;
            }
            JoinTable joinTable = field.getAnnotation(JoinTable.class);
            if (null != joinTable) {
                hasManyToMany = true;
                break;
            }

        }
        if (hasManyToMany) {
            this.baseMapper.deleteManyToMany(t.getId());
            this.baseMapper.saveManyToMany(t);
        }
    }

    protected void deleteManyToMany(T t) {
        if (null == t) {
            return;
        }
        boolean hasManyToMany = false;

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ManyToMany oneToMany = field.getAnnotation(ManyToMany.class);
            if (null == oneToMany) {
                continue;
            }
            JoinTable joinTable = field.getAnnotation(JoinTable.class);
            if (null != joinTable) {
                hasManyToMany = true;
                break;
            }

        }
        if (hasManyToMany) {
            this.baseMapper.deleteManyToMany(t.getId());
        }
    }

    protected void getOneToMany(T t) {
        if (null == t) {
            return;
        }
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

            ID id = t.getId();

            String entity = clz.getSimpleName();
            String firstId = BeanUtils.humpToUnderline(joinColumn.name());
            BaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl",
                    BaseService.class);
            List<?> joinResult = baseService.listByColumn(firstId, id);

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

    protected void saveOneToMany(T t) {
        if (null == t) {
            return;
        }
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
            try {
                Object joDataObj = field.get(t);
                if (null == joDataObj) {
                    continue;
                }
                Collection joinData = (Collection<?>) joDataObj;
                if (CollectionUtils.isEmpty(joinData)) {
                    continue;
                }

                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

                ID id = t.getId();
                Field joinField = clz.getDeclaredField(joinColumn.name());
                joinField.setAccessible(true);
                for (Object joinDatum : joinData) {
                    joinField.set(joinDatum, id);
                }

                String entity = clz.getSimpleName();
                BaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) +
                        "ServiceImpl", BaseService.class);
                baseService.saveBatch(joinData);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    protected void updateOneToMany(T t) {
        if (null == t) {
            return;
        }
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
            try {
                Object joDataObj = field.get(t);
                if (null == joDataObj) {
                    continue;
                }
                Collection joinData = (Collection<?>) joDataObj;
                if (CollectionUtils.isEmpty(joinData)) {
                    continue;
                }

                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

                ID id = t.getId();
                Field joinField = clz.getDeclaredField(joinColumn.name());
                joinField.setAccessible(true);
                for (Object joinDatum : joinData) {
                    joinField.set(joinDatum, id);
                }

                String entity = clz.getSimpleName();
                BaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) +
                        "ServiceImpl", BaseService.class);

                baseService.removeByColumn(joinColumn.name(), id);
                baseService.saveBatch(joinData);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    protected void deleteOneToMany(T t) {
        if (null == t) {
            return;
        }
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
            try {
                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

                ID id = t.getId();
                String entity = clz.getSimpleName();
                BaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) +
                        "ServiceImpl", BaseService.class);

                baseService.removeByColumn(BeanUtils.humpToUnderline(joinColumn.name()), id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void getOneToOne(T t) {
        if (null == t) {
            return;
        }
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

            ID id = t.getId();

            String entity = clz.getSimpleName();
            String firstId = BeanUtils.humpToUnderline(joinColumn.name());
            BaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl",
                    BaseService.class);
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

    protected void saveOneToOne(T t) {
        if (null == t) {
            return;
        }
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
            try {
                field.setAccessible(true);
                Object joinDataObj = field.get(t);
                if (null == joinDataObj) {
                    continue;
                }
                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

                ID id = t.getId();

                Field joinField = clz.getDeclaredField(joinColumn.name());
                joinField.setAccessible(true);
                joinField.set(joinDataObj, id);

                String entity = clz.getSimpleName();
                BaseService baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) +
                        "ServiceImpl", BaseService.class);
                baseService.save(joinDataObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void updateOneToOne(T t) {
        if (null == t) {
            return;
        }
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
            try {
                field.setAccessible(true);
                Object joinDataObj = field.get(t);
                if (null == joinDataObj) {
                    continue;
                }
                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

                ID id = t.getId();

                Field joinField = clz.getDeclaredField(joinColumn.name());
                joinField.setAccessible(true);
                joinField.set(joinDataObj, id);

                String entity = clz.getSimpleName();
                BaseService baseService =
                        SpringUtils.getBean(StringUtils.firstToLowerCase(entity) +
                        "ServiceImpl", BaseService.class);
                baseService.updateById(field.get(t));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void deleteOneToOne(T t) {
        if (null == t) {
            return;
        }
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
            try {
                field.setAccessible(true);
                Object joinDataObj = field.get(t);
                if (null == joinDataObj) {
                    continue;
                }
                ListEntity joinData = (ListEntity) joinDataObj;
                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];
                String entity = clz.getSimpleName();
                BaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) +
                        "ServiceImpl", BaseService.class);
                baseService.removeById(joinData.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void getManyToOne(T t) {
        if (null == t) {
            return;
        }
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
            Integer value;
            try {
                Field joinField = t.getClass().getDeclaredField(joinColumn.name());
                joinField.setAccessible(true);
                Object v = joinField.get(t);
                if (null != v) {
                    value = Integer.parseInt(v.toString());
                } else {
                    continue;
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
                continue;
            }

            String entity = clz.getSimpleName();
            BaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl",
                    BaseService.class);
            ListEntity<?> joinResult = baseService.getById(value);

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

    protected void saveManyToOne(T t) {
        if (null == t) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        boolean hasManyToOne = false;
        for (Field field : fields) {
            ManyToOne manyToMany = field.getAnnotation(ManyToOne.class);
            if (null == manyToMany) {
                continue;
            }
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null == joinColumn) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object joinDataObj = field.get(t);
                if (null == joinDataObj) {
                    continue;
                }
                ListEntity joinData = (ListEntity) joinDataObj;
                Field joinField = t.getClass().getDeclaredField(joinColumn.name());
                joinField.setAccessible(true);
                joinField.set(t, joinData.getId());
                hasManyToOne = true;
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if (hasManyToOne) {
            this.updateById(t);
        }
    }

    protected void updateManyToOne(T t) {
        this.saveManyToOne(t);
    }

    protected void deleteManyToOne(T t) {
    }


}
