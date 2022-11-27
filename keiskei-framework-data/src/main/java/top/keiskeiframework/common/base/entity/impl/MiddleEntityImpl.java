package top.keiskeiframework.common.base.entity.impl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.keiskeiframework.common.base.entity.IMiddleEntity;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen
 * @since 2022/11/19 17:09
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class MiddleEntityImpl<ID1 extends Serializable, ID2 extends Serializable> implements IMiddleEntity<ID1, ID2> {

    private static final long serialVersionUID = 215281170462638537L;
    @Id
    @TableId(type = IdType.INPUT)
    @Column(length = 64)
    protected String id;
    protected ID1 id1;
    protected ID2 id2;

    @Override
    public String getId() {
        return id1 + MIDDLE_SPLIT + id2;
    }
}
