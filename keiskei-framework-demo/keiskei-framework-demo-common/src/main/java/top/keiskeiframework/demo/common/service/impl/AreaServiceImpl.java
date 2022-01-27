package top.keiskeiframework.demo.common.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.demo.common.entity.Area;
import top.keiskeiframework.demo.common.service.IAreaService;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/25 18:41
 */
@Service
public class AreaServiceImpl extends TreeServiceImpl<Area, Long> implements IAreaService {
}
