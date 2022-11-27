package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.IListEntity;
import top.keiskeiframework.common.base.entity.impl.ListEntityImpl;

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
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_scheduled_task")
@TableName(value = "sys_scheduled_task")
@ApiModel(value = "ScheduledTask", description = "定时任务")
public class ScheduledTask extends ListEntityImpl<Integer> implements IListEntity<Integer> {

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

    @ApiModelProperty(value = "启用状态", dataType = "Boolean")
    private Boolean enabled;

}
