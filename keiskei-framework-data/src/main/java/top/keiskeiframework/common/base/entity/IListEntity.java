package top.keiskeiframework.common.base.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 列表形式实体类
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 20:17
 */
public interface IListEntity<ID extends Serializable> extends IBaseEntity<ID> {

    /**
     * 部门层级
     *
     * @return 。
     */
    String getP();

    /**
     * 部门层级
     *
     * @param p p
     */
    void setP(String p);

    /**
     * 创建人
     *
     * @return 。
     */
    ID getCreateUserId();

    /**
     * 创建人
     *
     * @param createUserId 。
     */
    void setCreateUserId(ID createUserId);

    /**
     * 创建时间
     *
     * @return 。
     */
    LocalDateTime getCreateTime();

    /**
     * 创建时间
     *
     * @param createTime 。
     */
    void setCreateTime(LocalDateTime createTime);

    /**
     * 更新人
     *
     * @return 。
     */
    ID getUpdateUserId();

    /**
     * 更新人
     *
     * @param updateUserId 。
     */
    void setUpdateUserId(ID updateUserId);

    /**
     * 更新时间
     *
     * @return 。
     */
    LocalDateTime getUpdateTime();

    /**
     * 更新时间
     *
     * @param updateTime 。
     */
    void setUpdateTime(LocalDateTime updateTime);
}
