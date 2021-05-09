package top.keiskeiframework.generate.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 项目信息
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gr_project_info")
@ApiModel(value = "project_info", description = "项目信息")
public class ProjectInfo extends BaseEntity {

    private static final long serialVersionUID = 8549325611615861124L;

    @ApiModelProperty(value = "项目名称", dataType = "String")
    @NotBlank(message = "项目名称不能为空", groups = {Insert.class})
    private String name;

    @ApiModelProperty(value = "项目注释", dataType = "String")
    @NotBlank(message = "项目注释不能为空", groups = {Insert.class})
    private String comment;

    @ApiModelProperty(value = "项目版本", dataType = "String")
    @NotBlank(message = "项目版本不能为空", groups = {Insert.class})
    private String version;

    @ApiModelProperty(value = "favicon", dataType = "String")
    @NotBlank(message = "请输入favicon", groups = {Insert.class})
    private String favicon;

    @ApiModelProperty(value = "LOGO", dataType = "String")
    @NotBlank(message = "请输入LOGO", groups = {Insert.class})
    private String logo;

    @ApiModelProperty(value = "作者", dataType = "String")
    private String author;

    @Transient
    private List<ModuleInfo> modules;

}