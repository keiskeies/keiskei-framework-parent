package top.keiskeiframework.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
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
@AllArgsConstructor
@Entity
@Table(name = "sys_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = -3821316560303369479L;

    @ApiModelProperty(value = "账号", dataType = "String")
    @NotBlank(message = "账号不能为空", groups = {Insert.class})
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
    @NotBlank(message = "请上传用户头像", groups = {Insert.class})
    private String avatar;

    @ApiModelProperty(value = "用户手机号", dataType = "String")
    @NotBlank(message = "用户手机号不能为空", groups = {Insert.class})
    private String phone;

    @ApiModelProperty(value = "用户邮箱", dataType = "String")
    @NotBlank(message = "用户邮箱不能为空", groups = {Insert.class})
    private String email;

    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.MERGE)
    @JoinTable(name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles() {
        return new HashSet<>(roles);
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

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
    private Long passwordErrorTimes;

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
