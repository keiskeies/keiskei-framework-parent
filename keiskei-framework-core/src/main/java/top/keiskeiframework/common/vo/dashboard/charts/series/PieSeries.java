package top.keiskeiframework.common.vo.dashboard.charts.series;

import lombok.*;
import top.keiskeiframework.common.vo.dashboard.charts.Series;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 饼图 series数据
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
    /**
     * 数据
     */
    @NonNull
    private Collection<PieSeriesData> data;

    /**
     * 饼图数据节点
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PieSeriesData implements Serializable {

        private static final long serialVersionUID = -732244914968958590L;
        private String name;
        private Number value;
    }

}
