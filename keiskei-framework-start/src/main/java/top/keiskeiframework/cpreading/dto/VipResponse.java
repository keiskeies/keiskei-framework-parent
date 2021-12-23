package top.keiskeiframework.cpreading.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 用户信息 admin 返回类
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-10-25
 */

@Data
@ApiModel(value="VipResponse", description="用户信息 前台返回类")
public class VipResponse implements Serializable {

    private static final long serialVersionUID = -6438788677327822483L;

    @ApiModelProperty(value = "用户信息id", dataType="Integer")
    private Long id;

    @ApiModelProperty(value = "姓名", dataType="String")
    private String name;

    @ApiModelProperty(value = "手机号", dataType="String")
    private String phone;

    @ApiModelProperty(value = "微信open ID", dataType="String")
    private String wechatOpenid;

    @ApiModelProperty(value = "头像", dataType="String")
    private String avatar;

    @ApiModelProperty(value = "token", dataType="String")
    private String token;

    @ApiModelProperty(value = "wxCode", dataType="String")
    private String wxCode;

    @ApiModelProperty(value = "appName", dataType="String")
    private String appName;

    @ApiModelProperty(value = "性别 1为男性，2为女性", dataType="Integer")
    private Integer sex;

    @ApiModelProperty(value = "IP", dataType="String")
    private String ip;

    @ApiModelProperty(value = "首次登陆提示", dataType="String")
    private String firstLoginMessage;




}
