package top.keiskeiframework.common.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/12 19:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MiddleEntity<ID1 extends Serializable, ID2 extends Serializable> extends BaseEntity<String> implements IMiddleEntity<ID1, ID2> {
    private static final long serialVersionUID = -212868740901553952L;

    @TableId(type = IdType.INPUT)
    private String id;

    private ID1 id1;
    private ID2 id2;

}
