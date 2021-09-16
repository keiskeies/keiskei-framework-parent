package top.keiskeiframework.common.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.keiskeiframework.common.util.SecurityUtils;

import javax.persistence.*;
import java.util.UUID;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/3 01:15
 */
@EqualsAndHashCode(callSuper = false)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseStringIdEntity extends SuperEntity<String> {

    @Id
    @Column(length = 32)
    protected String id;

    @Override
    protected void onCreate() {
        id = UUID.randomUUID().toString().replace("-", "");
        super.onCreate();
    }

}
