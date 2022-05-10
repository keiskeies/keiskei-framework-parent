package top.keiskeiframework.system.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

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
public class TokenUser implements UserDetails {

    private static final long serialVersionUID = 6452938173658688276L;

    private Integer id;
    private String name;
    private String avatar;
    private String phone;
    private String email;
    private String username;
    /**
     * 用户角色
     */
    private Collection<TokenGrantedAuthority> authorities;

    /**
     * 密码
     */
    private String password;

    /**
     * 所属部门
     */
    @JsonIgnore
    private Integer departmentId;
    /**
     * 部门标识
     */
    @JsonIgnore
    private String department;
    /**
     * 账号是否未过期
     */
    @JsonIgnore
    private boolean accountNonExpired = true;
    /**
     * 账号是否未被锁定
     */
    @JsonIgnore
    private boolean accountNonLocked = true;
    /**
     * 用户凭证是否未过期
     */
    @JsonIgnore
    private boolean credentialsNonExpired = true;
    /**
     * 账号是否可用
     */
    @JsonIgnore
    private boolean enabled = true;


}
