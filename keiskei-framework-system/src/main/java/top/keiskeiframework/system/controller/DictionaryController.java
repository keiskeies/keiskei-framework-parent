package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.TreeController;
import top.keiskeiframework.system.entity.Dictionary;

/**
 * <p>
 * 缓存管理 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@RestController
@RequestMapping("/admin/v1/system/dictionary")
@Api(tags = "系统设置 - 数据字典")
public class DictionaryController extends TreeController<Dictionary, Long> {




}
