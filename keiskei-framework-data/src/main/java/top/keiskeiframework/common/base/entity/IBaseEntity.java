package top.keiskeiframework.common.base.entity;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 20:16
 */
public interface IBaseEntity<ID extends Serializable> extends Serializable {

    /**
     * ID
     * @return id
     */
    ID getId();
    void setId(ID id);
}
