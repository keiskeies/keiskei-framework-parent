package top.keiskeiframework.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.BaseEntity;

import javax.validation.constraints.NotBlank;
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
@Document("sys_role")
@ApiModel(value = "Role", description = "角色")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "角色名称", dataType = "String")
    @NotBlank(message = "角色名称不能为空", groups = {Insert.class, Update.class})
    private String name;

    @DBRef
    private Set<Permission> permissions;
}
