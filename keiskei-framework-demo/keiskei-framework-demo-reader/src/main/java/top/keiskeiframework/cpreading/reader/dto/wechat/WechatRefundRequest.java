package top.keiskeiframework.cpreading.reader.dto.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * ([\u4e00-\u9fa5|A-Z|a-z|_]+)	([a-z|_]+)	([是|否])	([A-Z|a-z]+)([\d|\(\|)]+)	([	|\u4e00-\u9fa5|a-z|A-Z|0-9|_| |，|-]+)
 * </p>
 * @since 2020/11/1 16:48
 */
@Data
public class WechatRefundRequest {

    /**
     * length: (32) ; wx8888888888888888	微信分配的公众账号ID（企业号corpid即为此appId）
     **/
    @ApiModelProperty(value = "公众账号ID", dataType = "String", required = true)
    private String appid;


    /**
     * length: (32)	1900000109 ; 微信支付分配的商户号
     **/
    @ApiModelProperty(value = "商户号", dataType = "String", required = true)
    private String mch_id;


    /**
     * length: (32) ; 5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
     **/
    @ApiModelProperty(value = "随机字符串", dataType = "String", required = true)
    private String nonce_str;


    /**
     * length: (32) ; C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
     **/
    @ApiModelProperty(value = "签名", dataType = "String", required = true)
    private String sign;


    /**
     * length: (32) ; HMAC-SHA256	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
     **/
    @ApiModelProperty(value = "签名类型", dataType = "String")
    private String sign_type;


    /**
     * length: (32)	1217752501201407033233368018 ; 微信生成的订单号，在支付通知中有返回
     **/
    @ApiModelProperty(value = "微信订单号", dataType = "String")
    private String transaction_id;
    /**
     * length: (32)
     * 1217752501201407033233368018	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     * transaction_id、out_trade_no二选一，如果同时存在优先级：transaction_id> out_trade_no
     */
    @ApiModelProperty(value = "商户订单号", dataType = "String")
    private String out_trade_no;


    /**
     * length: (64)	1217752501201407033233368018 ; 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     **/
    @ApiModelProperty(value = "商户退款单号", dataType = "String", required = true)
    private String out_refund_no;


    /**
     * length: 	100 ; 订单总金额，单位为分，只能为整数，详见支付金额
     **/
    @ApiModelProperty(value = "订单金额", dataType = "String", required = true)
    private Integer total_fee;


    /**
     * length: 	100 ; 退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
     **/
    @ApiModelProperty(value = "退款金额", dataType = "String", required = true)
    private Integer refund_fee;


    /**
     * length: (8) ; CNY	退款货币类型，需与支付一致，或者不填。符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     **/
    @ApiModelProperty(value = "退款货币种类", dataType = "String")
    private String refund_fee_type;


    /**
     * length: (80) ; 商品已售完
     * 若商户传入，会在下发给用户的退款消息中体现退款原因
     * <p>
     * 注意：若订单退款金额≤1元，且属于部分退款，则不会在退款消息中体现退款原因
     **/
    @ApiModelProperty(value = "退款原因", dataType = "String")
    private String refund_desc;


    /**
     * length: (30) ; REFUND_SOURCE_RECHARGE_FUNDS
     * 仅针对老资金流商户使用
     * <p>
     * REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）
     * <p>
     * REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款
     **/
    @ApiModelProperty(value = "退款资金来源", dataType = "String")
    private String refund_account;


    /**
     * length: (256) ; https://weixin.qq.com/notify/
     * 异步接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数
     * <p>
     * 如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效。
     **/
    @ApiModelProperty(value = "退款结果通知url", dataType = "String")
    private String notify_url;

    public Map<String, String> toMap() {
        Map<String,String> map = new HashMap<>();
        if (!StringUtils.isEmpty(this.appid)) {map.put("appid",appid);}
        if (!StringUtils.isEmpty(this.mch_id)) {map.put("mch_id",mch_id);}
        if (!StringUtils.isEmpty(this.nonce_str)) {map.put("nonce_str",nonce_str);}
        if (!StringUtils.isEmpty(this.sign)) {map.put("sign",sign);}
        if (!StringUtils.isEmpty(this.sign_type)) {map.put("sign_type",sign_type);}
        if (!StringUtils.isEmpty(this.transaction_id)) {map.put("transaction_id",transaction_id);}
        if (!StringUtils.isEmpty(this.out_trade_no)) {map.put("out_trade_no",out_trade_no);}
        if (!StringUtils.isEmpty(this.out_refund_no)) {map.put("out_refund_no",out_refund_no);}
        if (null != total_fee) {map.put("total_fee",total_fee + "");}
        if (null != this.refund_fee) {map.put("refund_fee",refund_fee + "");}
        if (!StringUtils.isEmpty(this.refund_fee_type)) {map.put("refund_fee_type",refund_fee_type);}
        if (!StringUtils.isEmpty(this.refund_desc)) {map.put("refund_desc",refund_desc);}
        if (!StringUtils.isEmpty(this.refund_account)) {map.put("refund_account",refund_account);}
        if (!StringUtils.isEmpty(this.notify_url)) {map.put("notify_url",notify_url);}
        return map;
    }

}
