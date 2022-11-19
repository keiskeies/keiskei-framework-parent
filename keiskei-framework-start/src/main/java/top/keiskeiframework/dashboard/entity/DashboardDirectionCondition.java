package top.keiskeiframework.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.impl.ListEntityImpl;
import top.keiskeiframework.common.util.data.TagDeserializer;
import top.keiskeiframework.common.util.data.TagSerializer;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 图表横坐标查询条件
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/10/10 01:09
 */
@Data
@Entity
@Table(name = "dashboard_direction_condition")
@TableName(value = "dashboard_direction_condition")
@ApiModel(value = "DashboardDirectionCondition", description = "图表横坐标查询条件")
public class DashboardDirectionCondition extends ListEntityImpl<Integer> {

    private static final long serialVersionUID = -2434119392535767822L;

    private Integer dashboardDirectionId;

    @ApiModelProperty(value = "字段", dataType = "String")
    @NotBlank(message = "字段不能为空", groups = {Insert.class, Update.class})
    private String field;

    @ApiModelProperty(value = "范围", dataType = "Array")
    @JsonDeserialize(converter = TagDeserializer.class)
    @JsonSerialize(converter = TagSerializer.class)
    @NotBlank(message = "范围不能为空", groups = {Insert.class, Update.class})
    private String rangeValue;
}
