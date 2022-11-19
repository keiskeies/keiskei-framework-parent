package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import top.keiskeiframework.common.base.entity.impl.ListEntityImpl;

import javax.persistence.*;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen
 * @since 2022/11/17 10:38
 */
@Data
@Entity
@Table(name = "sys_user_setting")
@TableName(value = "sys_user_setting")
@ApiModel(value = "UserSetting", description = "管理员设置")
public class UserSetting extends ListEntityImpl<Integer> {

    private static final long serialVersionUID = 2713833138598881989L;

    @TableId(type = IdType.INPUT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String size = "size";

    private String theme;

    private Boolean showSettings = Boolean.TRUE;

    private Boolean tagsView = Boolean.TRUE;

    private Boolean fixedHeader = Boolean.FALSE;

    private Boolean sidebarLogo = Boolean.FALSE;
}
