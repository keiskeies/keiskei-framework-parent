package top.keiskeiframework.cpreading.common.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.cpreading.common.entity.Area;
import top.keiskeiframework.cpreading.common.service.IAreaService;

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
