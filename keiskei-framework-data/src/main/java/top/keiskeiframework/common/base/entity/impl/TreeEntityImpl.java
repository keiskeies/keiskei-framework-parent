package top.keiskeiframework.common.base.entity.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.keiskeiframework.common.base.entity.ITreeEntity;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen
 * @since 2022/11/19 17:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class TreeEntityImpl<ID extends Serializable> extends ListEntityImpl<ID> implements ITreeEntity<ID> {

    private static final long serialVersionUID = 4960499500142619802L;

    protected ID parentId;

    protected String sign;

    protected transient List<? extends ITreeEntity<ID>> children;
}
