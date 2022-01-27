package top.keiskeiframework.demo.order.enums;

/**
 * <p>
 * 订单状态枚举
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/21 23:38
 */
public enum OrderStatusEnum {
    // 新建等待付款
    WAIT_PAY,

    // 已付款
    PAID,

    // 等待配送
    WART_POST,

    // 已取消
    CANCEL,

    // 已退款
    REFUND,

    // 发起退款
    WAIT_REFUND,

    // 已完成
    COMPLETE,

    // 已关闭
    CLOSE


}
