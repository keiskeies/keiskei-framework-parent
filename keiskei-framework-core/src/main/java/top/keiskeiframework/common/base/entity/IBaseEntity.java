package top.keiskeiframework.common.base.entity;

import java.io.Serializable;

/**
 * <p>
 * 基础实体类接口
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 20:16
 */
public interface IBaseEntity<ID extends Serializable> extends Serializable {

    /**
     * ID
     *
     * @return id
     */
    ID getId();

    /**
     * ID
     *
     * @param id id
     */
    void setId(ID id);
}
