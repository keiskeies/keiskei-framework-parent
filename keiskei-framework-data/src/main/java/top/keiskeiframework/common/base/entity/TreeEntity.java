package top.keiskeiframework.common.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 基础实体类
 * </p>
 *
 * @param <ID> .
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午5:12:51
 */
@EqualsAndHashCode(callSuper = false)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TreeEntity<ID extends Serializable> extends ListEntity<ID> implements Serializable {
    private static final long serialVersionUID = -802579500126524571L;

    protected ID parentId;

    protected String sign;

    @Transient
    protected List<? extends TreeEntity<ID>> children;

}
