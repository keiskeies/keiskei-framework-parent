package top.keiskeiframework.demo.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.*;

import top.keiskeiframework.demo.entity.Demo;
import top.keiskeiframework.demo.service.IDemoService;

/**
 * <p>
 * 表单测试 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-18 14:23:37
 */
@RestController
@RequestMapping("/admin/v1/demo/demo")
@Api(tags = "测试 - 表单测试")
public class DemoController extends ListController<Demo, Long>{

    @Autowired
    private IDemoService demoService;



}
