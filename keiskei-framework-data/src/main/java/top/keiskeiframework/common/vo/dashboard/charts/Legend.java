package top.keiskeiframework.common.vo.dashboard.charts;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 数据指示器
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Legend implements Serializable {
    private static final long serialVersionUID = -194763321705309878L;
    /**
     * 数据各类型
     */
    private List<String> data;




}
