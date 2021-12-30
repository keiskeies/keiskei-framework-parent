package top.keiskeiframework.cpreading.dto.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * ([\u4e00-\u9fa5|A-Z]+)	([a-z|_]+)	([是|否])	([A-Z|a-z]+)([\d|\(\|)]+)	([	|\u4e00-\u9fa5|a-z|A-Z|0-9|_| |，]+)
 * </p>
 * @since 2020/11/1 16:38
 */
@Data
public class WechatPayResponse {


    /**
     * length: (32) ; wx8888888888888888	调用接口提交的公众账号ID
     **/
    @ApiModelProperty(value = "公众账号ID", dataType = "String")
    private String appid;


    /**
     * length: (32) ; 1900000109	调用接口提交的商户号
     **/
    @ApiModelProperty(value = "商户号", dataType = "String")
    private String mch_id;


    /**
     * length: (32) ; 013467007045764	自定义参数，可以为请求支付的终端设备号等
     **/
    @ApiModelProperty(value = "设备号", dataType = "String")
    private String device_info;


    /**
     * length: (32) ; 5K8264ILTKCH16CQ2502SI8ZNMTM67VS	微信返回的随机字符串
     **/
    @ApiModelProperty(value = "随机字符串", dataType = "String")
    private String nonce_str;


    /**
     * length: (32) ; C380BEC2BFD727A4B6845133519F3AD6	微信返回的签名值，详见签名算法
     **/
    @ApiModelProperty(value = "签名", dataType = "String")
    private String sign;


    /**
     * length: (16) ; SUCCESS	SUCCESS/FAIL
     **/
    @ApiModelProperty(value = "业务结果", dataType = "String")
    private String result_code;


    /**
     * length: (32) ;  	当result_code为FAIL时返回错误代码，详细参见下文错误列表
     **/
    @ApiModelProperty(value = "错误代码", dataType = "String")
    private String err_code;


    /**
     * length: (128) ;  	当result_code为FAIL时返回错误描述，详细参见下文错误列表
     **/
    @ApiModelProperty(value = "错误代码描述", dataType = "String")
    private String err_code_des;


    /**
     * length: (16) ; JSAPI
     * JSAPI -JSAPI支付
     * <p>
     * NATIVE -Native支付
     * <p>
     * APP -APP支付
     **/
    @ApiModelProperty(value = "交易类型", dataType = "String")
    private String trade_type;


    /**
     * length: (64) ; wx201410272009395522657a690389285100	微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
     **/
    @ApiModelProperty(value = "预支付交易会话标识", dataType = "String")
    private String prepay_id;


    /**
     * length: (64) ; weixin
     * ://wxpay/bizpayurl/up?pr=NwY5Mz9&groupid=00
     * trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。
     * <p>
     * 注意：code_url的值并非固定，使用时按照URL格式转成二维码即可
     **/
    @ApiModelProperty(value = "二维码链接", dataType = "String")
    private String code_url;



    /**
     * SUCCESS/FAIL
     * SUCCESS表示商户接收通知成功并校验成功
     **/
    @ApiModelProperty(value = "返回状态码", dataType = "String")
    private String return_code;

    /**
     * 返回信息，如非空，为错误原因： 签名失败
     * 参数格式校验错误
     **/
    @ApiModelProperty(value = "返回信息", dataType = "String")
    private String return_msg;


    @ApiModelProperty(value = "时间戳", dataType = "String")
    private String timestamp;


    public static WechatPayResponse mapToBean(Map<String, String> map) {
        WechatPayResponse temp = new WechatPayResponse();
        temp.setAppid(map.get("appid"));
        temp.setMch_id(map.get("mch_id"));
        temp.setDevice_info(map.get("device_info"));
        temp.setNonce_str(map.get("nonce_str"));
        temp.setSign(map.get("sign"));
        temp.setResult_code(map.get("result_code"));
        temp.setErr_code(map.get("err_code"));
        temp.setErr_code_des(map.get("err_code_des"));
        temp.setTrade_type(map.get("trade_type"));
        temp.setPrepay_id(map.get("prepay_id"));
        temp.setCode_url(map.get("code_url"));
        temp.setReturn_code(map.get("return_code"));
        temp.setReturn_msg(map.get("return_msg"));
        return temp;
    }

}
