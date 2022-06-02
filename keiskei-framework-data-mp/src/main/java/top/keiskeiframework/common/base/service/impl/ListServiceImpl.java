package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.annotation.annotation.Lockable;
import top.keiskeiframework.common.base.annotation.ManyToMany;
import top.keiskeiframework.common.base.annotation.ManyToOne;
import top.keiskeiframework.common.base.annotation.OneToMany;
import top.keiskeiframework.common.base.annotation.OneToOne;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.*;
import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.common.base.service.IListBaseService;
import top.keiskeiframework.common.base.service.IMiddleService;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.util.SpringUtils;
import top.keiskeiframework.common.vo.PageResult;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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
public class ListServiceImpl
        <T extends ListEntity<ID>, ID extends Serializable, M extends BaseMapper<T>>
        extends AbstractServiceImpl<T, ID, M>
        implements IListBaseService<T, ID>, IService<T> {

    @Autowired
    protected ListServiceImpl<T, ID, M> listService;

    @Override
    public PageResult<T> page(BaseRequestVO<T, ID> request, BasePageVO page) {
        PageResult<T> iPage = super.page(request, page);
        for (T record : iPage.getRecords()) {
            getManyToMany(record);
            getOneToMany(record);
            getManyToOne(record);
            getOneToOne(record);
        }
        return iPage;
    }

    @Override
    public T findOneById(Serializable id) {
        T t = listService.getById(id);
        getManyToMany(t);
        getOneToMany(t);
        getManyToOne(t);
        getOneToOne(t);
        return t;
    }


    @Override
    public List<T> findListByColumn(String column, Serializable value) {
        List<T> ts = super.findListByColumn(column, value);
        for (T t : ts) {
            getManyToMany(t);
            getOneToMany(t);
            getManyToOne(t);
            getOneToOne(t);
        }
        return ts;
    }

    @Override
    public List<T> findListByCondition(BaseRequestVO<T, ID> request) {
        List<T> ts = super.findListByCondition(request);
        for (T t : ts) {
            getManyToMany(t);
            getOneToMany(t);
            getManyToOne(t);
            getOneToOne(t);
        }
        return ts;
    }



    @Override
    @Cacheable(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id", unless = "#result == null")
    public T getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @Lockable(key = "targetClass.name + ':' + #t.hashCode()")
    public boolean save(T t) {
        saveManyToOne(t);
        super.baseMapper.insert(t);
        saveManyToMany(t);
        saveOneToMany(t);
        saveOneToOne(t);
        return true;
    }


    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")
    public boolean updateById(T t) {
        T told = listService.getById(t.getId());
        BeanUtils.copyPropertiesIgnoreNull(t, told);
        updateManyToOne(told);
        super.updateById(told);
        updateManyToMany(told);
        updateOneToMany(told);
        updateOneToOne(told);
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean updateBatchById(Collection<T> entityList) {

        if (hasRelation()) {
            for (T t : entityList) {
                listService.updateById(t);
            }
            return true;
        }

        super.updateBatchById(entityList, DEFAULT_BATCH_SIZE);
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")
    public T updateOne(T t) {
        return super.updateOne(t);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public List<T> updateList(List<T> ts) {
        return super.updateList(ts);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    public boolean deleteOneById(ID id) {
        return super.deleteOneById(id);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean deleteListByIds(Collection<ID> ids) {
        return super.deleteListByIds(ids);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean updateListByCondition(List<QueryConditionVO> conditions, T t) {
        return super.updateListByCondition(conditions, t);
    }

    @Override
    public boolean saveOrUpdate(T t) {
        if (null != t.getId()) {
            return listService.updateById(t);
        } else {
            return listService.save(t);
        }
    }

    @Override
    public boolean saveBatch(Collection<T> entityList) {
        if (hasRelation()) {
            for (T t : entityList) {
                listService.save(t);
            }
            return true;
        }
        return super.saveBatch(entityList, DEFAULT_BATCH_SIZE);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean saveOrUpdateBatch(Collection<T> ts) {
        return super.saveOrUpdateBatch(ts);
    }

    @Override
    public boolean removeById(T t) {
        return listService.removeById(t.getId());
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean remove(Wrapper<T> queryWrapper) {
        return super.remove(queryWrapper);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean removeByIds(Collection<?> list, boolean useFill) {
        return super.removeByIds(list, useFill);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean removeBatchByIds(Collection<?> list, boolean useFill) {
        return super.removeBatchByIds(list, useFill);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean update(Wrapper<T> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    public boolean removeById(Serializable id) {
        T t = listService.getById(id);
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
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean deleteListByColumn(String column, Serializable value) {
        return this.deleteListByColumn(column, value);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean deleteListByCondition(List<QueryConditionVO> conditions) {
        return super.deleteListByCondition(conditions);
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
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
            if (null != manyToMany) {
                IMiddleService<?, ?, ?> middleService = SpringUtils.getBean(
                        StringUtils.firstToLowerCase(manyToMany.middleClass().getSimpleName()) + "ServiceImpl",
                        IMiddleService.class);
                IService<?> baseService = SpringUtils.getBean(
                        StringUtils.firstToLowerCase(manyToMany.targetClass().getSimpleName()) + "ServiceImpl",
                        IService.class
                );

                List joinData = new ArrayList<>();
                if (manyToMany.id1()) {
                    List<? extends IMiddleEntity<?, ?>> middleData = middleService.getById1(t.getId());
                    if (!CollectionUtils.isEmpty(middleData)) {
                        for (IMiddleEntity<?, ?> iMiddleEntity : middleData) {
                            joinData.add(baseService.getById((Serializable) iMiddleEntity.getId2()));
                        }
                    }
                } else {
                    List<? extends IMiddleEntity<?, ?>> middleData = middleService.getById2(t.getId());
                    if (!CollectionUtils.isEmpty(middleData)) {
                        for (IMiddleEntity<?, ?> iMiddleEntity : middleData) {
                            joinData.add(baseService.getById((Serializable) iMiddleEntity.getId1()));
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(joinData)) {
                    field.setAccessible(true);
                    try {
                        field.set(t, joinData);
                    } catch (IllegalAccessException e) {
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
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
            if (null != manyToMany) {

                try {
                    field.setAccessible(true);
                    Object joDataObj = field.get(t);
                    if (null == joDataObj) {
                        continue;
                    }
                    Collection<BaseEntity<?>> joinData = (Collection<BaseEntity<?>>) joDataObj;
                    if (!CollectionUtils.isEmpty(joinData)) {

                        IMiddleService middleService = SpringUtils.getBean(
                                StringUtils.firstToLowerCase(manyToMany.middleClass().getSimpleName()) + "ServiceImpl",
                                IMiddleService.class);
                        IBaseService baseService = SpringUtils.getBean(
                                StringUtils.firstToLowerCase(manyToMany.targetClass().getSimpleName()) + "ServiceImpl",
                                IBaseService.class);

                        for (BaseEntity joinObj : joinData) {
                            if (null == joinObj.getId()) {
                                baseService.saveOne(joinObj);
                            }
                        }

                        List<MiddleEntity> middleEntities = new ArrayList<>(joinData.size());
                        for (BaseEntity baseEntity : joinData) {
                            try {
                                MiddleEntity middleEntity = manyToMany.middleClass().newInstance();
                                if (manyToMany.id1()) {
                                    middleEntity.setId1(t.getId());
                                    middleEntity.setId2(baseEntity.getId());
                                } else {
                                    middleEntity.setId1(baseEntity.getId());
                                    middleEntity.setId2(t.getId());
                                }
                                middleEntities.add(middleEntity);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            }
                        }
                        if (manyToMany.id1()) {
                            middleService.saveOrUpdateById1(middleEntities);
                        } else {
                            middleService.saveOrUpdateById2(middleEntities);
                        }

                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void updateManyToMany(T t) {
        if (null == t) {
            return;
        }
        boolean hasManyToMany = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
            if (null != manyToMany) {
                hasManyToMany = true;
                break;
            }
        }
        if (hasManyToMany) {
            this.deleteManyToMany(t);
            this.saveManyToMany(t);
        }
    }

    protected void deleteManyToMany(T t) {
        if (null == t) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
            if (null != manyToMany) {
                Class<? extends MiddleEntity<?, ?>> middleClass = manyToMany.middleClass();
                String entity = middleClass.getSimpleName();
                IMiddleService middleService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl",
                        IMiddleService.class);
                if (manyToMany.id1()) {
                    middleService.removeById1(t.getId());
                } else {
                    middleService.removeById2(t.getId());
                }
            }

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
            Type type = field.getGenericType();
            ParameterizedType pt = (ParameterizedType) type;
            Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

            ID id = t.getId();

            String entity = clz.getSimpleName();
            String firstId = BeanUtils.humpToUnderline(oneToMany.filedName());
            IBaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl",
                    IBaseService.class);
            List<?> joinResult = baseService.findListByColumn(firstId, id);

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
            try {
                field.setAccessible(true);
                Object joDataObj = field.get(t);
                if (null == joDataObj) {
                    continue;
                }
                Collection<?> joinData = (Collection<?>) joDataObj;
                if (CollectionUtils.isEmpty(joinData)) {
                    continue;
                }

                Class<?> joinClass = oneToMany.targetClass();
                Field joinField = joinClass.getDeclaredField(oneToMany.filedName());
                joinField.setAccessible(true);
                for (Object joinDatum : joinData) {
                    joinField.set(joinDatum, t.getId());
                }

                IService baseService = SpringUtils.getBean(
                        StringUtils.firstToLowerCase(joinClass.getSimpleName()) + "ServiceImpl",
                        IService.class);
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
            try {
                Object joDataObj = field.get(t);
                if (null == joDataObj) {
                    continue;
                }
                Collection joinData = (Collection) joDataObj;
                if (CollectionUtils.isEmpty(joinData)) {
                    continue;
                }

                Class<?> joinClass = oneToMany.targetClass();

                ID id = t.getId();
                Field joinField = joinClass.getDeclaredField(oneToMany.filedName());
                joinField.setAccessible(true);
                for (Object joinDatum : joinData) {
                    joinField.set(joinDatum, id);
                }

                IService baseService = SpringUtils.getBean(
                        StringUtils.firstToLowerCase(joinClass.getSimpleName()) + "ServiceImpl",
                        IService.class);
                baseService.removeByMap(Collections.singletonMap(oneToMany.filedName(), id));
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
            try {
                ID id = t.getId();
                IBaseService baseService = SpringUtils.getBean(
                        StringUtils.firstToLowerCase(oneToMany.targetClass().getSimpleName()) + "ServiceImpl",
                        IBaseService.class);
                baseService.deleteListByColumn(BeanUtils.humpToUnderline(oneToMany.filedName()), id);
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
            Type type = field.getGenericType();
            ParameterizedType pt = (ParameterizedType) type;
            Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

            ID id = t.getId();

            String entity = clz.getSimpleName();
            String firstId = BeanUtils.humpToUnderline(oneToOne.filedName());
            IBaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl",
                    IBaseService.class);
            IBaseEntity joinResult = baseService.findOneByColumn(firstId, id);

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

                Field joinField = clz.getDeclaredField(oneToOne.filedName());
                joinField.setAccessible(true);
                joinField.set(joinDataObj, id);

                String entity = clz.getSimpleName();
                IService baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) +
                        "ServiceImpl", IService.class);
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

                Field joinField = clz.getDeclaredField(oneToOne.filedName());
                joinField.setAccessible(true);
                joinField.set(joinDataObj, id);

                String entity = clz.getSimpleName();
                IService baseService = SpringUtils.getBean(
                        StringUtils.firstToLowerCase(entity) + "ServiceImpl", IService.class);
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
            try {
                field.setAccessible(true);
                Object joinDataObj = field.get(t);
                if (null == joinDataObj) {
                    continue;
                }
                BaseEntity<?> joinData = (BaseEntity<?>) joinDataObj;
                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];
                String entity = clz.getSimpleName();
                IService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) +
                        "ServiceImpl", IService.class);
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

            Class<?> clz = field.getType();
            int value;
            try {
                Field joinField = t.getClass().getDeclaredField(manyToMany.filedName());
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
            IBaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl",
                    IBaseService.class);
            IBaseEntity<?> joinResult = baseService.findOneById(value);

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
            try {
                field.setAccessible(true);
                Object joinDataObj = field.get(t);
                if (null == joinDataObj) {
                    continue;
                }
                ListEntity<?> joinData = (ListEntity<?>) joinDataObj;
                Field joinField = t.getClass().getDeclaredField(manyToMany.filedName());
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
