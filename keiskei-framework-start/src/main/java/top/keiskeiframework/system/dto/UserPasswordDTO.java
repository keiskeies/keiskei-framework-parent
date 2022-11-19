package top.keiskeiframework.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.keiskeiframework.system.util.SecurityUtils;

import javax.annotation.MatchesPattern;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 用户信息DTO
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 16:39
 */
@Data
@ApiModel(value = "SystemUserPasswordDto", description = "用户密码相关")
public class UserPasswordDTO implements Serializable {


    private static final long serialVersionUID = -4765310835063712994L;

    @ApiModelProperty(value = "原始密码", dataType = "String")
    @NotBlank(message = "原始密码不能为空")
    private String oldPassword;

    @ApiModelProperty(value = "新密码", dataType = "String")
    @NotBlank(message = "新密码不能为空")
    @MatchesPattern(value = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[._~!@#$^&*])[A-Za-z0-9._~!@#$^&*]{8,20}$")
    private String newPassword;

    /**
     * 旧密码验证
     *
     * @param encodesPassword .
     * @return .
     */
    public boolean matchOld(String encodesPassword) {
        return SecurityUtils.matchPassword(oldPassword, encodesPassword);
    }

    /**
     * 新密码验证
     *
     * @param encodesPassword 。
     * @return 。
     */
    public boolean matchNew(String encodesPassword) {
        return SecurityUtils.matchPassword(newPassword, encodesPassword);
    }

    public String getNewPassword() {
        return SecurityUtils.encodePassword(oldPassword);
    }

}
