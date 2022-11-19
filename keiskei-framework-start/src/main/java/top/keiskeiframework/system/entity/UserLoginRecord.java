package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import top.keiskeiframework.common.base.entity.impl.ListEntityImpl;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen
 * @since 2022/11/19 20:51
 */
@Data
@Entity
@Table(name = "sys_user_login_record")
@TableName(value = "sys_user_login_record")
@ApiModel(value = "UserLoginRecord", description = "管理员登录记录")
public class UserLoginRecord extends ListEntityImpl<Long> {

    private static final long serialVersionUID = -8330585475074725544L;

    private Integer userId;
    private String ip;
    private String address;

}
