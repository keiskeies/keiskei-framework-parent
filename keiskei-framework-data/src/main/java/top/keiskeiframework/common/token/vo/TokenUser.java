package top.keiskeiframework.common.token.vo;

import lombok.Data;

import java.util.Collection;

/**
 * <p>
 * 管理员信息
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/10 18:19
 */
@Data
public class TokenUser {

    private static final long serialVersionUID = 6452938173658688276L;

    private Long id;
    private String name;
    private String avatar;
    private String phone;
    private String email;
    private String username;
    private String password;

    private String token;

    private Collection<TokenGrantedAuthority> authorities;
    /**
     * 账号是否未过期
     */
    private boolean accountNonExpired = true;
    /**
     * 账号是否未被锁定
     */
    private boolean accountNonLocked = true;
    /**
     * 用户凭证是否未过期
     */
    private boolean credentialsNonExpired = true;
    /**
     * 账号是否可用
     */
    private boolean enabled = true;


}
