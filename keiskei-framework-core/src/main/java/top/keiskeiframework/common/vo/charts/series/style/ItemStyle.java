package top.keiskeiframework.common.vo.charts.series.style;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:00
 */
@Data
public class ItemStyle implements Serializable {
    private static final long serialVersionUID = 2090419178719739054L;
    private Normal normal;

    @Data
    public static class Normal implements Serializable{
        private static final long serialVersionUID = 5470673672713225552L;

        private String color;
        private LineStyle lineStyle;
        private AreaStyle areaStyle;


        @Data
        public static class LineStyle implements Serializable {
            private static final long serialVersionUID = -3982391761722556170L;
            private String color;
            private Integer width;
        }

        @Data
        public static class AreaStyle implements Serializable {
            private static final long serialVersionUID = 992375576687054784L;
            private String color;
        }
    }
}


