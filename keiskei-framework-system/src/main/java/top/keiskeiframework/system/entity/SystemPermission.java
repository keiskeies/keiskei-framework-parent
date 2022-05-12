package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.TreeEntity;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 后台权限详情信息
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019-06-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "sys_permission")
@ApiModel(value = "SystemPermission", description = "操作权限")
public class SystemPermission extends TreeEntity<Integer>  {

    private static final long serialVersionUID = -7872341210410988194L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "权限标识", dataType = "String")
    @NotBlank(message = "权限标识不能为空", groups = {Insert.class})
    private String permission;

    @NotBlank(message = "资源名称不能为空", groups = {Insert.class})
    @ApiModelProperty(value = "资源名称", dataType = "String")
    private String name;

    @NotBlank(message = "请输入URL路径", groups = {Insert.class})
    @ApiModelProperty(value = "请求URL路径", dataType = "String")
    private String path;

    @NotBlank(message = "请求方式不能为空", groups = {Insert.class})
    @ApiModelProperty(value = "请求方式", dataType = "String")
    private String method;

    @ApiModelProperty(value = "排序", dataType = "Integer")
    @OrderBy
    private Integer sortBy;
}
