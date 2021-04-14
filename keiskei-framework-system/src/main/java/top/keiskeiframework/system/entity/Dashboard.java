package top.keiskeiframework.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.ListEntity;

import javax.persistence.*;
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
@Table(name = "sys_dashboard")
@ApiModel(value = "Dashboard", description = "图表")
public class Dashboard extends ListEntity {

    @ApiModelProperty(value = "排序", dataType = "Long")
    @SortBy(desc = false)
    private Long sortBy;

    @ApiModelProperty(value = "图表名称", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String name;

    @ApiModelProperty(value = "图表类型", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String type;

    @ApiModelProperty(value = "实体名称", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String entityName;

    @ApiModelProperty(value = "时间起点", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String start;

    @ApiModelProperty(value = "时间结点", dataType = "String")
    @NotBlank(groups = {Insert.class, Update.class})
    private String end;

    @ApiModelProperty(value = "图表宽度", dataType = "Integer")
    private Integer width;

    @ApiModelProperty(value = "图表高度", dataType = "Integer")
    private Integer height;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dashboard_id")
    @OrderBy("sortBy")
    @Where(clause = "direction = 'x'")
    private List<DashboardDirection> xs;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dashboard_id")
    @OrderBy("sortBy")
    @Where(clause = "direction = 'y'")
    private List<DashboardDirection> ys;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dashboard_id")
    @OrderBy("sortBy")
    @Where(clause = "direction = 'z'")
    private List<DashboardDirection> zs;


    @Transient
    public Map<String, String> entityInfo;


    public Map<String, String> getEntityInfo() {
        if (!StringUtils.isEmpty(getEntityName())) {
            try {
                Class<?> clazz = Class.forName(entityName);
                Field[] fields = clazz.getDeclaredFields();
                this.entityInfo = new HashMap<>(fields.length);
                for (Field field : fields) {
                    if (DATA_CLASS_SET.contains(field.getType())) {
                        System.out.println(field.getType().getName());
                        ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                        if (null != apiModelProperty) {
                            this.entityInfo.put(field.getName(), apiModelProperty.value());
                        } else {
                            this.entityInfo.put(field.getName(), field.getName());
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return entityInfo;
    }

    private final static List<Class<?>> DATA_CLASS_SET = Arrays.asList(
            java.lang.Integer.class,
            java.lang.Long.class,
            java.lang.Double.class,
            java.lang.String.class,
            java.lang.Float.class
    );
}
