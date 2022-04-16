package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.system.dto.SystemUserDto;
import top.keiskeiframework.system.dto.SystemUserPasswordDto;
import top.keiskeiframework.system.entity.SystemUser;
import top.keiskeiframework.system.service.ISystemUserService;
import top.keiskeiframework.system.util.SecurityUtils;
import top.keiskeiframework.system.vo.TokenUser;

/**
 * <p>
 * 个人中心 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@RestController
@RequestMapping("/system/self")
@Api(tags = "系统设置-个人中心", hidden = true)
public class SelfController {

    @Autowired
    private ISystemUserService userService;

    @GetMapping
    public R<SystemUserDto> getSelfInfo() {
        return R.ok(userService.getSelfInfo());
    }

    @PutMapping
    public R<SystemUserDto> update(@RequestBody SystemUserDto systemUserDto) {
        TokenUser tokenUser = SecurityUtils.getSessionUser();
        SystemUser systemUser = userService.getById(tokenUser.getId());
        BeanUtils.copyPropertiesIgnoreNull(systemUserDto, systemUser);
        userService.updateByIdAndNotify(systemUser);
        return R.ok(systemUserDto);
    }

    @PatchMapping
    public R<Boolean> update(@RequestBody @Validated SystemUserPasswordDto systemUserPasswordDto) {
        TokenUser tokenUser = SecurityUtils.getSessionUser();
        SystemUser systemUser = userService.getById(tokenUser.getId());
        Assert.isTrue(systemUserPasswordDto.match(systemUser.getPassword()), BizExceptionEnum.AUTH_PASSWORD_ERROR.getMsg());

        systemUser.setPassword(systemUserPasswordDto.getNewPassword());
        userService.updateByIdAndNotify(systemUser);
        return R.ok(Boolean.TRUE);
    }


}
