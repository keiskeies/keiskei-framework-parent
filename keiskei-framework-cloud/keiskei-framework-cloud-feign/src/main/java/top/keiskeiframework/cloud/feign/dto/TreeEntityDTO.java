package top.keiskeiframework.cloud.feign.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class TreeEntityDTO<ID extends Serializable> extends ListEntityDTO<ID> implements Serializable {
    private static final long serialVersionUID = -802579500126524571L;

    protected ID parentId;
    protected String sign;
    protected List<? extends TreeEntityDTO<ID>> children;

}
