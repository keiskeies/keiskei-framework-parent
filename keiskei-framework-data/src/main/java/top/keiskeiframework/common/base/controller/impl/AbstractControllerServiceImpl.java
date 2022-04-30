package top.keiskeiframework.common.base.controller.impl;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.controller.IControllerService;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.BaseSortVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.vo.R;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * controller实现抽象类
 * </p>
 * @param <T> .
 * @param <ID> .
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/3 20:51
 */
public abstract class AbstractControllerServiceImpl<T extends ListEntity<ID>, ID extends Serializable>
        implements IControllerService<T, ID> {

    @Autowired
    private BaseService<T, ID> baseService;


    @Override
    @ApiOperation("数量")
    public R<Long> count(BaseRequestVO<T, ID> request) {
        return R.ok(baseService.count(request));
    }

    @Override
    @ApiOperation("详情")
    public R<T> getOne(@PathVariable ID id) {
        return R.ok(baseService.getById(id));
    }

    @Override
    public R<T> getOne(BaseRequestVO<T, ID> request) {
        return R.ok(baseService.getOne(request));
    }

    @Override
    public R<Boolean> exist(BaseRequestVO<T, ID> request) {
        return R.ok(baseService.exist(request));
    }

    @Override
    @ApiOperation("新增")
    public R<T> save(@RequestBody @Validated({Insert.class}) T t) {
        baseService.save(t);
        return R.ok(t);
    }

    @Override
    @ApiOperation("新增")
    public R<List<T>> save(@RequestBody @Validated({Insert.class}) List<T> ts) {
        for (T t : ts) {
            baseService.save(t);
        }
        return R.ok(ts);
    }

    @Override
    @ApiOperation("更新")
    public R<T> update(@RequestBody @Validated({Update.class}) T t) {
        baseService.updateById(t);
        return R.ok(t);
    }

    @Override
    @ApiOperation("更新")
    public R<List<T>> update(@RequestBody @Validated({Update.class}) List<T> ts) {
        for (T t : ts) {
            baseService.updateById(t);
        }
        return R.ok(ts);
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> delete(@PathVariable ID id) {
        return R.ok(baseService.removeById(id));
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody List<ID> ids) {
        for (ID id : ids) {
            baseService.removeById(id);
        }
        return R.ok(Boolean.TRUE);
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> deleteByConditions(BaseRequestVO<T, ID> request) {
        return R.ok(baseService.removeByCondition(request.getConditions()));
    }



    @Override
    @ApiOperation("统计")
    public R<Map<String, Double>> statistic(
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
        ChartRequestDTO chartRequestDTO = new ChartRequestDTO();
        chartRequestDTO.setColumn(column);
        if (!StringUtils.isEmpty(timeField)) {
            chartRequestDTO.setTimeField(timeField);
        }
        chartRequestDTO.setColumnType(columnType);
        chartRequestDTO.setTimeDelta(timeDelta);
        if (!StringUtils.isEmpty(start)) {
            chartRequestDTO.setStart(DateTimeUtils.strToTime(start));
        }
        if (!StringUtils.isEmpty(end)) {
            chartRequestDTO.setEnd(DateTimeUtils.strToTime(end));
        }
        if (null != calcType) {
            chartRequestDTO.setCalcType(calcType);
            if (calcType.equals(CalcType.SUM)) {
                if (StringUtils.isEmpty(sumColumn)) {
                    throw new RuntimeException("求和字段：sumColumn 为空");
                }
                chartRequestDTO.setSumColumn(sumColumn);
            }
        }
        if (!StringUtils.isEmpty(conditions)) {
            chartRequestDTO.setConditions(JSON.parseArray(conditions, QueryConditionVO.class));
        }

        return R.ok(baseService.getChartOptions(chartRequestDTO));
    }

}
