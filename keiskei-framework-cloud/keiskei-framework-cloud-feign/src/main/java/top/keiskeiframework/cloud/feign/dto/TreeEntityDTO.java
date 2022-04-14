package top.keiskeiframework.cloud.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 基础实体类
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午5:12:51
 */
@EqualsAndHashCode(callSuper = false)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TreeEntityDTO extends ListEntityDTO implements Serializable {
    private static final long serialVersionUID = -802579500126524571L;

    protected Long parentId;
    protected String sign;
    protected List<? extends TreeEntityDTO> children;

}
