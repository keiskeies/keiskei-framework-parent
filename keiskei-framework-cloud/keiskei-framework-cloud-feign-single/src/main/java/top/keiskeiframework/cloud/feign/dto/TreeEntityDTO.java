package top.keiskeiframework.cloud.feign.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.base.entity.ITreeEntity;

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
public class TreeEntityDTO<ID extends Serializable> extends ListEntityDTO<ID> implements ITreeEntity<ID> {

    private static final long serialVersionUID = 4960499500142619802L;

    protected ID parentId;
    protected String sign;
    protected transient List<? extends ITreeEntity<ID>> children;
}
