package top.keiskeiframework.common.base.entity.impl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.keiskeiframework.common.base.entity.IBaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen
 * @since 2022/11/19 17:01
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntityImpl<ID extends Serializable> implements IBaseEntity<ID> {

    private static final long serialVersionUID = -531829598114928379L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    protected ID id;
}
