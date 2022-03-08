package top.keiskeiframework.cpreading.reader.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.keiskeiframework.common.annotation.validate.Insert;

import javax.validation.constraints.NotBlank;


/**
 * <p>
 * 用户信息 admin 返回类
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-10-25
 */

@Data
@ApiModel(value = "VipRequest", description = "用户信息 前台请求类")
public class VipRequest {

    @ApiModelProperty(value = "姓名", dataType = "String")
    private String name;

    @ApiModelProperty(value = "手机号", dataType = "String")
    private String phone;

    @ApiModelProperty(value = "性别", dataType = "Integer")
    private Integer sex;

    @ApiModelProperty(value = "头像", dataType = "String")
    private String avatar;

    @ApiModelProperty(value = "国家", dataType = "String")
    private String country;

    @ApiModelProperty(value = "省", dataType = "String")
    private String province;

    @ApiModelProperty(value = "城市", dataType = "String")
    private String city;

    @ApiModelProperty(value = "wxCode", dataType = "String")
    @NotBlank(message = "wxCode为空", groups = {Insert.class})
    private String wxCode;


}
