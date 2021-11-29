package top.keiskeiframework.cpreading.entity;

import com.fasterxml.jackson.annotation.*;
import top.keiskeiframework.common.base.entity.*;
import top.keiskeiframework.common.util.data.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.cpreading.enums.*;

import io.swagger.annotations.*;

import javax.persistence.*;
import javax.validation.*;
import javax.validation.constraints.*;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.annotation.validate.*;
import top.keiskeiframework.common.annotation.data.*;

import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import java.time.*;
import java.util.*;

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

    @NotBlank(message = "姓名不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "姓名", dataType="String")
    private String name;

    @NotBlank(message = "昵称不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "昵称", dataType="String")
    private String nikeName;

    @NotBlank(message = "头像不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "头像", dataType="String")
    private String avatar;

    @NotBlank(message = "手机号不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "手机号", dataType="String")
    private String phone;

    @NotBlank(message = "邮箱地址不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "邮箱地址", dataType="String")
    private String email;

    @NotBlank(message = "微信公众号OPENID不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "微信公众号OPENID", dataType="String")
    private String wechatＷebOpenid;

    @NotBlank(message = "微信小程序OPENID不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "微信小程序OPENID", dataType="String")
    private String ｗechatMiniOpenId;

    @NotBlank(message = "抖音OPENID不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "抖音OPENID", dataType="String")
    private String tiktokOpenid;

    @NotBlank(message = "阿里OPENID不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "阿里OPENID", dataType="String")
    private String alipayOpenid;

    @NotNull(message = "性别不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "性别", dataType="String")
    private ReaderSexEnum sex;

    @NotBlank(message = "微信唯一标识不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "微信唯一标识", dataType="String")
    private String unionid;

    @NotBlank(message = "微信特权不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "微信特权", dataType="String")
    private String privilege;

    @NotBlank(message = "签名不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "签名", dataType="String")
    @Column(columnDefinition = "text")
    private String description;

}
