package top.keiskeiframework.common.token.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 管理员权限
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/10 18:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenGrantedAuthority {

    private static final long serialVersionUID = -7873909088341560213L;

    private Long id;
    private String authority;
}
