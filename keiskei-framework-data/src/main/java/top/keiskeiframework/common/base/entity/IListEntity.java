package top.keiskeiframework.common.base.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
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
    void setP(String p);

    /**
     * 创建人
     *
     * @return 。
     */
    ID getCreateUserId();
    void setCreateUserId(ID createUserId);

    /**
     * 创建时间
     *
     * @return 。
     */
    LocalDateTime getCreateTime();
    void setCreateTime(LocalDateTime createTime);

    /**
     * 更新人
     *
     * @return 。
     */
    ID getUpdateUserId();
    void setUpdateUserId(ID updateUserId);

    /**
     * 更新时间
     *
     * @return 。
     */
    LocalDateTime getUpdateTime();
    void setUpdateTime(LocalDateTime updateTime);
}
