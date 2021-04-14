package top.keiskeiframework.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.ListEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/13 16:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_dashboard_direction")
@ApiModel(value = "DashboardDirection", description = "图表横坐标")
public class DashboardDirection extends ListEntity {

    @ApiModelProperty(value = "排序", dataType = "Long")
    @SortBy(desc = false)
    private Long sortBy;

    @ApiModelProperty(value = "字段", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String field;

    @ApiModelProperty(value = "字段名称", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String fieldName;

    @ApiModelProperty(value = "字段值", dataType = "String")
    private String fieldValue;

    @ApiModelProperty(value = "分段", dataType = "String")
    private Double delta;

    @ApiModelProperty(value = "时间结点", dataType = "String")
    private String type;

    @ApiModelProperty(value = "轴方向", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String direction;

}
