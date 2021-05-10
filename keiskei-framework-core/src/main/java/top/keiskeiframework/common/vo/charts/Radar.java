package top.keiskeiframework.common.vo.charts;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Radar implements Serializable {
    private static final long serialVersionUID = 9065801904058866173L;


    private String radius;
    private List<String> center;
    private Integer splitNumber;
    private SplitArea splitArea;

    @NonNull
    private List<Indicator> indicator;

    @Data
    public static class SplitArea implements Serializable {
        private static final long serialVersionUID = -5033294275488625385L;
        private AreaStyle areaStyle;

        @Data
        public static class AreaStyle implements Serializable {
            private static final long serialVersionUID = -6890243711168870979L;
            private String color;
            private Integer opacity;
            private Integer shadowBlur;
            private String shadowColor;
            private Integer shadowOffsetX;
            private Integer shadowOffsetY;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Indicator implements Serializable {
        private static final long serialVersionUID = -7065029137670618519L;
        private String name;
        private Integer max;
    }
}
