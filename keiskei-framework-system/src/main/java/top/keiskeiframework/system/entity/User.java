package top.keiskeiframework.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.lombok.Chartable;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
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
@Document("sys_user")
@ApiModel(value = "User", description = "管理员")
@Chartable
public class User extends BaseEntity {

    private static final long serialVersionUID = -3821316560303369479L;

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
    @DBRef
    private Set<Role> roles;

    @ApiModelProperty(value = "用户部门", dataType = "Department")
    @DBRef
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
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime accountExpiredTime;


    public static void main(String[] args) {

    }

}
