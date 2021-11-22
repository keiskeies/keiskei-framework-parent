package top.keiskeiframework.generate.entity;

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
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.generate.enums.TableInfoControllerTypeEnum;
import top.keiskeiframework.generate.enums.TableInfoIdTypeEnum;
import top.keiskeiframework.generate.enums.TableInfoTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 表结构信息
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
@Document("gr_table_info")
@ApiModel(value = "TableInfo", description = "表结构信息")
public class TableInfo extends ListEntity {

    private static final long serialVersionUID = 7715195221883078519L;

    @ApiModelProperty(value = "实体类名", dataType = "String")
    @NotBlank(message = "实体类名不能为空", groups = {Insert.class})
    private String name;

    @ApiModelProperty(value = "表注释", dataType = "String")
    @NotBlank(message = "表注释不能为空", groups = {Insert.class})
    private String comment;

    @ApiModelProperty(value = "主键类型", dataType = "String")
    private TableInfoIdTypeEnum idType = TableInfoIdTypeEnum.LONG;

    @ApiModelProperty(value = "表名称", dataType = "String")
    @NotBlank(message = "表名称不能为空", groups = {Insert.class})
    private String tableName;

    @ApiModelProperty(value = "表类型", dataType = "String")
    @NotNull(message = "表类型不能为空", groups = {Insert.class, Update.class})
    private TableInfoTypeEnum type;

    @ApiModelProperty(value = "是否构建接口", dataType = "Boolean")
    private Boolean buildController = Boolean.FALSE;

    @ApiModelProperty(value = "接口类型", dataType = "String")
    private TableInfoControllerTypeEnum controllerType;

    @DBRef(lazy = true)
    private List<FieldInfo> fields;

    @ApiModelProperty(value = "排序", dataType = "Integer")
    private Integer sortBy;
}
