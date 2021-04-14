package top.keiskeiframework.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/10 20:04
 */
@Getter
@AllArgsConstructor
public enum OperateTypeEnum {

    //
    LOGIN("login","登录"),
    LOGOUT("logout","登出"),
    SAVE("post","添加"),
    UPDATE("put","更新"),
    UPDATE_PART("patch","更新部分数据"),
    DELETE("delete","删除"),
    GET("get","查询"),

    ;

    private final String method;
    private final String type;

    public static String getType(String method) {
        if (StringUtils.isEmpty(method)) {
            return null;
        }
        for (OperateTypeEnum operateTypeEnum:OperateTypeEnum.values()) {
            if (operateTypeEnum.getMethod().equalsIgnoreCase(method)) {
                return operateTypeEnum.getType();
            }
        }
        return null;
    }
}
