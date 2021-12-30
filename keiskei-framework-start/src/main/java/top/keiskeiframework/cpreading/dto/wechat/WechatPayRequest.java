package top.keiskeiframework.cpreading.dto.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 *
 * </p>
 * @since 2020/11/1 15:19
 */
@Data
public class WechatPayRequest {


    @ApiModelProperty(value = "公众账号ID", dataType = "String", required = true)
    private String appid; // length: 32; wxd678efh567hg6787	微信支付分配的公众账号ID（企业号corpid即为此appId）

    @ApiModelProperty(value = "商户号", dataType = "String", required = true)
    private String mch_id; // length: 32; 1230000109	微信支付分配的商户号

    @ApiModelProperty(value = "设备号", dataType = "String", required = false)
    private String device_info; // length: 32; 013467007045764	自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"

    @ApiModelProperty(value = "随机字符串", dataType = "String", required = true)
    private String nonce_str; // length: 32; 5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，长度要求在32位以内。推荐随机数生成算法

    @ApiModelProperty(value = "签名", dataType = "String", required = true)
    private String sign; // length: 32; C380BEC2BFD727A4B6845133519F3AD6	通过签名算法计算得出的签名值，详见签名生成算法

    @ApiModelProperty(value = "签名类型", dataType = "String", required = false)
    private String sign_type; // length: 32; MD5	签名类型，默认为MD5，支持HMAC-SHA256和MD5。

    @ApiModelProperty(value = "商品描述", dataType = "String", required = true)
    private String body; // length: 128; 腾讯充值中心-QQ会员充值 商品简单描述，该字段请按照规范传递，具体请见参数规定

    @ApiModelProperty(value = "商品详情", dataType = "String", required = false)
    private String detail; // length: 6000;  	商品详细描述，对于使用单品优惠的商户，该字段必须按照规范上传，详见“单品优惠参数说明”

    @ApiModelProperty(value = "附加数据", dataType = "String", required = false)
    private String attach; // length: 127; 深圳分店	附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。

    @ApiModelProperty(value = "商户订单号", dataType = "String", required = true)
    private String out_trade_no; // length: 32; 20150806125346	商户系统内部订单号，要求32个字符内，只能true数字、大小写字母_-|* 且在同一个商户号下唯一。详见商户订单号

    @ApiModelProperty(value = "标价币种", dataType = "String", required = false)
    private String fee_type; // length: 16; CNY	符合ISO 4217标准的三位字母代码，默认人民币：CNY，详细列表请参见货币类型

    @ApiModelProperty(value = "标价金额", dataType = "Integer", required = true)
    private Integer total_fee;// 88订单总金额，单位为分，详见支付金额

    @ApiModelProperty(value = "终端IP", dataType = "String", required = true)
    private String spbill_create_ip; // length: 64; 123.12.12.123	支持IPV4和IPV6两种格式的IP地址。用户的客户端IP

    @ApiModelProperty(value = "交易起始时间", dataType = "String", required = false)
    private String time_start; // length: 14; 20091225091010	订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则

    /**
     * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间true针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。其他详见时间规则
     *
     *     time_expire只能第一次下单传值，不允许二次修改，二次修改系统将报错。如用户支付失败后，需再次支付，需更换原订单号重新下单。
     *     建议：最短失效时间间隔大于1分钟
     */
    @ApiModelProperty(value = "交易结束时间", dataType = "String", required = false)
    private String time_expire; // length: 14; 20091227091010

    @ApiModelProperty(value = "订单优惠标记", dataType = "String", required = false)
    private String goods_tag; // length: 32; WXG	订单优惠标记，使用代金券或立减优惠功能时需要的参数，说明详见代金券或立减优惠

    @ApiModelProperty(value = "通知地址", dataType = "String", required = true)
    private String notify_url; // length: 256; http://www.weixin.qq.com/wxpay/pay.php	异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。

    /**
     * JSAPI -JSAPI支付
     *
     *     NATIVE -Native支付
     *
     *     APP -
     *     APP支付
     */
    @ApiModelProperty(value = "交易类型", dataType = "String", required = true)
    private String trade_type; // length: 16; JSAPI

    @ApiModelProperty(value = "商品ID", dataType = "String", required = false)
    private String product_id; // length: 32; 12235413214070356458058	trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义。

    @ApiModelProperty(value = "指定支付方式", dataType = "String", required = false)
    private String limit_pay; // length: 32; no_credit	上传此参数no_credit--可限制用户不能使用信用卡支付

    @ApiModelProperty(value = "用户标识", dataType = "String", required = false)
    private String openid; // length: 128; oUpF8uMuAJO_M2pxb1Q9zNjWeS6o	trade_type=JSAPI时（即JSAPI支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。openid如何获取，可参考【获取openid】。企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换

    @ApiModelProperty(value = "电子发票入口开放标识", dataType = "String", required = false)
    private String receipt; // length: 8; Y	Y，传入Y时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效

    /**
     * 该字段常用于线下活动时的场景信息上报，支持上报实际门店信息，商户也可以按需求自己上报相关信息。该字段为JSON对象数据，
     *
     *     对象格式为 {
     *         "store_info":{
     *             "id":"门店ID", "name":"名称", "area_code":"编码", "address":"地址"
     *         }
     *     }
     */
    @ApiModelProperty(value = "场景信息", dataType = "String", required = false)
    private String scene_info; // length: 256;


    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        if (!StringUtils.isEmpty(this.appid)) {map.put("appid",appid);}
        if (!StringUtils.isEmpty(this.mch_id)) {map.put("mch_id",mch_id);}
        if (!StringUtils.isEmpty(this.device_info)) {map.put("device_info",device_info);}
        if (!StringUtils.isEmpty(this.nonce_str)) {map.put("nonce_str",nonce_str);}
        if (!StringUtils.isEmpty(this.sign)) {map.put("sign",sign);}
        if (!StringUtils.isEmpty(this.sign_type)) {map.put("sign_type",sign_type);}
        if (!StringUtils.isEmpty(this.body)) {map.put("body",body);}
        if (!StringUtils.isEmpty(this.detail)) {map.put("detail",detail);}
        if (!StringUtils.isEmpty(this.attach)) {map.put("attach",attach);}
        if (!StringUtils.isEmpty(this.out_trade_no)) {map.put("out_trade_no",out_trade_no);}
        if (!StringUtils.isEmpty(this.fee_type)) {map.put("fee_type",fee_type);}
        if (null != total_fee) {map.put("total_fee",total_fee + "");}
        if (!StringUtils.isEmpty(this.spbill_create_ip)) {map.put("spbill_create_ip",spbill_create_ip);}
        if (!StringUtils.isEmpty(this.time_start)) {map.put("time_start",time_start);}
        if (!StringUtils.isEmpty(this.time_expire)) {map.put("time_expire",time_expire);}
        if (!StringUtils.isEmpty(this.goods_tag)) {map.put("goods_tag",goods_tag);}
        if (!StringUtils.isEmpty(this.notify_url)) {map.put("notify_url",notify_url);}
        if (!StringUtils.isEmpty(this.trade_type)) {map.put("trade_type",trade_type);}
        if (!StringUtils.isEmpty(this.product_id)) {map.put("product_id",product_id);}
        if (!StringUtils.isEmpty(this.limit_pay)) {map.put("limit_pay",limit_pay);}
        if (!StringUtils.isEmpty(this.openid)) {map.put("openid",openid);}
        if (!StringUtils.isEmpty(this.receipt)) {map.put("receipt",receipt);}
        if (!StringUtils.isEmpty(this.scene_info)) {map.put("scene_info",scene_info);}
        return map;
    }



}
