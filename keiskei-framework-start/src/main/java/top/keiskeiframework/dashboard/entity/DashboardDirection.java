package top.keiskeiframework.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.impl.ListEntityImpl;
import top.keiskeiframework.common.base.mp.annotation.MpOneToMany;
import top.keiskeiframework.common.enums.dashboard.ChartType;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * <p>
 * 图表坐标
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/13 16:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "dashboard_direction")
@TableName(value = "dashboard_direction")
@ApiModel(value = "DashboardDirection", description = "图表横坐标")
public class DashboardDirection extends ListEntityImpl<Integer> {

    private static final long serialVersionUID = -2719449560787668928L;

    private Integer dashboardId;

    @ApiModelProperty(value = "字段", dataType = "String")
    private String field;

    @ApiModelProperty(value = "实体类", dataType = "String")
    @NotBlank(message = "实体类不能为空", groups = {Insert.class, Update.class})
    private String entityClass;

    @ApiModelProperty(value = "显示名称", dataType = "String")
    @NotBlank(message = "显示名称不能为空", groups = {Insert.class, Update.class})
    private String entityName;

    @ApiModelProperty(value = "查询条件")
    @MpOneToMany(filedName = "dashboardDirectionId", targetClass = DashboardDirectionCondition.class)
    private transient Collection<DashboardDirectionCondition> conditions;

    @ApiModelProperty(value = "图表类型", dataType = "String")
    @NotNull(message = "图表类型不能为空", groups = {Insert.class, Update.class})
    private ChartType type;
}
