package top.keiskeiframework.cpreading.dto.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 *
 * </p>
 * @since 2020/11/1 17:00
 */
@Data
public class WechatRefundNotifyResponse {

    /**
     * 微信分配的公众账号ID（企业号corpid即为此appId）
     */
    @ApiModelProperty(value = "公众账号ID", dataType = "String")
    private String appid;

    /**
     * 微信支付分配的商户号
     */
    @ApiModelProperty(value = "退款的商户号", dataType = "String")
    private String mch_id;

    /**
     * 随机字符串，不长于32位。推荐随机数生成算法
     */
    @ApiModelProperty(value = "随机字符串", dataType = "String")
    private String nonce_str;

    /**
     * 加密信息请用商户秘钥进行解密
     */
    @ApiModelProperty(value = "加密信息", dataType = "String")
    private String req_info;


    /**
     * SUCCESS/FAIL
     * SUCCESS表示商户接收通知成功并校验成功
     **/
    @ApiModelProperty(value = "返回状态码", dataType = "String")
    private String return_code;

    /**
     * SUCCESS/FAIL
     * SUCCESS表示商户接收通知成功并校验成功
     **/
    @ApiModelProperty(value = "返回状态码", dataType = "String")
    private String result_code;
    /**
     * 当result_code为FAIL时返回错误代码，详细参见下文错误列表
     **/
    @ApiModelProperty(value = "返回状态码", dataType = "String")
    private String err_code;
    /**
     * 当result_code为FAIL时返回错误描述，详细参见下文错误列表
     **/
    @ApiModelProperty(value = "返回状态码", dataType = "String")
    private String err_code_des;

    /**
     * 返回信息，如非空，为错误原因： 签名失败
     * 参数格式校验错误
     **/
    @ApiModelProperty(value = "返回信息", dataType = "String")
    private String return_msg;


    private WechatRefundNotifyDetailResponse detail;


    public static WechatRefundNotifyResponse mapToBean(Map<String, String> map) {
        WechatRefundNotifyResponse temp = new WechatRefundNotifyResponse();
        temp.setAppid(map.get("appid"));
        temp.setMch_id(map.get("mch_id"));
        temp.setNonce_str(map.get("nonce_str"));
        temp.setReq_info(map.get("req_info"));
        temp.setReturn_code(map.get("return_code"));
        temp.setResult_code(map.get("result_code"));
        temp.setErr_code(map.get("err_code"));
        temp.setErr_code_des(map.get("err_code_des"));
        temp.setReturn_msg(map.get("return_msg"));
        return temp;

    }
}
