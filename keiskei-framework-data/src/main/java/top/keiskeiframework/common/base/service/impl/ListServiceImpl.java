package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.common.base.service.IListBaseService;

import java.io.Serializable;
import java.util.List;

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
        extends AbstractListServiceImpl<T, ID, M>
        implements IListBaseService<T, ID>, IBaseService<T, ID>, IService<T> {

    @Override
    public Page<T> pageComplete(BaseRequestVO<T, ID> request, BasePageVO page) {
        Page<T> iPage = super.page(request, page);
        for (T record : iPage.getRecords()) {
            getManyToMany(record);
            getOneToMany(record);
            getManyToOne(record);
            getOneToOne(record);
        }
        return iPage;
    }

    @Override
    public T getByIdComplete(Serializable id) {
        T t = listBaseService.getById(id);
        getManyToMany(t);
        getOneToMany(t);
        getManyToOne(t);
        getOneToOne(t);
        return t;
    }


    @Override
    public List<T> listByColumn(String column, Serializable value) {
        List<T> ts = super.listByColumn(column, value);
        for (T t : ts) {
            getManyToMany(t);
            getOneToMany(t);
            getManyToOne(t);
            getOneToOne(t);
        }
        return ts;
    }

    @Override
    public List<T> listComplete(BaseRequestVO<T, ID> request) {
        List<T> ts = super.list(request);
        for (T t : ts) {
            getManyToMany(t);
            getOneToMany(t);
            getManyToOne(t);
            getOneToOne(t);
        }
        return ts;
    }
}
