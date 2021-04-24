package top.keiskeiframework.system.service.impl;

import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.keiskeiframework.common.base.BaseRequest;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.enums.BizExceptionEnum;
import top.keiskeiframework.common.util.SpringUtils;
import top.keiskeiframework.system.entity.Dashboard;
import top.keiskeiframework.system.service.IDashboardService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
@Slf4j
public class DashboardServiceImpl extends ListServiceImpl<Dashboard> implements IDashboardService {

    public Map<String, List<Object>> data(Long id) throws Exception {
        Dashboard dashboard = getById(id);
        Assert.notNull(dashboard, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
        BaseService<?> baseService = (BaseService<?>)SpringUtils.getBean(dashboard.getEntityName() + "ServiceImpl");
        BaseRequest<?> baseRequest = new BaseRequest<>();

        Class<?> entityClazz = Class.forName(dashboard.getEntityName());
        Specification<?> specification = new Specification<Object>() {
            @Override
            public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return ;
            }
        }

        List<?> dataList = baseService.findAll();

        return null;

    }


}
