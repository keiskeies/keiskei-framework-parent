package top.keiskeiframework.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.BaseEntity;

import javax.persistence.*;
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
@Entity
@Table(name = "sys_operate_log")
@ApiModel(value = "OperateLog", description = "操作日志")
public class OperateLog extends BaseEntity {

    private static final long serialVersionUID = -1735705706625546610L;

    @ApiModelProperty(value = "操作人员", dataType = "Long")
    @NotNull(message = "操作人员不能为空", groups = {Insert.class})
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ApiModelProperty(value = "操作IP", dataType = "String")
    @NotNull(message = "请选择操作IP", groups = {Insert.class})
    private String ip;

    @ApiModelProperty(value = "操作名称", dataType = "String")
    @NotBlank(message = "操作名称不能为空", groups = {Insert.class})
    private String name;

    @ApiModelProperty(value = "操作类型", dataType = "String")
    @NotBlank(message = "操作类型不能为空", groups = {Insert.class})
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
