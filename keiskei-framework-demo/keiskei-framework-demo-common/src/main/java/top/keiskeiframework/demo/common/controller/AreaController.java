package top.keiskeiframework.demo.common.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.TreeControllerImpl;
import top.keiskeiframework.demo.common.entity.Area;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/25 18:30
 */
@RestController
@RequestMapping("/demo/common/area")
public class AreaController extends TreeControllerImpl<Area, Long> {
}
