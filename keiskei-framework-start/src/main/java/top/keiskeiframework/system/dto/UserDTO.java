package top.keiskeiframework.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.keiskeiframework.system.vo.TokenDepartment;
import top.keiskeiframework.system.vo.TokenGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * <p>
 * 用户信息DTO
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 16:39
 */
@Data
@ApiModel(value = "UserDto", description = "用户信息")
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -3480246235052237482L;
    private Integer id;

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

    @ApiModelProperty(value = "权限", dataType = "Set<String>")
    private Set<String> permissions;

    @ApiModelProperty(value = "用户角色", dataType = "Collection<TokenGrantedAuthority>")
    private Collection<TokenGrantedAuthority> authorities;

    @ApiModelProperty(value = "用户组织", dataType = "Collection<TokenDepartment>")
    private Collection<TokenDepartment> departments;

    @ApiModelProperty(value = "用户设置", dataType = "UserSettingDTO")
    private UserSettingDTO setting;
}
