package top.keiskeiframework.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.base.entity.TreeEntity;

import javax.persistence.Entity;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 数据字典
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
@Table(name = "sys_dictionary")
@ApiModel(value = "Dictionary", description = "数据字段")
public class Dictionary extends TreeEntity<Long> {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "资源名称不能为空", groups = {Insert.class})
    @ApiModelProperty(value = "资源名称", dataType = "String")
    private String name;

    @NotBlank(message = "资源类型不能为空", groups = {Insert.class})
    @ApiModelProperty(value = "资源类型", dataType = "String")
    private String code;

    @ApiModelProperty(value = "颜色类型", dataType = "String")
    @NotBlank(message = "颜色类型不能为空", groups = {Insert.class})
    private String type;

    @ApiModelProperty(value = "主题", dataType = "String")
    @NotBlank(message = "主题不能为空", groups = {Insert.class})
    private String effect;

    @ApiModelProperty(value = "排序", dataType = "Long")
    @SortBy(desc = false)
    private Long sortBy;

    @PreUpdate
    public void onUpdate() {
        sortBy = Long.parseLong(id.toString());
    }

}
