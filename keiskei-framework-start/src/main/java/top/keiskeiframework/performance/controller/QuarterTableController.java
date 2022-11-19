package top.keiskeiframework.performance.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.impl.ListControllerImpl;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.performance.entity.InspiritDistribution;
import top.keiskeiframework.performance.entity.QuarterTable;
import top.keiskeiframework.performance.service.IInspiritDistributionService;
import top.keiskeiframework.performance.service.IQuarterTableService;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/9/27 22:25
 */
@RestController
@RequestMapping("/performance/quarterTable")
@Api(tags = "激励系统 - 季度分配")
public class QuarterTableController extends ListControllerImpl<QuarterTable, Integer> {

    @Autowired
    private IQuarterTableService quarterTableService;
    @Autowired
    private IInspiritDistributionService inspiritDistributionService;



    @GetMapping("/inspirit/{quarterId}")
    public R<List<InspiritDistribution>> listInspirit(@PathVariable Integer quarterId) {
        QuarterTable quarterTable = quarterTableService.findOneById(quarterId);
        return R.ok(inspiritDistributionService.findListByQuarterTableIdAndDepartment(quarterId));

    }




}
