package top.keiskeiframework.common.vo.charts.series;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.vo.charts.Series;
import top.keiskeiframework.common.vo.charts.series.data.RadarSeriesData;

import java.io.Serializable;
import java.util.List;

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
public class RadarSeries extends Series implements Serializable {
    private static final long serialVersionUID = 5295284467232758863L;
    private String type = "radar";
    private Integer symbolSize = Integer.MIN_VALUE;
    private AreaStyle areaStyle;
    private List<RadarSeriesData> data;

    @Data
    public static class AreaStyle implements Serializable{
        private static final long serialVersionUID = 1996002094691670173L;

        @Data
        public static class Normal implements Serializable {
            private static final long serialVersionUID = 4745956789202888771L;
            private Integer shadowBlur;
            private String shadowColor;
            private Integer shadowOffsetX;
            private Integer shadowOffsetY;
            private Integer opacity;
        }
    }
}
