package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.ITreeEntity;
import top.keiskeiframework.common.base.entity.impl.TreeEntityImpl;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;


/**
 * @author cjm
 */
@Data
@Entity
@Table(name = "sys_permission")
@TableName(value = "sys_permission")
@ApiModel(value = "Permission", description = "角色")
public class Permission extends TreeEntityImpl<Integer> implements ITreeEntity<Integer> {

    private static final long serialVersionUID = -2219244944682016397L;

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
    @OrderBy(asc = true)
    private Integer sortBy;


}
