package top.keiskeiframework.performance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.enums.ConditionEnum;
import top.keiskeiframework.common.base.mp.service.impl.MpListServiceImpl;
import top.keiskeiframework.common.util.MdcUtils;
import top.keiskeiframework.common.util.TreeEntityUtils;
import top.keiskeiframework.performance.entity.InspiritDistribution;
import top.keiskeiframework.performance.mapper.InspiritDistributionMapper;
import top.keiskeiframework.performance.service.IInspiritDistributionService;
import top.keiskeiframework.performance.service.IQuarterTableService;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/9/27 22:19
 */
@Service
public class InspiritDistributionServiceImpl extends MpListServiceImpl<InspiritDistribution, Integer, InspiritDistributionMapper> implements IInspiritDistributionService {

    @Autowired
    private IQuarterTableService quarterTableService;

    @Override
    public List<InspiritDistribution> findListByQuarterTableIdAndDepartment(Integer quarterTableId) {
        String departmentSign = MdcUtils.getUserDepartment();
        BaseRequestVO<InspiritDistribution, Integer> request = new BaseRequestVO();
        request.addCondition(new QueryConditionVO("quarterTableId", ConditionEnum.EQ, quarterTableId));
        request.addCondition(new QueryConditionVO("sign", ConditionEnum.LL, departmentSign));
        List<InspiritDistribution> list = super.findListByCondition(request);
        TreeEntityUtils<InspiritDistribution, Integer> treeEntityUtils = new TreeEntityUtils<>(list);
        return treeEntityUtils.getTree(null);
    }

}
