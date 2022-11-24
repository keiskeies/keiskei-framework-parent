package top.keiskeiframework.common.base.controller.impl;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.controller.IControllerService;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.PageResultVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.vo.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * controller实现抽象类
 * </p>
 *
 * @param <T>  .
 * @param <ID> .
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/3 20:51
 */
@Order
public abstract class AbstractControllerServiceImpl<T extends IBaseEntity<ID>, ID extends Serializable>
        implements IControllerService<T, ID> {

    @Autowired
    protected IBaseService<T, ID> baseService;

    @Override
    @ApiOperation("列表")
    public R<PageResultVO<T>> page(BaseRequestVO<T, ID> request, BasePageVO page) {
        return R.ok(baseService.page(request, page));
    }

    @Override
    @ApiOperation("下拉框")
    public R<List<T>> options(BaseRequestVO<T, ID> request) {
        return R.ok(baseService.findListByCondition(request));
    }

    @Override
    @ApiOperation("数量")
    public R<Long> count(BaseRequestVO<T, ID> request) {
        return R.ok(baseService.getCount(request));
    }

    @Override
    @ApiOperation("详情")
    public R<T> getOne(@PathVariable ID id) {
        return R.ok(baseService.findOneById(id));
    }

    @Override
    @ApiOperation("详情")
    public R<T> getOne(BaseRequestVO<T, ID> request) {
        return R.ok(baseService.findOneByCondition(request));
    }

    @Override
    @ApiOperation("存在")
    public R<Boolean> exist(BaseRequestVO<T, ID> request) {
        return R.ok(baseService.exist(request));
    }

    @Override
    @ApiOperation("新增")
    public R<T> save(@RequestBody @Validated({Insert.class}) T t) {
        return R.ok(baseService.saveOne(t));
    }

    @Override
    @ApiOperation("更新")
    public R<T> update(@RequestBody @Validated({Update.class}) T t) {
        return R.ok(baseService.updateOne(t));
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> deleteById(@PathVariable ID id) {
        return R.ok(baseService.deleteOneById(id));
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> delete(T t) {
        return R.ok(baseService.deleteOneById(t.getId()));
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> deleteMulti(@RequestBody List<ID> ids) {
        return R.ok(baseService.deleteListByIds(ids));
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> deleteByConditions(List<QueryConditionVO> conditions) {
        return R.ok(baseService.deleteListByCondition(conditions));
    }

    @Override
    @ApiOperation("更新")
    public R<List<T>> update(@RequestBody @Validated({Update.class}) List<T> ts) {
        return R.ok(baseService.updateList(ts));
    }

    @Override
    @ApiOperation("新增")
    public R<List<T>> save(@RequestBody @Validated({Insert.class}) List<T> ts) {
        return R.ok(baseService.saveList(ts));
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
            chartRequestDTO.setConditions(new ArrayList<>(JSON.parseArray(conditions, QueryConditionVO.class)));
        }

        return R.ok(baseService.getChartOptions(chartRequestDTO));
    }

}
