package top.keiskeiframework.common.base.entity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 树形结构实体类
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

    /**
     * 上级ID
     *
     * @param parentId 。
     */
    void setParentId(ID parentId);

    /**
     * 层级标识
     *
     * @return 。
     */
    String getSign();

    /**
     * 层级标识
     *
     * @param sign 。
     */
    void setSign(String sign);

    /**
     * 子集
     *
     * @return 。
     */
    List<? extends ITreeEntity<ID>> getChildren();

    /**
     * 子集
     *
     * @param children 。
     */
    void setChildren(List<? extends ITreeEntity<ID>> children);
}
