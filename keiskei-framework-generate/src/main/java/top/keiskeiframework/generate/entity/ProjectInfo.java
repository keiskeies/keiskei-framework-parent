package top.keiskeiframework.generate.entity;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.generate.enums.ProjectInfoFileJarEnum;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;

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
@TableName(value = "gr_project_info")
@ApiModel(value = "ProjectInfo", description = "项目信息")
public class ProjectInfo extends ListEntity {

    private static final long serialVersionUID = 8549325611615861124L;

    @ApiModelProperty(value = "项目名称", dataType = "String")
    @NotBlank(message = "项目名称不能为空", groups = {Insert.class, Update.class})
    private String name;

    @ApiModelProperty(value = "项目注释", dataType = "String")
    @NotBlank(message = "项目注释不能为空", groups = {Insert.class, Update.class})
    private String comment;

    @ApiModelProperty(value = "项目版本", dataType = "String")
    @NotBlank(message = "项目版本不能为空", groups = {Insert.class, Update.class})
    @OrderBy
    private String version;

    @ApiModelProperty(value = "favicon", dataType = "String")
    @NotBlank(message = "请输入favicon", groups = {Insert.class, Update.class})
    private String favicon;

    @ApiModelProperty(value = "LOGO", dataType = "String")
    @NotBlank(message = "请输入LOGO", groups = {Insert.class, Update.class})
    private String logo;

    @ApiModelProperty(value = "作者", dataType = "String")
    private String author;

    @ApiModelProperty(value = "文件方式", dataType = "String")
    private ProjectInfoFileJarEnum fileJar = ProjectInfoFileJarEnum.LOCAL;

    @ApiModelProperty(value = "日志存储", dataType = "Boolean")
    private Boolean sqlLog = false;

    @ApiModelProperty(value = "构建工作流", dataType = "Boolean")
    private Boolean workflow = false;

    @OneToMany
    @JoinColumn(name = "projectId")
    @Valid
    private transient Collection<ModuleInfo> modules = new ArrayList<>();
}
