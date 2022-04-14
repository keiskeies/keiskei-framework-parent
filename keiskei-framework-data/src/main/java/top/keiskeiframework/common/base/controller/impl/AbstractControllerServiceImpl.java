package top.keiskeiframework.common.base.controller.impl;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.vo.R;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * controller实现抽象类
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/3 20:51
 */
public abstract class AbstractControllerServiceImpl<T extends ListEntity> implements IControllerService<T> {

    @Autowired
    private BaseService<T> baseService;


    @Override
    @ApiOperation("数量")
    public R<Long> count(BaseRequestVO<T> request) {
        return R.ok(baseService.count(request));
    }

    @Override
    @ApiOperation("详情")
    public R<T> getOne(@PathVariable Long id) {
        return R.ok(baseService.findById(id));
    }

    @Override
    public R<T> getOne(String column, Serializable value) {
        return R.ok(baseService.findByColumn(column, value));
    }

    @Override
    @ApiOperation("新增")
    public R<T> save(@RequestBody @Validated({Insert.class}) T t) {
        return R.ok(baseService.saveAndNotify(t));
    }

    @Override
    @ApiOperation("新增")
    public R<List<T>> save(@RequestBody @Validated({Insert.class}) List<T> ts) {
        baseService.saveBatch(ts);
        return R.ok(ts);
    }

    @Override
    @ApiOperation("更新")
    public R<T> update(@RequestBody @Validated({Update.class}) T fieldInfo) {
        return R.ok(baseService.updateAndNotify(fieldInfo));
    }

    @Override
    @ApiOperation("更新")
    public R<List<T>> update(@RequestBody @Validated({Update.class}) List<T> ts) {
        for (T t : ts) {
            baseService.updateAndNotify(t);
        }
        return R.ok(ts);
    }

    @Override
    @ApiOperation("更改排序")
    public R<Boolean> changeSort(@RequestBody @Validated BaseSortVO baseSortVO) {
        baseService.changeSort(baseSortVO);
        return R.ok(Boolean.TRUE);
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> delete(@PathVariable Long id) {
        baseService.deleteByIdAndNotify(id);
        return R.ok(Boolean.TRUE);
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody List<Long> ids) {
        for (Long id : ids) {
            baseService.deleteByIdAndNotify(id);
        }
        return R.ok(Boolean.TRUE);
    }

    @Override
    @ApiOperation("统计")
    public R<Map<String, Double>> statistic(
            String column,
            String timeField,
            ColumnType columnType,
            TimeDeltaEnum timeDelta,
            String start,
            String end,
            CalcType calcType,
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
        }
        if (!StringUtils.isEmpty(conditions)) {
            chartRequestDTO.setConditions(JSON.parseArray(conditions, QueryConditionVO.class));
        }

        return R.ok(baseService.getChartOptions(chartRequestDTO));
    }
}
