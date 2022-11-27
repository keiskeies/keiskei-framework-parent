package top.keiskeiframework.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import top.keiskeiframework.common.util.VerificationCodeUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/11/20 03:15
 */
@Controller
@RequestMapping("/system/common")
public class SystemCommonController {


    @GetMapping("/verifyCode/{uuid}.jpg")
    public void verifyCode(@PathVariable String uuid, HttpServletResponse response) throws IOException {
        VerificationCodeUtil.generateVerificationCode(response, uuid);
    }

}
