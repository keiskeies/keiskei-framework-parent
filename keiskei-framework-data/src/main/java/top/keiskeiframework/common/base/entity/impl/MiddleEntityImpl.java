package top.keiskeiframework.common.base.entity.impl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.keiskeiframework.common.base.entity.IMiddleEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen
 * @since 2022/11/19 17:09
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class MiddleEntityImpl<ID1 extends Serializable, ID2 extends Serializable> implements IMiddleEntity<ID1, ID2> {

    @Id
    @TableId(type = IdType.INPUT)
    @Column(length = 64)
    protected String id;
    protected ID1 id1;
    protected ID2 id2;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        MiddleEntityImpl<?, ?> that = (MiddleEntityImpl<?, ?>) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
