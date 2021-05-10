package top.keiskeiframework.dashboard.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class DashboardDirection extends BaseEntity {

    @ApiModelProperty(value = "字段", dataType = "String")
    private String field;

    @ApiModelProperty(value = "实体类", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String entityClass;

    @ApiModelProperty(value = "实体类名称", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String entityName;



    @ApiModelProperty(value = "图表类型", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String type;


}
