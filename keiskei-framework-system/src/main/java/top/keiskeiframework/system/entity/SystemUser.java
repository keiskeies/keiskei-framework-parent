package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.dashboard.Chartable;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.ListEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 管理员
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-10-16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@TableName(value = "sys_user")
@ApiModel(value = "SystemUser", description = "管理员")
public class SystemUser extends ListEntity<Integer>  {

    private static final long serialVersionUID = -3821316560303369479L;


    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "账号", dataType = "String")
    @NotBlank(message = "账号不能为空", groups = {Insert.class})
    @Chartable
    private String username;

    @ApiModelProperty(value = "密码", dataType = "String")
    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "姓名", dataType = "String")
    @NotBlank(message = "姓名不能为空", groups = {Insert.class})
    private String name;

    @ApiModelProperty(value = "启用状态", dataType = "Boolean")
    private Boolean enabled = true;

    @ApiModelProperty(value = "用户头像", dataType = "String")
    private String avatar;

    @ApiModelProperty(value = "用户手机号", dataType = "String")
    @NotBlank(message = "用户手机号不能为空", groups = {Insert.class})
    private String phone;

    @ApiModelProperty(value = "用户邮箱", dataType = "String")
    @NotBlank(message = "用户邮箱不能为空", groups = {Insert.class})
    private String email;

    @ApiModelProperty(value = "用户角色", dataType = "Set<Role>")
    @ManyToMany
    @JoinTable(name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "roleId", referencedColumnName = "id")})
    private transient Collection<SystemRole> systemRoles;

    @ApiModelProperty(value = "用户部门", dataType = "Department")
    @ManyToOne
    @JoinColumn(name = "departmentId")
    private transient SystemDepartment systemDepartment;

    private Integer departmentId;

    /**
     * 最后登录时间
     */
    @JsonIgnore
    private LocalDateTime lastLoginTime;

    /**
     * 最后修改密码时间
     */
    @JsonIgnore
    private LocalDateTime lastModifyPasswordTime;

    /**
     * 密码错误次数
     */
    @JsonIgnore
    private Integer passwordErrorTimes;

    /**
     * 锁定时间
     */
    @JsonIgnore
    private LocalDateTime accountLockTime;

    /**
     * 账号过期时间
     */
    @JsonIgnore
    private LocalDateTime accountExpiredTime;

}
