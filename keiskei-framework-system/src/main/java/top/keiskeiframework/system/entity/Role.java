package top.keiskeiframework.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.ListEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 角色信息
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019-06-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_role")
public class Role extends ListEntity {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "角色名称", dataType = "String")
    @NotBlank(message = "角色名称不能为空", groups = {Insert.class, Update.class})
    private String name;

    @ManyToMany(targetEntity = Permission.class, cascade = CascadeType.MERGE)
    @JoinTable(name = "sys_role_permission",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
    private Set<Permission> permissions = new HashSet<>();
    public Set<Permission> getPermissions() { return new HashSet<>(permissions); }
}
