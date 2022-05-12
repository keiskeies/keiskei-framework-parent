package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 部门管理
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_department")
@ApiModel(value = "SystemDepartment", description = "部门管理")
public class SystemDepartment extends TreeEntity<Integer>  {

    private static final long serialVersionUID = 4845168322937249454L;


    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "部门名称", dataType = "String")
    @NotBlank(message = "部门名称不能为空", groups = {Insert.class})
    private String name;

}
