package top.keiskeiframework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.service.TestService;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/6 22:39
 */
@RestController
@RequestMapping("/admin/v1/test")
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id) {
        return testService.getById(id);
    }
}
