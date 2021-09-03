package top.keiskeiframework.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.entity.TestInfo;
import top.keiskeiframework.entity.TestResult;
import top.keiskeiframework.service.TestInfoService;
import top.keiskeiframework.service.TestResultService;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/3 00:56
 */
@Service
public class TestResultServiceImpl extends ListServiceImpl<TestResult, Long> implements TestResultService {
}
