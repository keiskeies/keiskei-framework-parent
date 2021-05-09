package top.keiskeiframework.common.vo.charts;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:20
 */
@Data
public class Legend implements Serializable {
    private static final long serialVersionUID = -194763321705309878L;
    private String left;
    private String bottom;
    private List<String> data;
}
