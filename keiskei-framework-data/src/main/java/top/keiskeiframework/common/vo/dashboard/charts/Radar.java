package top.keiskeiframework.common.vo.dashboard.charts;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 雷达图坐标数据信息
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Radar implements Serializable {
    private static final Long serialVersionUID = 9065801904058866173L;
    /**
     * 雷达坐标
     */
    private List<Indicator> indicator;

    /**
     * 雷达图坐标节点
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Indicator implements Serializable {
        private static final Long serialVersionUID = -7065029137670618519L;
        /**
         * 坐标名称
         */
        private String name;
        /**
         * 坐标边界值
         */
        private Integer max;
    }
}
