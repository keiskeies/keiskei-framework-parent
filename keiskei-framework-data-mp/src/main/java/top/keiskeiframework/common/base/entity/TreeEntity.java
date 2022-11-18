package top.keiskeiframework.common.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 基础实体类
 * </p>
 *
 * @param <ID> .
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午5:12:51
 */
@EqualsAndHashCode(callSuper = false)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class TreeEntity<ID extends Serializable> extends ListEntity<ID> implements ITreeEntity<ID> {
    private static final long serialVersionUID = -802579500126524571L;

    protected ID parentId;

    protected String sign;

    protected transient List<? extends ITreeEntity<ID>> children;

}
