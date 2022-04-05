package top.keiskeiframework.common.base.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.controller.IControllerService;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.BaseSortVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
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
public abstract class AbstractControllerServiceImpl<T extends ListEntity<ID>, ID extends Serializable> implements IControllerService<T, ID> {

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
        return R.ok(baseService.saveAll(ts));
    }

    @Override
    @ApiOperation("更新")
    public R<T> update(@RequestBody @Validated({Update.class}) T fieldInfo) {
        return R.ok(baseService.updateAndNotify(fieldInfo));
    }

    @Override
    @ApiOperation("更新")
    public R<List<T>> update(@RequestBody @Validated({Update.class}) List<T> ts) {
        return R.ok(baseService.updateAll(ts));
    }

    @Override
    @ApiOperation("更改排序")
    public R<Boolean> changeSort(@RequestBody @Validated BaseSortVO<ID> baseSortVO) {
        baseService.changeSort(baseSortVO);
        return R.ok(Boolean.TRUE);
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> delete(@PathVariable ID id) {
        baseService.deleteByIdAndNotify(id);
        return R.ok(Boolean.TRUE);
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody List<ID> ids) {
        baseService.deleteByIds(ids);
        return R.ok(Boolean.TRUE);
    }

    @Override
    @ApiOperation("统计")
    public R<Map<String, Double>> statistic(ChartRequestDTO chartRequestDTO) {
        return R.ok(baseService.getChartOptions(chartRequestDTO));
    }
}
