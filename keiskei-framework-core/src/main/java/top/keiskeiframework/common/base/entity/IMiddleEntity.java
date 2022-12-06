package top.keiskeiframework.common.base.entity;

import java.io.Serializable;

/**
 * <p>
 * 中间关系表
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 20:17
 */
public interface IMiddleEntity<ID1 extends Serializable, ID2 extends Serializable> extends IBaseEntity<String> {

    String MIDDLE_SPLIT = "-";

    /**
     * ID1
     *
     * @return 。
     */
    ID1 getId1();

    /**
     * ID1
     *
     * @param id1 id1
     */
    void setId1(ID1 id1);

    /**
     * ID2
     *
     * @return 。
     */
    ID2 getId2();

    /**
     * ID2
     *
     * @param id2 id2
     */
    void setId2(ID2 id2);

    /**
     * 获取ID
     *
     * @return id
     */
    @Override
    default String getId() {
        return getId1() + MIDDLE_SPLIT + getId2();
    }

}
