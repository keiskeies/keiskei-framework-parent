package top.keiskeiframework.dashboard.entity;

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
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

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
@Table(name = "sys_dashboard")
@ApiModel(value = "Dashboard", description = "图表")
public class Dashboard extends BaseEntity {

    private static final long serialVersionUID = -5855392143364324899L;

    @ApiModelProperty(value = "排序", dataType = "Long")
    @SortBy(desc = false)
    private Long sortBy;

    @ApiModelProperty(value = "图表名称", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String name;

    @ApiModelProperty(value = "时间起点", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String start;

    @ApiModelProperty(value = "时间结点", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String end;

    @ApiModelProperty(value = "图表宽度", dataType = "Integer")
    private Integer span = 1;

    @ApiModelProperty(value = "xy坐标翻转", dataType = "Integer")
    private Boolean yHorizontal = Boolean.FALSE;

    @ApiModelProperty(value = "x坐标字段", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String xField;

    @ApiModelProperty(value = "x坐标名称", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String xFieldName;

    @ApiModelProperty(value = "x坐标类型", dataType = "Integer")
    @NotBlank(groups = {Insert.class, Update.class})
    private ChartRequestDTO.ColumnType xFieldType;

    @ApiModelProperty(value = "时间类型间隔", dataType = "String")
    private String xFieldDelta;

    @ApiModelProperty(value = "y坐标", dataType = "String")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dashboard_id")
    @NotEmpty(groups = {Insert.class, Update.class})
    private List<DashboardDirection> yFields;


}
