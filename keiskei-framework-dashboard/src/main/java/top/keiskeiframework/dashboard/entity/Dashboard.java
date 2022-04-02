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
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.enums.dashboard.ChartType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;
import top.keiskeiframework.common.enums.timer.TimeTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 图表
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
@Table(name = "dashboard")
@ApiModel(value = "Dashboard", description = "图表")
public class Dashboard extends ListEntity<Long> {

    private static final long serialVersionUID = -5855392143364324899L;

    @ApiModelProperty(value = "排序", dataType = "Long")
    @SortBy(desc = false)
    private Long sortBy;

    @PostPersist
    private void postPersist() {
        this.sortBy = super.getId();
    }

    @ApiModelProperty(value = "图表名称", dataType = "String")
    @NotBlank(message = "图表名称不能为空", groups = {Insert.class, Update.class})
    private String name;

    @ApiModelProperty(value = "时间类型", dataType = "Integer")
    @NotNull(message = "时间类型不能为空", groups = {Insert.class, Update.class})
    private TimeTypeEnum timeType;

    @ApiModelProperty(value = "时间起点", dataType = "String")
    private String start;

    @ApiModelProperty(value = "时间结点", dataType = "String")
    private String end;

    @ApiModelProperty(value = "图表主类型", dataType = "String")
    @NotNull(message = "图表主类型不能为空", groups = {Insert.class, Update.class})
    private ChartType type;

    @ApiModelProperty(value = "图表宽度", dataType = "Integer")
    private Integer span = 1;

    @ApiModelProperty(value = "xy坐标翻转", dataType = "Boolean")
    private Boolean horizontal = Boolean.FALSE;

    @ApiModelProperty(value = "x坐标名称", dataType = "String")
    private String fieldName;

    @ApiModelProperty(value = "创建时间字段", dataType = "String")
    private String timeField;

    @ApiModelProperty(value = "x坐标类型", dataType = "Integer")
    @NotNull(message = "x坐标类型不能为空", groups = {Insert.class, Update.class})
    private ColumnType fieldType;

    @ApiModelProperty(value = "时间类型间隔", dataType = "String")
    private TimeDeltaEnum fieldDelta;

    @ApiModelProperty(value = "y坐标", dataType = "List<DashboardDirection>")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dashboard_id")
    @NotEmpty(message = "y坐标不能为空", groups = {Insert.class, Update.class})
    private List<DashboardDirection> directions;
}
