package top.keiskeiframework.logdb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.dashboard.Chartable;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.ListEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "log_operate_log")
@ApiModel(value = "OperateLog", description = "操作日志")
@Chartable
public class OperateLog extends ListEntity<Integer> {

    private static final long serialVersionUID = -1735705706625546610L;


    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "操作人员", dataType = "Integer")
    private Integer userId;

    @ApiModelProperty(value = "操作IP", dataType = "String")
    @NotNull(message = "请选择操作IP", groups = {Insert.class})
    @Chartable
    private String ip;

    @ApiModelProperty(value = "操作名称", dataType = "String")
    @NotBlank(message = "操作名称不能为空", groups = {Insert.class})
    @Chartable
    private String name;

    @ApiModelProperty(value = "操作类型", dataType = "String")
    @NotBlank(message = "操作类型不能为空", groups = {Insert.class})
    @Chartable
    private String type;

    @ApiModelProperty(value = "操作链接", dataType = "String")
    @NotBlank(message = "操作链接不能为空", groups = {Insert.class})
    private String url;

    @ApiModelProperty(value = "请求参数", dataType = "String")
    @NotBlank(message = "请求参数不能为空", groups = {Insert.class})
    private String requestParam;

    @ApiModelProperty(value = "返回结果", dataType = "String")
    @NotBlank(message = "返回结果不能为空", groups = {Insert.class})
    private String responseParam;

}
