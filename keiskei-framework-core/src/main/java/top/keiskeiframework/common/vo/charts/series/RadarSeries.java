package top.keiskeiframework.common.vo.charts.series;

import lombok.*;
import top.keiskeiframework.common.vo.charts.Series;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 *
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

    @NonNull
    private Collection<RadarSeriesData> data;

    @Data
    @NoArgsConstructor
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class RadarSeriesData implements Serializable {

        private static final long serialVersionUID = 4252943431260003924L;
        private String name;

        @NonNull
        private Collection<?> value;
    }
}
