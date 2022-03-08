package top.keiskeiframework.cpreading.reader.dto.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 支付回调信息
 * </p>
 * @since 2020/11/1 16:03
 */
@Data
public class WechatPayNotifyResponse {


    /**
     * length: (32) ; wx8888888888888888	微信分配的小程序ID
     **/
    @ApiModelProperty(value = "小程序ID", dataType = "String")
    private String appid;


    /**
     * length: (32)	1900000109 ; 微信支付分配的商户号
     **/
    @ApiModelProperty(value = "商户号", dataType = "String")
    private String mch_id;


    /**
     * length: (32)	013467007045764 ; 微信支付分配的终端设备号，
     **/
    @ApiModelProperty(value = "设备号", dataType = "String")
    private String device_info;


    /**
     * length: (32) ; 5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位
     **/
    @ApiModelProperty(value = "随机字符串", dataType = "String")
    private String nonce_str;


    /**
     * length: (32) ; C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名算法
     **/
    @ApiModelProperty(value = "签名", dataType = "String")
    private String sign;


    /**
     * length: (32) ; HMAC-SHA256	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
     **/
    @ApiModelProperty(value = "签名类型", dataType = "String")
    private String sign_type;


    /**
     * length: (16) ; SUCCESS	SUCCESS/FAIL
     **/
    @ApiModelProperty(value = "业务结果", dataType = "String")
    private String result_code;


    /**
     * length: (32) ; SYSTEMERROR	错误返回的信息描述
     **/
    @ApiModelProperty(value = "错误代码", dataType = "String")
    private String err_code;


    /**
     * length: (128) ; 系统错误	错误返回的信息描述
     **/
    @ApiModelProperty(value = "错误代码描述", dataType = "String")
    private String err_code_des;


    /**
     * length: (128) ; wxd930ea5d5a258f4f	用户在商户appid下的唯一标识
     **/
    @ApiModelProperty(value = "用户标识", dataType = "String")
    private String openid;


    /**
     * length: (1) ; Y	用户是否关注公众账号，Y-关注，N-未关注
     **/
    @ApiModelProperty(value = "是否关注公众账号", dataType = "String")
    private String is_subscribe;


    /**
     * length: (16) ; JSAPI	JSAPI、NATIVE、APP
     **/
    @ApiModelProperty(value = "交易类型", dataType = "String")
    private String trade_type;


    /**
     * length: (32) ; CMC	银行类型，采用字符串类型的银行标识，银行类型见银行列表
     **/
    @ApiModelProperty(value = "付款银行", dataType = "String")
    private String bank_type;


    /**
     * length: 	100 ; 订单总金额，单位为分
     **/
    @ApiModelProperty(value = "订单金额", dataType = "String")
    private Integer total_fee;


    /**
     * length: 	100 ; 应结订单金额
     * =订单金额-非充值代金券金额，应结订单金额<=订单金额。
     **/
    @ApiModelProperty(value = "应结订单金额", dataType = "String")
    private Integer settlement_total_fee;


    /**
     * length: (8) ; CNY	货币类型，符合ISO4217标准的三位字母代码，默认人民币
     * ：CNY，其他值列表详见货币类型
     **/
    @ApiModelProperty(value = "货币种类", dataType = "String")
    private String fee_type;


    /**
     * length: 	100 ; 现金支付金额订单现金支付金额，详见支付金额
     **/
    @ApiModelProperty(value = "现金支付金额", dataType = "String")
    private Integer cash_fee;


    /**
     * length: (16) ; CNY	货币类型，符合ISO4217标准的三位字母代码，默认人民币
     * ：CNY，其他值列表详见货币类型
     **/
    @ApiModelProperty(value = "现金支付货币类型", dataType = "String")
    private String cash_fee_type;


    /**
     * length: 	10 ; 代金券金额
     * <=订单金额，订单金额-代金券金额=现金支付金额，详见支付金额
     **/
    @ApiModelProperty(value = "总代金券金额", dataType = "String")
    private Integer coupon_fee;


