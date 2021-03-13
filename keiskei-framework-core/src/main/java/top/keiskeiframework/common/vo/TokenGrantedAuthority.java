package top.keiskeiframework.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/10 18:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = -7873909088341560213L;

    private Long id;
    private String authority;
}
