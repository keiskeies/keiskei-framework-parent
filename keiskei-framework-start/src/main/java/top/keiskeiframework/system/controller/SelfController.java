package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.system.dto.UserDTO;
import top.keiskeiframework.system.dto.UserDepartmentDTO;
import top.keiskeiframework.system.dto.UserPasswordDTO;
import top.keiskeiframework.system.entity.User;
import top.keiskeiframework.system.entity.UserSetting;
import top.keiskeiframework.system.service.IUserPasswordService;
import top.keiskeiframework.system.service.IUserService;
import top.keiskeiframework.system.service.IUserSettingService;
import top.keiskeiframework.system.util.SecurityUtils;
import top.keiskeiframework.system.vo.TokenUser;

import javax.servlet.http.HttpServletRequest;

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
    private IUserService userService;
    @Autowired
    private IUserPasswordService userPasswordService;
    @Autowired
    private IUserSettingService userSettingService;

    @GetMapping
    public R<UserDTO> getSelfInfo() {
        return R.ok(userService.getSelfInfo());
    }

    @PutMapping
    public R<UserDTO> update(@RequestBody UserDTO userDto) {
        TokenUser tokenUser = SecurityUtils.getSessionUser();
        User systemUser = userService.findOneById(tokenUser.getId());
        BeanUtils.copyPropertiesIgnoreNull(userDto, systemUser);
        systemUser.setUsername(null);
        userService.updateOne(systemUser);
        return R.ok(userDto);
    }

    @PatchMapping
    public R<Boolean> updatePassword(@RequestBody @Validated UserPasswordDTO userPassword, HttpServletRequest request) {
        TokenUser tokenUser = SecurityUtils.getSessionUser();
        userPasswordService.updatePassword(tokenUser.getId(), userPassword);
        SecurityUtils.removeSession(request);
        return R.ok(Boolean.TRUE);
    }

    @PutMapping("/department")
    public R<Boolean> updateDepartment(@RequestBody @Validated UserDepartmentDTO department, HttpServletRequest request) {
        return null;
    }

    @GetMapping("/setting")
    public R<UserSetting> getSetting() {
        TokenUser tokenUser = SecurityUtils.getSessionUser();
        return R.ok(userSettingService.findOneById(tokenUser.getId()));
    }

    @PutMapping("/setting")
    public R<Boolean> updateSetting(@RequestBody @Validated UserSetting userSetting) {
        TokenUser tokenUser = SecurityUtils.getSessionUser();
        userSetting.setId(tokenUser.getId());
        userSettingService.updateOne(userSetting);
        return R.ok(Boolean.TRUE);
    }

}
