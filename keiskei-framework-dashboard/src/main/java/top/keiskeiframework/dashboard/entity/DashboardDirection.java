package top.keiskeiframework.dashboard.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.enums.dashboard.ChartType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 图表坐标
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
@Document("dashboard_direction")
@ApiModel(value = "DashboardDirection", description = "图表横坐标")
public class DashboardDirection extends ListEntity {

    private static final long serialVersionUID = -2719449560787668928L;

    @ApiModelProperty(value = "字段", dataType = "String")
    private String field;

    @ApiModelProperty(value = "实体类", dataType = "String")
    @NotBlank(message = "实体类不能为空", groups = {Insert.class, Update.class})
    private String entityClass;

    @ApiModelProperty(value = "显示名称", dataType = "String")
    @NotBlank(message = "实体类名称不能为空", groups = {Insert.class, Update.class})
    private String entityName;

    @ApiModelProperty(value = "查询条件")
    private List<DashboardDirectionCondition> conditions;

    @ApiModelProperty(value = "图表类型", dataType = "String")
    @NotNull(message = "图表类型不能为空", groups = {Insert.class, Update.class})
    private ChartType type;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardDirectionCondition {

        @ApiModelProperty(value = "条件字段", dataType = "String")
        private String field;

        @ApiModelProperty(value = "字段范围")
        private List<String> rangeValue;
    }
}
