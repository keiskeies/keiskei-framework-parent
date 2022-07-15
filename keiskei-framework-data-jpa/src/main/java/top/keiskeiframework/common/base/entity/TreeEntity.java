package top.keiskeiframework.common.base.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;
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
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class TreeEntity<ID extends Serializable> extends ListEntity<ID> implements ITreeEntity<ID> {
    private static final long serialVersionUID = -802579500126524571L;

    protected ID parentId;

    protected String sign;

    @Transient
    protected List<? extends ITreeEntity<ID>> children;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        TreeEntity<?> that = (TreeEntity<?>) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
