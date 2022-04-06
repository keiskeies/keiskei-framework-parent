package top.keiskeiframework.cloud.feign.front.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import top.keiskeiframework.cloud.feign.dto.ListEntityDTO;
import top.keiskeiframework.cloud.feign.enums.CalcType;
import top.keiskeiframework.cloud.feign.enums.ColumnType;
import top.keiskeiframework.cloud.feign.front.service.IListFrontService;
import top.keiskeiframework.cloud.feign.service.IListFeignService;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;

import java.io.Serializable;
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
        return listFeignService.page(
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
    public List<T> save(List<T> ts) {
        return listFeignService.save(ts).getData();
    }

    @Override
    public T update(T t) {
        return listFeignService.update(t).getData();
    }

    @Override
    public List<T> update(List<T> ts) {
        return listFeignService.update(ts).getData();
    }

    @Override
    public void deleteById(ID id) {
        listFeignService.delete(id);
    }

    @Override
    public Boolean delete(List<ID> ids) {
        return listFeignService.delete(ids).getData();
    }

    @Override
    public Map<String, Double> statistic(
            String column,
            String timeField,
            ColumnType columnType,
            TimeDeltaEnum timeDelta,
            String start,
            String end,
            CalcType calcType,
            String conditions
    ) {
        return listFeignService.statistic(
                column,
                timeField,
                columnType,
                timeDelta,
                start,
                end,
                calcType,
                conditions
        ).getData();
    }
}
