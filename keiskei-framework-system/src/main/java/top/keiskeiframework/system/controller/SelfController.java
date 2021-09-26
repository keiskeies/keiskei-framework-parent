package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.system.vo.TokenUser;
import top.keiskeiframework.system.dto.UserDto;
import top.keiskeiframework.system.dto.UserPasswordDto;
import top.keiskeiframework.system.entity.User;
import top.keiskeiframework.system.service.IUserService;
import top.keiskeiframework.system.util.SecurityUtils;

/**
 * <p>
 * 个人中心 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@RestController
@RequestMapping("/admin/v1/system/self")
@Api(tags = "系统设置-个人中心", hidden = true)
public class SelfController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public R<UserDto> getSelfInfo() {
        return R.ok(userService.getSelfInfo());
    }

    @PutMapping
    public R<UserDto> update(@RequestBody UserDto userDto) {
        TokenUser tokenUser = SecurityUtils.getSessionUser();
        User user = userService.getById(tokenUser.getId());
        BeanUtils.copyPropertiesIgnoreNull(userDto, user);
        userService.update(user);
        return R.ok(userDto);
    }

    @PatchMapping
    public R<Boolean> update(@RequestBody @Validated UserPasswordDto userPasswordDto) {
        TokenUser tokenUser = SecurityUtils.getSessionUser();
        User user = userService.getById(tokenUser.getId());
        Assert.isTrue(userPasswordDto.match(user.getPassword()), BizExceptionEnum.AUTH_PASSWORD_ERROR.getMsg());

        user.setPassword(userPasswordDto.getNewPassword());
        userService.update(user);
        return R.ok(Boolean.TRUE);
    }


}
