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
@ApiModel(value = "VipMiniAppOpenidRequest", description = "用户信息 前台请求类")
public class VipMiniAppOpenidRequest {


    @ApiModelProperty(value = "wxCode", dataType = "String")
    @NotBlank(message = "wxCode为空", groups = {Insert.class})
    private String wxCode;


}
