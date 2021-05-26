package top.keiskeiframework.dashboard.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.service.EntityFactory;
import top.keiskeiframework.common.dto.base.BaseSortDTO;
import top.keiskeiframework.common.dto.cache.CacheDTO;
import top.keiskeiframework.common.enums.CacheTimeEnum;
import top.keiskeiframework.common.util.SecurityUtils;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.dashboard.entity.Dashboard;
import top.keiskeiframework.dashboard.service.IDashboardService;

import java.util.List;

/**
 * <p>
 * 操作日志 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@RestController
@RequestMapping("/admin/v1/system/dashboard")
@Api(tags = "主页 - 图表")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;

    @ApiOperation("列表")
    @GetMapping
    public R<List<Dashboard>> list() {
        Dashboard dashboard = Dashboard.builder().createUserId(SecurityUtils.getSessionUser().getId()).build();
        return R.ok(dashboardService.options(dashboard));
    }

    @ApiOperation("详情")
    @GetMapping("/{id:-?[\\d]+}")
    public R<?> getOne(@PathVariable Long id) {
        return R.ok(dashboardService.getChartOption(id));
    }

    @PostMapping
    @ApiOperation("新增")
    public R<Dashboard> save(@RequestBody @Validated({Insert.class}) Dashboard fieldInfo) {
        return R.ok(dashboardService.save(fieldInfo));
    }

    @PutMapping
    @ApiOperation("更新")
    public R<Dashboard> update(@RequestBody @Validated({Update.class}) Dashboard fieldInfo) {
        fieldInfo = dashboardService.update(fieldInfo);
        return R.ok(fieldInfo);
    }

    @PutMapping("/sort")
    @ApiOperation("更改排序")
    public R<Boolean> changeSort(@RequestBody @Validated BaseSortDTO baseSortDto) {
        dashboardService.changeSort(baseSortDto);
        return R.ok(Boolean.TRUE);
    }

    @DeleteMapping("/{id:-?[\\d]+}")
    @ApiOperation("删除")
    public R<Boolean> delete(@PathVariable Long id) {
        dashboardService.deleteById(id);
        return R.ok(Boolean.TRUE);
    }

    @ApiOperation("实体")
    @GetMapping("/table")
    public R<List<CacheDTO>> table() {
        return R.ok(EntityFactory.getBASE_ENTITY_LIST());
    }


    @ApiOperation("字段")
    @GetMapping("/table/field")
    @Cacheable(cacheNames = CacheTimeEnum.M10, key = "#targetClass + '-field-' + #entityClass")
    public R<List<CacheDTO>> tableField(@RequestParam String entityClass) {
        return R.ok(EntityFactory.getEntityInfo(entityClass));
    }


}