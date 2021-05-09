package top.keiskeiframework.common.vo.charts.series;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.vo.charts.Series;
import top.keiskeiframework.common.vo.charts.series.data.PieSeriesData;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 21:46
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PieSeries extends Series implements Serializable {
    private static final long serialVersionUID = 7755013680905254636L;
    private Integer[] radius = {15, 95};
    private String[] center = {"50%", "38%"};
    private List<PieSeriesData> data;

}
