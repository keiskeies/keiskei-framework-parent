package top.keiskeiframework.common.vo.charts;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:31
 */
@NoArgsConstructor
@Data
public class Grid implements Serializable {
    private static final long serialVersionUID = 1226748475228704419L;

    private Integer left;
    private Integer right;
    private Integer bottom;
    private Integer top;
    private Boolean containLabel;
}
