package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.cache.service.CacheStorageService;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.injector.dto.ManyToManyResult;
import top.keiskeiframework.common.base.mapper.BaseEntityMapper;
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
public abstract class AbstractListBaseServiceImpl<T extends ListEntity<ID>, ID extends Serializable,
        M extends BaseEntityMapper<T, ID>>
        extends AbstractBaseServiceImpl<T, ID, M>
        implements ListBaseService<T, ID>, BaseService<T, ID>, IService<T> {

    @Autowired
    protected ListBaseService<T, ID> listBaseService;
    @Autowired
    protected CacheStorageService cacheStorageService;


    @Override
    @Cacheable(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id", unless = "#result == null")
    public T getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @Lockable(key = "targetClass.name + ':' + #t.hashCode()")
    public boolean save(T t) {
        saveManyToOne(t);
        super.save(t);
        saveManyToMany(t);
        saveOneToMany(t);
        saveOneToOne(t);
        return true;
    }


    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")
    public boolean updateById(T t) {
        T told = listBaseService.getById(t.getId());
        BeanUtils.copyPropertiesIgnoreNull(t, told);
        updateManyToOne(told);
        super.updateById(told);
        updateManyToMany(told);
        updateOneToMany(told);
        updateOneToOne(told);
        return true;
    }

    @Override
    public boolean updateBatchById(Collection<T> entityList) {

        if (hasRelation()) {
            for (T t : entityList) {
                listBaseService.updateById(t);
            }
            return true;
        }

        super.updateBatchById(entityList, DEFAULT_BATCH_SIZE);
        List<String> cacheKeys =
                entityList.stream().map(e -> CACHE_LIST_NAME + "::" + this.getClass().getName() +
                        ":" + e.getId()).collect(Collectors.toList());
        cacheStorageService.del(cacheKeys);
        return true;
    }

    @Override
    public boolean saveOrUpdate(T t) {
        if (null != t.getId()) {
            return listBaseService.updateById(t);
        } else {
            return listBaseService.save(t);
        }
    }

    @Override
    public boolean saveBatch(Collection<T> entityList) {
        if (hasRelation()) {
            for (T t : entityList) {
                listBaseService.save(t);
            }
            return true;
        }
        return super.saveBatch(entityList, DEFAULT_BATCH_SIZE);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<T> ts) {
        if (hasRelation()) {
            for (T t : ts) {
                listBaseService.saveOrUpdate(t);
            }
            return true;
        }
        List<String> cacheKeys = ts.stream().filter(t -> null != t.getId()).map(e ->
                CACHE_LIST_NAME + "::" + this.getClass().getName() +
                        ":" + ((ListEntity<?>) e).getId()).collect(Collectors.toList());
        super.saveOrUpdateBatch(ts, DEFAULT_BATCH_SIZE);
        cacheStorageService.del(cacheKeys);
        return true;
    }

    @Override
    public boolean removeById(T t) {
        return listBaseService.removeById(t.getId());
    }

    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        List<T> ts = super.listByMap(columnMap);
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        for (T t : ts) {
            listBaseService.removeById(t.getId());
        }
        return true;
    }

    @Override
    public boolean remove(Wrapper<T> queryWrapper) {
        List<T> ts = super.list(queryWrapper);
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        for (T t : ts) {
            listBaseService.removeById(t.getId());
        }
        return true;
    }

    @Override
    public boolean removeByIds(Collection<?> list, boolean useFill) {
        super.removeBatchByIds(list, DEFAULT_BATCH_SIZE, useFill);
        List<String> cacheKeys = list.stream().map(e -> CACHE_LIST_NAME + "::" + this.getClass().getName() +
                ":" + ((ListEntity<?>) e).getId()).collect(Collectors.toList());
        cacheStorageService.del(cacheKeys);
        return true;
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        super.removeBatchByIds(list, DEFAULT_BATCH_SIZE);
        List<String> cacheKeys = list.stream().map(e -> CACHE_LIST_NAME + "::" + this.getClass().getName() +
                ":" + ((ListEntity<?>) e).getId()).collect(Collectors.toList());
        cacheStorageService.del(cacheKeys);
        return true;
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list, boolean useFill) {
        super.removeBatchByIds(list, DEFAULT_BATCH_SIZE, useFill);
        List<String> cacheKeys = list.stream().map(e -> CACHE_LIST_NAME + "::" + this.getClass().getName() +
                ":" + ((ListEntity<?>) e).getId()).collect(Collectors.toList());
        cacheStorageService.del(cacheKeys);
        return true;
    }

    @Override
    public boolean update(Wrapper<T> updateWrapper) {
        List<T> ts = super.list(updateWrapper);
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        super.update(updateWrapper);
        List<String> cacheKeys = ts.stream().map(e -> CACHE_LIST_NAME + "::" + this.getClass().getName() +
                ":" + e.getId()).collect(Collectors.toList());
        cacheStorageService.del(cacheKeys);
        return true;
    }

    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {

        List<T> ts = super.list(new QueryWrapper<>(entity));
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        super.update(entity, updateWrapper);
        List<String> cacheKeys = ts.stream().map(e -> CACHE_LIST_NAME + "::" + this.getClass().getName() +
                ":" + e.getId()).collect(Collectors.toList());
        cacheStorageService.del(cacheKeys);
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    public boolean removeById(Serializable id) {
        T t = listBaseService.getById(id);
        if (null != t) {
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
        List<T> ts = listBaseService.listByColumn(column, value);
        for (T t : ts) {
            listBaseService.removeById(t.getId());
        }
        return true;
    }

    @Override
    public boolean removeByCondition(List<QueryConditionVO> conditions) {
        Class<T> tClass = getEntityClass();
        QueryWrapper<T> queryWrapper = BaseRequestUtils.getQueryWrapperByConditions(conditions, tClass);
        List<T> ts = listBaseService.list(queryWrapper);
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        for (T t : ts) {
            super.removeById(t.getId());
        }
        return true;
    }

    protected boolean hasRelation() {
        Class<T> tClass = getEntityClass();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            if (null != field.getAnnotation(ManyToMany.class)) {
                return true;
            }
            if (null != field.getAnnotation(ManyToOne.class)) {
                return true;
            }
            if (null != field.getAnnotation(OneToMany.class)) {
                return true;
            }
            if (null != field.getAnnotation(OneToOne.class)) {
                return true;
            }
        }
        return false;
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
                try {
                    field.setAccessible(true);
                    Object joDataObj = field.get(t);
                    if (null == joDataObj) {
                        continue;
                    }
                    Collection<ListEntity> joinData = (Collection<ListEntity>) joDataObj;
                    if (CollectionUtils.isEmpty(joinData)) {
                        continue;
                    }

                    Type type = field.getGenericType();
                    ParameterizedType pt = (ParameterizedType) type;
                    Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

                    String entity = clz.getSimpleName();
                    BaseService baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) +
                            "ServiceImpl", BaseService.class);
                    for (ListEntity joinObj : joinData) {
                        if (null == joinObj.getId()) {
                            baseService.save(joinObj);
                        }
                    }
                    hasManyToMany = true;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
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
                try {
                    field.setAccessible(true);
                    Object joDataObj = field.get(t);
                    if (null == joDataObj) {
                        continue;
                    }
                    Collection<ListEntity> joinData = (Collection<ListEntity>) joDataObj;
                    if (CollectionUtils.isEmpty(joinData)) {
                        continue;
                    }

                    Type type = field.getGenericType();
                    ParameterizedType pt = (ParameterizedType) type;
                    Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

                    String entity = clz.getSimpleName();
                    BaseService baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) +
                            "ServiceImpl", BaseService.class);
                    for (ListEntity joinObj : joinData) {
                        if (null == joinObj.getId()) {
                            baseService.save(joinObj);
                        }
                    }
                    hasManyToMany = true;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
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
                field.setAccessible(true);
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
                BaseService baseService = SpringUtils.getBean(
                        StringUtils.firstToLowerCase(entity) + "ServiceImpl", BaseService.class);
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
                ListEntity<?> joinData = (ListEntity<?>) joinDataObj;
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
            int value;
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
                ListEntity<?> joinData = (ListEntity<?>) joinDataObj;
                Field joinField = t.getClass().getDeclaredField(joinColumn.name());
                joinField.setAccessible(true);
                joinField.set(t, joinData.getId());
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    protected void updateManyToOne(T t) {
        this.saveManyToOne(t);
    }

    protected void deleteManyToOne(T t) {
    }


}
