package top.keiskeiframework.cpreading.wechart.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 微信app类型枚举
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/12/20 14:32
 */
@Getter
@AllArgsConstructor
public enum AppTypeEnum {
    //

    MIN_APP(1, "小程序",
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
            "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
            "https://api.weixin.qq.com/wxa/getpaidunionid?access_token=%s&openid=%s"),
    H5(2, "H5",
            "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
            "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
            "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s"),
    PC(3, "pc网站",
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
            "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
            "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s"),
    ;


    public static String getUrl(Integer appType, String appId, String secret, String code) {
        for (AppTypeEnum appTypeEnum : AppTypeEnum.values()) {
            if (appTypeEnum.getCode().equals(appType)) {
                return String.format(appTypeEnum.getUrlFormat(), appId, secret, code);
            }
        }
        return null;
    }

    public static String getTokenUrl(Integer appType, String appId, String secret) {
        for (AppTypeEnum appTypeEnum : AppTypeEnum.values()) {
            if (appTypeEnum.getCode().equals(appType)) {
                return String.format(appTypeEnum.getAccessTokenUrlFormat(), appId, secret);
            }
        }
        return null;
    }

    public static String getUserInfoUrl(Integer appType, String accessToken, String openid) {
        for (AppTypeEnum appTypeEnum : AppTypeEnum.values()) {
            if (appTypeEnum.getCode().equals(appType)) {
                return String.format(appTypeEnum.getUserInfoUrlFormat(), accessToken, openid);
            }
        }
        return null;
    }

    private final Integer code;
    private final String message;
    private final String urlFormat;
    private final String accessTokenUrlFormat;
    private final String userInfoUrlFormat;
}
