package top.keiskeiframework.common.vo.charts.series.data;

import lombok.Data;

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
public class PieSeriesData implements Serializable {

    private static final long serialVersionUID = -732244914968958590L;
    private Number value;
    private String name;
}
