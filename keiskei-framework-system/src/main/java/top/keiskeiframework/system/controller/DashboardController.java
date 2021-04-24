package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.ListController;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.common.vo.CacheDTO;
import top.keiskeiframework.dashboard.entity.Dashboard;
import top.keiskeiframework.common.base.service.EntityFactory;
import top.keiskeiframework.dashboard.service.IDashboardService;

import java.util.List;
import java.util.Map;

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
public class DashboardController extends ListController<Dashboard> {

    @Autowired
    private IDashboardService dashboardService;

    @ApiOperation("实体")
    @GetMapping("/table")
    public R<List<CacheDTO>> table() {
        return R.ok(EntityFactory.getBASE_ENTITY_LIST());
    }

    @ApiOperation("数据")
    @GetMapping("/data")
    public R<Map<String, Object>> data(@RequestParam Long id) {

        return null;
    }


}
