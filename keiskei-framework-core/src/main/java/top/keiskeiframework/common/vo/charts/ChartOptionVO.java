package top.keiskeiframework.common.vo.charts;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 21:00
 */
@Data
public class ChartOptionVO implements Serializable {
    private static final long serialVersionUID = -2401179965618911915L;
    private Axis xAxis;
    private Axis yAxis;
    private List<Series> series;
    private Legend legend;
    private Radar radar;
    private Tooltip tooltip;
    private Grid grid;
}
