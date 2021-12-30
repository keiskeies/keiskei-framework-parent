package top.keiskeiframework.cpreading.dto.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 退款通知详情
 * </p>
 * @since 2020/11/1 17:04
 */
@Data
public class WechatRefundNotifyDetailResponse {
    /**
     * String(32)
     * 微信订单号
     */
    @ApiModelProperty(value = "微信订单号")
    private String transaction_id;

    /**
     * String(32)
     * 商户系统内部的订单号
     */
    @ApiModelProperty(value = "商户订单号")
    private String out_trade_no;

    /**
     * String(32)
     * 微信退款单号
     */
    @ApiModelProperty(value = "微信退款单号")
    private String refund_id;

    /**
     * String(64)
     * 商户退款单号
     */
    @ApiModelProperty(value = "商户退款单号")
    private String out_refund_no;

    /**
     * 订单总金额，单位为分，只能为整数，详见支付金额
     */
    @ApiModelProperty(value = "订单金额")
    private Integer total_fee;

    /**
     * 当该订单有使用非充值券时，返回此字段。应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
     */
    @ApiModelProperty(value = "应结订单金额")
    private Integer settlement_total_fee;

    /**
     * 退款总金额,单位为分
     */
    @ApiModelProperty(value = "申请退款金额")
    private Integer refund_fee;

    /**
     * 退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private Integer settlement_refund_fee;

    /**
     * String(16)
     * SUCCESS-退款成功
     * CHANGE-退款异常
     * REFUNDCLOSE—退款关闭
     */
    @ApiModelProperty(value = "退款状态")
    private String refund_status;

    /**
     * String(20)
     * 资金退款至用户帐号的时间，格式2017-12-15 09:46:01
     */
    @ApiModelProperty(value = "退款成功时间")
    private String success_time;

    /**
     * String(64)
     * 1）退回银行卡： {银行名称}{卡类型}{卡尾号}
     * 2）退回支付用户零钱: 支付用户零钱
     * 3）退还商户: 商户基本账户 商户结算银行账户
     * 4）退回支付用户零钱通: 支付用户零钱通
     */
    @ApiModelProperty(value = "退款入账账户")
    private String refund_recv_accout;

    /**
     * String(30)
     * REFUND_SOURCE_RECHARGE_FUNDS 可用余额退款/基本账户
     * REFUND_SOURCE_UNSETTLED_FUNDS 未结算资金退款
     */
    @ApiModelProperty(value = "退款资金来源")
    private String refund_account;

    /**
     * String(30)
     * VENDOR_PLATFORM商户平台
     */
    @ApiModelProperty(value = "退款发起来源")
    private String refund_request_source;

//    public static void main(String[] args) {
//        Field[] fields = WechatRefundNotifyDetailResponse.class.getDeclaredFields();
//        for (Field field : fields) {
//            String name = field.getName();
//            name = name.substring(0,1).toUpperCase() + name.substring(1);
//            System.out.println("temp.set"+name+"(map.get(\""+field.getName()+"\"));");
//        }
//    }

    public static WechatRefundNotifyDetailResponse mapToBean(Map<String,String> map) {
        WechatRefundNotifyDetailResponse temp = new WechatRefundNotifyDetailResponse();
        temp.setTransaction_id(map.get("transaction_id"));
        temp.setOut_trade_no(map.get("out_trade_no"));
        temp.setRefund_id(map.get("refund_id"));
        temp.setOut_refund_no(map.get("out_refund_no"));
        temp.setTotal_fee(map.containsKey("total_fee") ? Integer.valueOf(map.get("total_fee")) : null);
        temp.setSettlement_total_fee(map.containsKey("settlement_total_fee") ? Integer.valueOf(map.get("settlement_total_fee")) : null);
        temp.setRefund_fee(map.containsKey("refund_fee") ? Integer.valueOf(map.get("refund_fee")) : null);
        temp.setSettlement_refund_fee(map.containsKey("settlement_refund_fee") ? Integer.valueOf(map.get("settlement_refund_fee")) : null);
        temp.setRefund_status(map.get("refund_status"));
        temp.setSuccess_time(map.get("success_time"));
        temp.setRefund_recv_accout(map.get("refund_recv_accout"));
        temp.setRefund_account(map.get("refund_account"));
        temp.setRefund_request_source(map.get("refund_request_source"));
        return temp;


    }


}
