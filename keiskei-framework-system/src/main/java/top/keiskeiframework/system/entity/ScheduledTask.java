package top.keiskeiframework.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.ListEntity;

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
@Where(clause = "d = 0")
@SQLDelete(sql = "UPDATE scheduled SET d = 1 WHERE id=? AND v=?")
@Accessors(chain = true)
@Table(name = "sys_scheduled_task")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class ScheduledTask extends ListEntity {

    private static final long serialVersionUID = -2520015535285512159L;

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
