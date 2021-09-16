package top.keiskeiframework.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Accessors;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 定时任务
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/15 18:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Accessors(chain = true)
@Table(name = "sys_scheduled_task")
@ApiModel(value = "ScheduledTask", description = "定时任务")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class ScheduledTask extends BaseEntity<Long> {

    private static final long serialVersionUID = -2520015535285512159L;

    @ApiModelProperty(value = "任务名称", dataType = "String")
    @NotBlank(message = "任务名称不能为空", groups = {Insert.class, Update.class})
    private String name;

    @ApiModelProperty(value = "表达式", dataType = "String")
    @NotBlank(message = "表达式不能为空", groups = {Insert.class, Update.class})
    private String cron;

    @ApiModelProperty(value = "任务KEY", dataType = "String")
    @NotBlank(message = "请输入任务KEY", groups = {Insert.class, Update.class})
    private String cronKey;


    @ApiModelProperty(value = "任务参数", dataType = "String")
    private String param;

    @ApiModelProperty(value = "任务描述", dataType = "String")
    private String description;

    @ApiModelProperty(value = "是否启用", dataType = "String")
    private Boolean enable = true;

}
