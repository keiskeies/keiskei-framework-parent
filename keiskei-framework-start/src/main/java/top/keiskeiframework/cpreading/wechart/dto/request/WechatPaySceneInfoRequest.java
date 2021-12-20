package top.keiskeiframework.cpreading.wechart.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 *
 * </p>
 * @since 2020/11/1 15:43
 */
@Data
public class WechatPaySceneInfoRequest {

    /**
     * String(32)	SZTX001	门店编号，由商户自定义
     */
    @ApiModelProperty(value = "门店id", dataType = "String", required = false)
    private String id;

    /**
     * String(64)	腾讯大厦腾大餐厅	门店名称 ，由商户自定义
     */
    @ApiModelProperty(value = "门店名称", dataType = "String", required = false)
    private String name;

    /**
     * String(6)	440305	门店所在地行政区划码，详细见《最新县及县以上行政区划代码》
     */
    @ApiModelProperty(value = "门店行政区划码", dataType = "String", required = false)
    private String area_code;

    /**
     * String(128)	科技园中一路腾讯大厦	门店详细地址 ，由商户自定义
     */
    @ApiModelProperty(value = "门店详细地址", dataType = "String", required = false)
    private String address;
}
