package top.keiskeiframework.cloud.core.vo;

import lombok.Data;

/**
 * <p>
 * 用户基础信息
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/10 18:19
 */
@Data
public class TokenUser {

    private static final long serialVersionUID = 6452938173658688276L;

    private Integer id;
    private String username;
    private String nickName;
    private String department;


}
