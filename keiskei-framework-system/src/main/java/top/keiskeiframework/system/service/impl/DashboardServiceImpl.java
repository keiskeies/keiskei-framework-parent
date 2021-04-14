package top.keiskeiframework.system.service.impl;

import io.jsonwebtoken.lang.Assert;
import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.enums.BizExceptionEnum;
import top.keiskeiframework.system.entity.Dashboard;
import top.keiskeiframework.system.service.IDashboardService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图表
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/13 18:40
 */
@Service
public class DashboardServiceImpl extends ListServiceImpl<Dashboard> implements IDashboardService {

    public Map<String, List<Object>> data(Long id) {
        Dashboard dashboard = getById(id);
        Assert.notNull(dashboard, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
//        Class<?> clazz = Class.forName(dashboard.getEntityName());
        return null;

    }
}
