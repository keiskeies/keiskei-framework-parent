package top.keiskeiframework.cpreading.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.cpreading.enums.ReaderSexEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 读者管理 实体类
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cpreading_reader")
@ApiModel(value="Reader", description="读者管理")
public class Reader extends ListEntity<Long> {

    private static final long serialVersionUID = 2995398319070487991L;

    @ApiModelProperty(value = "姓名", dataType="String")
    private String name;

    @ApiModelProperty(value = "昵称", dataType="String")
    private String nikeName;

    @ApiModelProperty(value = "头像", dataType="String")
    private String avatar;

    @ApiModelProperty(value = "手机号", dataType="String")
    private String phone;

    @ApiModelProperty(value = "邮箱地址", dataType="String")
    private String email;

    @ApiModelProperty(value = "签名", dataType="String")
    @Column(columnDefinition = "text")
    private String description;

    @ApiModelProperty(value = "微信公众号OPENID", dataType="String")
    private String wechatWebOpenid;

    @ApiModelProperty(value = "微信小程序OPENID", dataType="String")
    private String wechatMiniOpenid;


    @ApiModelProperty(value = "性别", dataType="String")
    private ReaderSexEnum sex;

    @ApiModelProperty(value = "微信唯一标识", dataType="String")
    private String unionid;

    @ApiModelProperty(value = "微信特权", dataType="String")
    private String privilege;

    @ApiModelProperty(value = "抖音OPENID", dataType="String")
    private String tiktokOpenid;

    @ApiModelProperty(value = "阿里OPENID", dataType="String")
    private String alipayOpenid;
}
