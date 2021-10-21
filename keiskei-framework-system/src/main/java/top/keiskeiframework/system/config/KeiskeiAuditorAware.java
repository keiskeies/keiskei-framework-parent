package top.keiskeiframework.system.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import top.keiskeiframework.system.entity.User;
import top.keiskeiframework.system.vo.user.TokenUser;

import java.util.Optional;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/10/21 10:53
 */
@Component
public class KeiskeiAuditorAware implements AuditorAware<User> {
    @Override
    public Optional<User> getCurrentAuditor() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(obj instanceof TokenUser)) {
            return Optional.empty();
        }
        TokenUser tokenUser =  (TokenUser) obj;
        User user = User.builder().id(tokenUser.getId()).username(tokenUser.getUsername()).name(tokenUser.getName()).build();
        return Optional.of(user);
    }
}
