package top.keiskeiframework.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.keiskeiframework.common.vo.TokenGrantedAuthority;
import top.keiskeiframework.system.entity.Permission;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 用户信息DTO
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 16:39
 */
@Data
public class UserDto {

    private Long id;

    @ApiModelProperty(value = "登录用户名", dataType = "String")
    private String username;

    @ApiModelProperty(value = "真实姓名", dataType = "String")
    private String name;

    @ApiModelProperty(value = "头像", dataType = "String")
    private String avatar;

    @ApiModelProperty(value = "手机号", dataType = "String")
    private String phone;

    @ApiModelProperty(value = "邮箱", dataType = "String")
    private String email;

    @ApiModelProperty(value = "权限", dataType = "List")
    private List<String> permissions;

    @ApiModelProperty(value = "用户角色", dataType = "List")
    private Collection<TokenGrantedAuthority> authorities;
}
