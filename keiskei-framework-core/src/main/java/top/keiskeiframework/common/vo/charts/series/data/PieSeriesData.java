package top.keiskeiframework.common.vo.charts.series.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 21:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PieSeriesData implements Serializable {

    private static final long serialVersionUID = -732244914968958590L;
    private String name;
    private Number value;
}
