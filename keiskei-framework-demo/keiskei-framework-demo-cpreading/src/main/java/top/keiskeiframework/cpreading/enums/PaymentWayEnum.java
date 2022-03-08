package top.keiskeiframework.cpreading.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付方式
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年6月4日 下午2:39:29
 */
@Getter
@AllArgsConstructor
public enum PaymentWayEnum {
    //
    JSAPI(1, "JSAPI支付（或小程序支付）", "JSAPI", "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&code=%s&grant_type=authorization_code"),
    NATIVE(2, "NATIVE", "NATIVE", ""),
    APP(3, "APP", "", ""),
    MWEB(2, "H5支付", "MWEB", "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code"),
    ;

    public static String getUrl(Integer appType, String appId, String secret, String code) {
        for (PaymentWayEnum appTypeEnum : PaymentWayEnum.values()) {
            if (appTypeEnum.getCode().equals(appType)) {
                return String.format(appTypeEnum.getUrlFormat(), appId, secret, code);
            }
        }
        return null;
    }

    private final Integer code;
    private final String name;
    private final String type;
    private final String urlFormat;


}
