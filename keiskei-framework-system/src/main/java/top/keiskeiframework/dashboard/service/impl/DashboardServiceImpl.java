package top.keiskeiframework.dashboard.service.impl;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.enums.BizExceptionEnum;
import top.keiskeiframework.common.util.SpringUtils;
import top.keiskeiframework.dashboard.entity.Dashboard;
import top.keiskeiframework.dashboard.service.IDashboardService;

import javax.persistence.criteria.*;
import java.util.ArrayList;
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




}
