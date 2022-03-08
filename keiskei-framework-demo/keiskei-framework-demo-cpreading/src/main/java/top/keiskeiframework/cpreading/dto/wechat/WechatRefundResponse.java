package top.keiskeiframework.cpreading.dto.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 *
 * </p>
 * @since 2020/11/1 16:57
 */
@Data
public class WechatRefundResponse {


    /**
     * length: (16) ; SUCCESS
     * SUCCESS/FAIL
     * <p>
     * SUCCESS退款申请接收成功，结果通过退款查询接口查询
     * <p>
     * FAIL 提交业务失败
     **/
    @ApiModelProperty(value = "业务结果", dataType = "String")
    private String result_code;


    /**
     * length: (32) ; SYSTEMERROR	列表详见错误码列表
     **/
    @ApiModelProperty(value = "错误代码", dataType = "String")
    private String err_code;


    /**
     * length: (128) ; 系统超时	结果信息描述
     **/
    @ApiModelProperty(value = "错误代码描述", dataType = "String")
    private String err_code_des;


    /**
     * length: (32) ; wx8888888888888888	微信分配的公众账号ID
     **/
    @ApiModelProperty(value = "公众账号ID", dataType = "String")
    private String appid;


    /**
     * length: (32)	1900000109 ; 微信支付分配的商户号
     **/
    @ApiModelProperty(value = "商户号", dataType = "String")
    private String mch_id;


    /**
     * length: (32) ; 5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位
     **/
    @ApiModelProperty(value = "随机字符串", dataType = "String")
    private String nonce_str;


    /**
     * length: (32) ; 5K8264ILTKCH16CQ2502SI8ZNMTM67VS	签名，详见签名算法
     **/
    @ApiModelProperty(value = "签名", dataType = "String")
    private String sign;


    /**
     * length: (32)	4007752501201407033233368018 ; 微信订单号
     **/
    @ApiModelProperty(value = "微信订单号", dataType = "String")
    private String transaction_id;


    /**
     * length: (32)	33368018 ; 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     **/
    @ApiModelProperty(value = "商户订单号", dataType = "String")
    private String out_trade_no;


    /**
     * length: (64)	121775250 ; 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     **/
    @ApiModelProperty(value = "商户退款单号", dataType = "String")
    private String out_refund_no;


    /**
     * length: (32)	2007752501201407033233368018 ; 微信退款单号
     **/
    @ApiModelProperty(value = "微信退款单号", dataType = "String")
    private String refund_id;


    /**
     * length: 	100 ; 退款总金额,单位为分,可以做部分退款
     **/
    @ApiModelProperty(value = "退款金额", dataType = "String")
    private int refund_fee;


    /**
     * length: 	100 ; 去掉非充值代金券退款金额后的退款金额，退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
     **/
    @ApiModelProperty(value = "应结退款金额", dataType = "String")
    private int settlement_refund_fee;


    /**
     * length: 	100 ; 订单总金额，单位为分，只能为整数，详见支付金额
     **/
    @ApiModelProperty(value = "标价金额", dataType = "String")
    private int total_fee;


    /**
     * length: 	100 ; 去掉非充值代金券金额后的订单总金额，应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
     **/
    @ApiModelProperty(value = "应结订单金额", dataType = "String")
    private int settlement_total_fee;


    /**
     * length: (8) ; CNY	订单金额货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     **/
    @ApiModelProperty(value = "标价币种", dataType = "String")
    private String fee_type;


    /**
     * length: 	100 ; 现金支付金额，单位为分，只能为整数，详见支付金额
     **/
    @ApiModelProperty(value = "现金支付金额", dataType = "String")
    private int cash_fee;


    /**
     * length: (16) ; CNY	货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     **/
    @ApiModelProperty(value = "现金支付币种", dataType = "String")
    private String cash_fee_type;


    /**
     * length: 	100 ; 现金退款金额，单位为分，只能为整数，详见支付金额
     **/
    @ApiModelProperty(value = "现金退款金额", dataType = "String")
    private int cash_refund_fee;


    /**
     * length: 	100 ; 代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
     * 单个代金券退款金额	coupon_refund_fee_$n	否	int	100	代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
     **/
    @ApiModelProperty(value = "代金券退款总金额", dataType = "String")
    private int coupon_refund_fee;


    /**
     * length: 	1 ; 退款代金券使用数量
     **/
    @ApiModelProperty(value = "退款代金券使用数量", dataType = "String")
    private int coupon_refund_count;



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

}
