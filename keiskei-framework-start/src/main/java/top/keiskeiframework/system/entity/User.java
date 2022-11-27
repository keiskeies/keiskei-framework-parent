package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
import top.keiskeiframework.common.annotation.dashboard.Chartable;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.IListEntity;
import top.keiskeiframework.common.base.entity.impl.ListEntityImpl;
import top.keiskeiframework.common.base.mp.annotation.MpManyToMany;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author cjm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_user")
@TableName(value = "sys_user")
@ApiModel(value = "User", description = "管理员")
public class User extends ListEntityImpl<Integer> implements IListEntity<Integer> {

    private static final long serialVersionUID = 7738707991477004429L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "账号", dataType = "String")
    @NotBlank(message = "账号不能为空", groups = {Insert.class})
    @Chartable
    private String username;

    @ApiModelProperty(value = "姓名", dataType = "String")
    @NotBlank(message = "姓名不能为空", groups = {Insert.class})
    private String name;

    @ApiModelProperty(value = "用户头像", dataType = "String")
    private String avatar;

    @ApiModelProperty(value = "用户手机号", dataType = "String")
    @NotBlank(message = "用户手机号不能为空", groups = {Insert.class})
    private String phone;

    @ApiModelProperty(value = "用户邮箱", dataType = "String")
    @NotBlank(message = "用户邮箱不能为空", groups = {Insert.class})
    private String email;

    @ApiModelProperty(value = "用户角色", dataType = "List<Role>")
    @MpManyToMany(middleClass = UserRole.class, targetClass = Role.class)
    @TableField(exist = false)
    private transient List<Role> roles;

    @ApiModelProperty(value = "用户组织", dataType = "List<Department>")
    @MpManyToMany(middleClass = UserDepartment.class, targetClass = Department.class)
    @TableField(exist = false)
    private transient List<Department> departments;

    @ApiModelProperty(value = "启用状态", dataType = "Boolean")
    @Column(columnDefinition = "tinyint(1) default 1")
    private Boolean enabled;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "锁定时间", dataType = "LocalDateTime")
    private LocalDateTime accountLockTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "账号过期时间", dataType = "LocalDateTime")
    private LocalDateTime accountExpiredTime;
}