    /**
     * length: 	1 ; 代金券使用数量
     * 代金券类型	coupon_type_$n	否	String	CASH
     * CASH--充值代金券
     * NO_CASH---非充值代金券
     * <p>
     * 并且订单使用了免充值券后有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_0
     * <p>
     * 注意：只有下单时订单使用了优惠，回调通知才会返回券信息。
     * 下列情况可能导致订单不可以享受优惠：可能情况。
     * 代金券ID	coupon_id_$n	否	String(20)	10000	代金券ID,$n为下标，从0开始编号
     * 注意：只有下单时订单使用了优惠，回调通知才会返回券信息。
     * 下列情况可能导致订单不可以享受优惠：可能情况。
     * 单个代金券支付金额	coupon_fee_$n	否	Int	100	单个代金券支付金额,$n为下标，从0开始编号
     **/
    @ApiModelProperty(value = "代金券使用数量", dataType = "String")
    private Integer coupon_count;


    /**
     * length: (32)	1217752501201407033233368018 ; 微信支付订单号
     **/
    @ApiModelProperty(value = "微信支付订单号", dataType = "String")
    private String transaction_id;


    /**
     * length: (32)	1212321211201407033568112322 ; 商户系统内部订单号，要求32个字符内，只能是数字
     * 、大小写字母_-|*@ ，且在同一个商户号下唯一。
     **/
    @ApiModelProperty(value = "商户订单号", dataType = "String")
    private String out_trade_no;


    /**
     * length: (128)	123456 ; 商家数据包，原样返回
     **/
    @ApiModelProperty(value = "商家数据包", dataType = "String")
    private String attach;


    /**
     * length: (14)	20141030133525 ; 支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
     **/
    @ApiModelProperty(value = "支付完成时间", dataType = "String")
    private String time_end;



    /**
     * SUCCESS/FAIL
     * <p>
     * SUCCESS表示商户接收通知成功并校验成功
     **/
    @ApiModelProperty(value = "返回状态码", dataType = "String")
    private String return_code;

    /**
     * 返回信息，如非空，为错误原因：
     * <p>
     * 签名失败
     * <p>
     * 参数格式校验错误
     **/
    @ApiModelProperty(value = "返回信息", dataType = "String")
    private String return_msg;



    public static WechatPayNotifyResponse mapToBean(Map<String, String> map) {
        WechatPayNotifyResponse temp = new WechatPayNotifyResponse();
        temp.setAppid(map.get("appid"));
        temp.setMch_id(map.get("mch_id"));
        temp.setDevice_info(map.get("device_info"));
        temp.setNonce_str(map.get("nonce_str"));
        temp.setSign(map.get("sign"));
        temp.setSign_type(map.get("sign_type"));
        temp.setResult_code(map.get("result_code"));
        temp.setErr_code(map.get("err_code"));
        temp.setErr_code_des(map.get("err_code_des"));
        temp.setOpenid(map.get("openid"));
        temp.setIs_subscribe(map.get("is_subscribe"));
        temp.setTrade_type(map.get("trade_type"));
        temp.setBank_type(map.get("bank_type"));
        temp.setTotal_fee(map.containsKey("total_fee") ? Integer.valueOf(map.get("total_fee")): null);
        temp.setSettlement_total_fee(map.containsKey("settlement_total_fee") ? Integer.valueOf(map.get("settlement_total_fee")): null);
        temp.setFee_type(map.get("fee_type"));
        temp.setCash_fee(map.containsKey("cash_fee") ? Integer.valueOf(map.get("cash_fee")): null);
        temp.setCash_fee_type(map.get("cash_fee_type"));
        temp.setCoupon_fee(map.containsKey("coupon_fee") ? Integer.valueOf(map.get("coupon_fee")) : null);
        temp.setCoupon_count(map.containsKey("coupon_count") ? Integer.valueOf(map.get("coupon_count")) : null);
        temp.setTransaction_id(map.get("transaction_id"));
        temp.setOut_trade_no(map.get("out_trade_no"));
        temp.setAttach(map.get("attach"));
        temp.setTime_end(map.get("time_end"));
        temp.setReturn_code(map.get("return_code"));
        temp.setReturn_msg(map.get("return_msg"));
        return temp;

    }

}
