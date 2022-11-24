package top.keiskeiframework.cloud.feign.front.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.cloud.feign.front.service.IListFrontService;
import top.keiskeiframework.cloud.feign.service.IListFeignService;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.PageResultVO;
import top.keiskeiframework.common.base.entity.IListEntity;
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
public class ListFrontServiceImpl<T extends IListEntity<ID>, ID extends Serializable> implements IListFrontService<T, ID> {

    @Autowired
    private IListFeignService<T, ID> listFeignService;

    @Override
    public PageResultVO<T> page(BaseRequestVO<T, ID> requestVO, BasePageVO pageVO) {
        return listFeignService.page(
                requestVO.getConditions(),
                requestVO.getShow(),
                pageVO.getOffset(),
                pageVO.getPage(),
                pageVO.getSize(),
                requestVO.getDesc(),
                requestVO.getAsc(),
                requestVO.getComplete()
        ).getData();
    }


    @Override
    public List<T> options(BaseRequestVO<T, ID> requestVO) {
        return listFeignService.options(
                requestVO.getConditions(),
                requestVO.getShow(),
                requestVO.getDesc(),
                requestVO.getAsc(),
                requestVO.getComplete()
        ).getData();
    }

    @Override
    public T findById(ID id) {
        return listFeignService.findById(id, true).getData();
    }

    @Override
    public T getOne(String conditions) {
        return listFeignService.getOne(conditions).getData();
    }

    @Override
    public Long count(String conditions) {
        return listFeignService.count(conditions).getData();
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
            ColumnType columnType,
            String timeField,
            TimeDeltaEnum timeDelta,
            String start,
            String end,
            CalcType calcType,
            String sumColumn,
            String conditions
    ) {
        return listFeignService.statistic(
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
