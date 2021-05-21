package top.keiskeiframework.common.vo.dashboard.charts;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 图表数据VO
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 21:00
 */
@Data
public class ChartOptionVO implements Serializable {
    private static final long serialVersionUID = -2401179965618911915L;
    /**
     * xy坐标翻转
     */
    private Boolean horizontal = Boolean.FALSE;
    /**
     * x坐标
     */
    private Axis axis;
    /**
     * series数据
     */
    private List<Series> series;
    /**
     * 数据指示器
     */
    private Legend legend;
    /**
     * 雷达图坐标
     */
    private Radar radar;
    /**
     * 图表标题
     */
    private Title title;
}
