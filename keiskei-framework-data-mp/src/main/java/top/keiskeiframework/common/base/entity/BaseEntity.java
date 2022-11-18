package top.keiskeiframework.common.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/12 19:45
 */
@Data
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity<ID extends Serializable> implements IBaseEntity<ID> {

    private static final long serialVersionUID = -7639279182532766702L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected ID id;

    /**
     * 删除标记
     * 数据初始化来源 {@link top.keiskeiframework.common.config.MyMetaObjectHandler}
     */
    @TableField(fill = FieldFill.INSERT)
    protected Integer d;
}
