package top.keiskeiframework.common.base.entity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 21:21
 */
public interface ITreeEntity<ID extends Serializable> extends IListEntity<ID> {

    /**
     * 上级ID
     *
     * @return 。
     */
    ID getParentId();
    void setParentId(ID parentId);

    /**
     * 层级标识
     *
     * @return 。
     */
    String getSign();
    void setSign(String sign);

    /**
     * 子集
     *
     * @return 。
     */
    List<? extends ITreeEntity<ID>> getChildren();
    void setChildren(List<? extends ITreeEntity<ID>> children);
}
