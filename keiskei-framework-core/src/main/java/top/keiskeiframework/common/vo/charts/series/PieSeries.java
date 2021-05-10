package top.keiskeiframework.common.vo.charts.series;

import lombok.*;
import top.keiskeiframework.common.vo.charts.Series;
import top.keiskeiframework.common.vo.charts.series.data.PieSeriesData;

import java.io.Serializable;
import java.util.Collection;

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
@NoArgsConstructor
@RequiredArgsConstructor
public class PieSeries extends Series implements Serializable {
    private static final long serialVersionUID = 7755013680905254636L;
    private Integer[] radius = {15, 95};
    private String[] center = {"50%", "38%"};

    @NonNull
    private Collection<PieSeriesData> data;

}
