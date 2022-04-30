package top.keiskeiframework.cloud.feign.front.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.keiskeiframework.cloud.feign.dto.ListEntityDTO;
import top.keiskeiframework.cloud.feign.dto.PageResultDTO;
import top.keiskeiframework.cloud.feign.enums.CalcType;
import top.keiskeiframework.cloud.feign.enums.ColumnType;
import top.keiskeiframework.cloud.feign.front.service.ICommonFrontService;
import top.keiskeiframework.cloud.feign.service.ICommonFeignService;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 基础查询接口
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class CommonFrontServiceImpl<T extends ListEntityDTO<ID>, ID extends Serializable> implements ICommonFrontService<T,
        ID> {

    @Autowired
    protected ICommonFeignService<T, ID> commonFeignService;

    @Override
    public PageResultDTO<T> page(
            String conditions,
            String show,
            Integer page,
            Integer size,
            String desc,
            String asc
    ) {
        return commonFeignService.page(
                conditions,
                show,
                page,
                size,
                desc,
                asc
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
        return commonFeignService.options(
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
        return commonFeignService.findById(id).getData();
    }


    @Override
    public T getOne(String conditions) {
        return commonFeignService.getOne(conditions).getData();
    }

    @Override
    public Integer count(String conditions) {
        return commonFeignService.count(conditions).getData();
    }

    @Override
    public T save(T t) {
        return commonFeignService.save(t).getData();
    }

    @Override
    public List<T> save(List<T> ts) {
        return commonFeignService.save(ts).getData();
    }

    @Override
    public T update(T t) {
        return commonFeignService.update(t).getData();
    }

    @Override
    public List<T> update(List<T> ts) {
        return commonFeignService.update(ts).getData();
    }

    @Override
    public void deleteById(ID id) {
        commonFeignService.delete(id);
    }

    @Override
    public Boolean deleteById(T t) {
        return commonFeignService.delete(t).getData();
    }

    @Override
    public Boolean delete(List<ID> ids) {
        return commonFeignService.delete(ids).getData();
    }

    @Override
    public Map<String, Double> statistic(
            String column,
            ColumnType columnType,
            String timeField,
            TimeDeltaEnum timeDelta,
            String start,
            String end,
            CalcType calcType,
            String sumColumn,
            String conditions
    ) {
        return commonFeignService.statistic(
                column,
                columnType,
                timeField,
                timeDelta,
                start,
                end,
                calcType,
                sumColumn,
                conditions
        ).getData();
    }
}
