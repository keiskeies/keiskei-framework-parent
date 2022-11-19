package top.keiskeiframework.performance.service;

import top.keiskeiframework.common.base.service.IListBaseService;
import top.keiskeiframework.performance.entity.InspiritDistribution;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/9/27 22:19
 */
public interface IInspiritDistributionService extends IListBaseService<InspiritDistribution, Integer> {

    /**
     * 通过季度和部门查询
     * @param quarterTableId 季度ID
     * @return 。
     */
    List<InspiritDistribution> findListByQuarterTableIdAndDepartment(Integer quarterTableId);
}
