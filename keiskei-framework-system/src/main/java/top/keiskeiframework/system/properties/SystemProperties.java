package top.keiskeiframework.system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.keiskeiframework.system.util.ResponseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户token相关数据
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/8/10 13:59
 */
@Component
@ConfigurationProperties(prefix = "keiskei")
@Data
public class SystemProperties {

    /**
     * 不进行拦截路径
     */
    private String[] permitUri;

    /**
     * 是否允许跨域
     */
    private Boolean cross = Boolean.FALSE;

    public void setCross(Boolean cross) {
        this.cross = cross;
        ResponseUtils.cross = cross;
    }

    /**
     * 登陆后可访问的路径
     */
    private List<AuthenticateUrl> authenticatedUrls = new ArrayList<>();

    /**
     * 管理员登录URL
     */
    private String authWebLoginPath = "/admin/v1/system/login";

    /**
     * 管理员退出URL
     */
    private String authWebLogoutPath = "/admin/v1/system/logout";

    /**
     * token有效时长
     */
    private Integer tokenMinutes = 2 * 60;
    /**
     * token加密
     */
    private String tokenSecret = "secret";

    /**
     * 最大同时在线人数
     */
    private Integer maximumSessions = 1;

    /**
     * remember-me 最大时长
     */
    private Integer rememberSeconds = 2 * 7 * 24 * 60 * 60;

    /**
     * 密码错误锁定分钟数
     */
    private Long lockMinutes = 10L;

}
