package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.enums.BizExceptionEnum;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.common.vo.TokenUser;
import top.keiskeiframework.system.dto.UserDto;
import top.keiskeiframework.system.dto.UserPasswordDto;
import top.keiskeiframework.system.entity.User;
import top.keiskeiframework.system.service.IUserService;
import top.keiskeiframework.common.util.SecurityUtils;

/**
 * <p>
 * 个人中心 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@RestController
@RequestMapping("/admin/{version}/system/self")
@Api(tags = "系统设置 - 个人中心")
public class SelfController {

    @Autowired
    private IUserService userService;

    @GetMapping
    @ApiOperation("详情")
    public R<UserDto> getSelfInfo() {
        return R.ok(userService.getSelfInfo());
    }

    @PutMapping
    @ApiOperation("修改")
    public R<UserDto> update(@RequestBody UserDto userDto) {
        TokenUser tokenUser = SecurityUtils.getSessionUser();
        User user = new User();
        BeanUtils.copyPropertiesIgnoreNull(userDto, user);
        user.setId(tokenUser.getId());
        userService.update(user);
        return R.ok(userDto);
    }

    @PatchMapping
    @ApiOperation("修改密码")
    public R<Boolean> update(@RequestBody @Validated UserPasswordDto userPasswordDto) {
        TokenUser tokenUser = SecurityUtils.getSessionUser();
        User user = userService.getById(tokenUser.getId());
        Assert.isTrue(userPasswordDto.match(user.getPassword()), BizExceptionEnum.AUTH_PASSWORD_ERROR.getMsg());

        user.setPassword(userPasswordDto.getNewPassword());
        userService.update(user);
        return R.ok(Boolean.TRUE);
    }



}
