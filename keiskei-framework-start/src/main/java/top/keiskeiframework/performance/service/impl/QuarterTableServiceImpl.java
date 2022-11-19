package top.keiskeiframework.performance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.base.entity.ITreeEntity;
import top.keiskeiframework.common.base.mp.service.impl.MpListServiceImpl;
import top.keiskeiframework.common.util.TreeEntityUtils;
import top.keiskeiframework.performance.entity.InspiritDistribution;
import top.keiskeiframework.performance.entity.QuarterTable;
import top.keiskeiframework.performance.mapper.QuarterTableMapper;
import top.keiskeiframework.performance.service.IInspiritDistributionService;
import top.keiskeiframework.performance.service.IQuarterTableService;
import top.keiskeiframework.system.entity.Department;
import top.keiskeiframework.system.service.IDepartmentService;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/9/27 22:17
 */
@Service
public class QuarterTableServiceImpl extends MpListServiceImpl<QuarterTable, Integer, QuarterTableMapper> implements IQuarterTableService {

    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IInspiritDistributionService inspiritDistributionService;

    @Override
    public QuarterTable saveOne(QuarterTable quarterTable) {
        quarterTable = super.saveOne(quarterTable);

        List<Department> departments = departmentService.findList();
        TreeEntityUtils<Department, Integer> departmentTree = new TreeEntityUtils<>(departments);
        List<Department> departmentTreeSelect = departmentTree.getTree(quarterTable.getDepartmentId());
        for (Department department : departmentTreeSelect) {
            this.saveInspiritDistributions(quarterTable, department, null);
        }

        return quarterTable;
    }

    private void saveInspiritDistributions(QuarterTable quarterTable, ITreeEntity<Integer> department, Integer parentId) {
        InspiritDistribution inspiritDistribution = new InspiritDistribution();
        inspiritDistribution.setQuarterTableId(quarterTable.getId());
        inspiritDistribution.setDepartmentId(department.getId());
        inspiritDistribution.setP(department.getSign());
        inspiritDistribution.setParentId(parentId);
        inspiritDistribution = inspiritDistributionService.saveOne(inspiritDistribution);
        if (!CollectionUtils.isEmpty(department.getChildren())) {
            for (ITreeEntity<Integer> departmentChild : department.getChildren()) {
                this.saveInspiritDistributions(quarterTable, departmentChild, inspiritDistribution.getId());
            }
        }
    }
}
