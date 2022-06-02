package top.keiskeiframework.common.base.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class TreeEntity<ID extends Serializable> extends ListEntity<ID> implements ITreeEntity<ID> {
    private static final long serialVersionUID = -802579500126524571L;

    protected ID parentId;

    protected String sign;

    protected transient List<? extends ITreeEntity<ID>> children;

}
