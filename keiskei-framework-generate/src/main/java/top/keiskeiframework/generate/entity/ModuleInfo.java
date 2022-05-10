package top.keiskeiframework.generate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableId;
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

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 * 模块信息
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
@TableName(value = "gr_module_info")
@ApiModel(value = "ModuleInfo", description = "模块信息")
public class ModuleInfo extends ListEntity<Integer> {

    private static final long serialVersionUID = 754302484437506602L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "模块名称", dataType = "String")
    @NotBlank(message = "模块名称不能为空", groups = {Insert.class, Update.class})
    private String name;

    @ApiModelProperty(value = "模块路径", dataType = "String")
    @NotBlank(message = "模块路径不能为空", groups = {Insert.class, Update.class})
    private String path;

    @ApiModelProperty(value = "模块注释", dataType = "String")
    @NotBlank(message = "模块注释不能为空", groups = {Insert.class, Update.class})
    private String comment;

    @ApiModelProperty(value = "模块包名", dataType = "String")
    @NotBlank(message = "模块包名不能为空", groups = {Insert.class, Update.class})
    private String packageName;

    @OneToMany
    @JoinColumn(name = "module_id")
    @Valid
    private transient Collection<TableInfo> tables = new ArrayList<>();

    private Integer projectId;

    @ApiModelProperty(value = "排序", dataType = "Integer")
    @OrderBy
    private Integer sortBy;

}
