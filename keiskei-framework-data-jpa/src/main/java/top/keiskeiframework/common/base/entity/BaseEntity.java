package top.keiskeiframework.common.base.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * <p>
 * 基础实体类
 * </p>
 *
 * @param <ID> .
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午5:12:51
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity<ID extends Serializable> implements IBaseEntity<ID> {

    private static final long serialVersionUID = 2575319892827923898L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected ID id;


    protected Boolean d;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        BaseEntity<?> that = (BaseEntity<?>) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
