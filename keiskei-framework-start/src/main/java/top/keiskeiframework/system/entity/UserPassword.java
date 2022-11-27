package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.base.entity.impl.ListEntityImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/11/15 15:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_user_password")
@TableName(value = "sys_user_password")
@ApiModel(value = "UserPassword", description = "管理员密码")
public class UserPassword extends ListEntityImpl<Integer> {

    private static final long serialVersionUID = 8917166246144369153L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "管理员", dataType = "String")
    private Integer userId;

    @ApiModelProperty(value = "密码", dataType = "String")
    private String password;

    @ApiModelProperty(value = "初始登录密码", dataType = "Boolean")
    @Column(columnDefinition = "tinyint(1) default 0")
    private Boolean firstSet;

}
