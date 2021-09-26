package top.keiskeiframework.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户信息DTO
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 16:39
 */
@ApiModel(value = "UserPasswordDto", description = "用户密码相关")
public class UserPasswordDto {

    private static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();


    @Setter
    @ApiModelProperty(value = "原始密码", dataType = "String")
    @NotBlank(message = "原始密码不能为空")
    private String oldPassword;

    @Setter
    @ApiModelProperty(value = "新密码", dataType = "String")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    /**
     * 密码验证
     * @param encodesPassword .
     * @return .
     */
    public boolean match(String encodesPassword) {
        return B_CRYPT_PASSWORD_ENCODER.matches(oldPassword, encodesPassword);
    }

    public String getNewPassword() {
        return B_CRYPT_PASSWORD_ENCODER.encode(newPassword);
    }

}
