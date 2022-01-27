package top.keiskeiframework.demo.customer.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.demo.customer.enums.SexEnum;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/25 17:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demo_customer")
@ApiModel(value = "Customer", description = "顾客")
public class Customer extends ListEntity<Long> {

    @ApiModelProperty(value = "昵称", dataType = "String")
    private String nickname;

    @ApiModelProperty(value = "账号", dataType = "String")
    private String username;

    @ApiModelProperty(value = "密码", dataType = "String")
    private String password;

    @ApiModelProperty(value = "邮箱", dataType = "String")
    private String email;

    @ApiModelProperty(value = "手机号", dataType = "String")
    private String phone;

    @ApiModelProperty(value = "头像", dataType = "String")
    private String avatar;

    @ApiModelProperty(value = "签名", dataType = "String")
    private String description;

    @ApiModelProperty(value = "性别", dataType = "String")
    private SexEnum sex;

    @ApiModelProperty(value = "生日", dataType = "String")
    private LocalDate birthday;

}
