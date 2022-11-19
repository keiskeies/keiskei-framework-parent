package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.ITreeEntity;
import top.keiskeiframework.common.base.entity.impl.TreeEntityImpl;
import top.keiskeiframework.common.base.mp.annotation.MpManyToOne;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author cjm
 */
@Data
@Entity
@Table(name = "sys_department")
@TableName(value = "sys_department")
@ApiModel(value = "Department", description = "组织")
public class Department extends TreeEntityImpl<Integer> implements ITreeEntity<Integer> {

    private static final long serialVersionUID = 5981923064186704999L;

    @ApiModelProperty(value = "组织名称", dataType = "String")
    @NotBlank(message = "组织名称不能为空", groups = {Insert.class, Update.class})
    private String name;

    private String logo;

    @MpManyToOne(filedName = "ownerId")
    @TableField(exist = false)
    @Transient
    private transient User owner;
    private Integer ownerId;

    @MpManyToOne(filedName = "levelId")
    @NotNull(message = "组织等级不能为空", groups = {Insert.class, Update.class})
    @TableField(exist = false)
    @Transient
    private transient DepartmentLevel level;
    private Integer levelId;


}
