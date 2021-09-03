package top.keiskeiframework.common.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.util.SecurityUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
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
public class TreeStringIdEntity extends TreeEntity<String> {

    @Id
    @Column(length = 32)
    protected String id;

    @Override
    protected void onCreate() {
        id = UUID.randomUUID().toString().replace("-", "");
        super.onCreate();
    }

}
