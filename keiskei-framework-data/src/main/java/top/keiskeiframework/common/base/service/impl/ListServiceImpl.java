package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.BaseService;

import java.io.Serializable;
import java.util.List;

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
public class ListServiceImpl<T extends ListEntity> extends AbstractListBaseServiceImpl<T> implements BaseService<T>, IService<T> {

    @Autowired
    private ListServiceImpl<T> listService;

    @Override
    public IPage<T> page(BaseRequestVO<T> request, BasePageVO<T> page) {
        IPage<T> iPage = super.page(request, page);
        for (T record : iPage.getRecords()) {
            getManyToMany(record);
            getOneToMany(record);
            getManyToOne(record);
            getOneToOne(record);
        }
        return iPage;
    }

    @Override
    public T findById(Long id) {
        T t = super.findByIdCache(id);
        getManyToMany(t);
        getOneToMany(t);
        getManyToOne(t);
        getOneToOne(t);
        return t;
    }


    @Override
    public List<T> findAllByColumn(String column, Serializable value) {
        List<T> ts = super.findAllByColumn(column, value);
        for (T t : ts) {
            getManyToMany(t);
            getOneToMany(t);
            getManyToOne(t);
            getOneToOne(t);
        }
        return ts;
    }

}
