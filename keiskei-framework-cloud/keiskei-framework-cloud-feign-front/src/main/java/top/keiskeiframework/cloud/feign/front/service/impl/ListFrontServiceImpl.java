package top.keiskeiframework.cloud.feign.front.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import top.keiskeiframework.cloud.feign.dto.ListEntityDTO;
import top.keiskeiframework.cloud.feign.front.service.IListFrontService;
import top.keiskeiframework.cloud.feign.service.IListFeignService;

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
public class ListFrontServiceImpl<T extends ListEntityDTO<ID>, ID extends Serializable> implements IListFrontService<T, ID> {

    @Autowired
    private IListFeignService<T, ID> listFeignService;

    @Override
    public Page<T> page(
            String conditions,
            String show,
            Integer page,
            Integer size,
            String desc,
            String asc
    ) {
        return listFeignService.list(
                conditions,
                show,
                page,
                size,
                desc,
                asc
        ).getData();
    }

    @Override
    public List<T> all(
            String conditions,
            String show
    ) {
        return listFeignService.all(
                conditions,
                show
        ).getData();
    }

    @Override
    public List<T> options(
            String conditions,
            String show,
            Integer page,
            Integer size,
            String desc,
            String asc
    ) {
        return listFeignService.options(
                conditions,
                show,
                page,
                size,
                desc,
                asc
        ).getData();
    }

    @Override
    public T findById(ID id) {
        return listFeignService.getOne(id).getData();
    }


    @Override
    public T findByColumn(String column, Serializable value) {
        return listFeignService.getOne(column, value).getData();
    }

    @Override
    public T save(T t) {
        return listFeignService.save(t).getData();
    }


    @Override
    public T update(T t) {
        return listFeignService.update(t).getData();
    }


    @Override
    public void deleteById(ID id) {
        listFeignService.delete(id);
    }
}
