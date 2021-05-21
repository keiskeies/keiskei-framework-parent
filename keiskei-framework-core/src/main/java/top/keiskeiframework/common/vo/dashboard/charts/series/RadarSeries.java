package top.keiskeiframework.common.vo.dashboard.charts.series;

import lombok.*;
import top.keiskeiframework.common.vo.dashboard.charts.Series;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 雷达图 series 数据
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class RadarSeries extends Series implements Serializable {
    private static final long serialVersionUID = 5295284467232758863L;
    /**
     * 数据
     */
    @NonNull
    private Collection<RadarSeriesData> data;

    /**
     * 数据节点
     */
    @Data
    @NoArgsConstructor
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class RadarSeriesData implements Serializable {

        private static final long serialVersionUID = 4252943431260003924L;
        /**
         * 数据分类名称
         */
        private String name;

        /**
         * 数据
         */
        @NonNull
        private Collection<?> value;
    }
}
