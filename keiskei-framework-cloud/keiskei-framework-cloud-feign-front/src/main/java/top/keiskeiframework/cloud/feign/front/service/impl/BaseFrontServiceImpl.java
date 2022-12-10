package top.keiskeiframework.cloud.feign.front.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.keiskeiframework.cloud.feign.vo.PageRequestVO;
import top.keiskeiframework.cloud.feign.front.service.IBaseFrontService;
import top.keiskeiframework.cloud.feign.service.IBaseFeignService;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.PageResultVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
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
public class BaseFrontServiceImpl<T extends IBaseEntity<ID>, ID extends Serializable> implements IBaseFrontService<T, ID> {

    @Autowired
    protected IBaseFeignService<T, ID> baseFeignService;

    @Override
    public PageResultVO<T> page(BaseRequestVO requestVO, BasePageVO pageVO) {
        return baseFeignService.page(new PageRequestVO(requestVO, pageVO)).getData();
    }

    @Override
    public List<T> options(BaseRequestVO requestVO) {
        return baseFeignService.options(requestVO).getData();
    }

    @Override
    public T findById(ID id) {
        return baseFeignService.findById(id).getData();
    }


    @Override
    public T getOne(String conditions) {
        return baseFeignService.getOne(conditions).getData();
    }

    @Override
    public Long count(String conditions) {
        return baseFeignService.count(conditions).getData();
    }

    @Override
    public T save(T t) {
        return baseFeignService.save(t).getData();
    }

    @Override
    public List<T> save(List<T> ts) {
        return baseFeignService.save(ts).getData();
    }

    @Override
    public T update(T t) {
        return baseFeignService.update(t).getData();
    }

    @Override
    public List<T> update(List<T> ts) {
        return baseFeignService.update(ts).getData();
    }

    @Override
    public void deleteById(ID id) {
        baseFeignService.delete(id);
    }

    @Override
    public Boolean delete(List<ID> ids) {
        return baseFeignService.delete(ids).getData();
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
        return baseFeignService.statistic(
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
