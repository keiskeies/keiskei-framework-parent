package top.keiskeiframework.common.vo.charts.series;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.vo.charts.Series;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BarSeries extends Series implements Serializable {
    private static final long serialVersionUID = -6477940205972847924L;

    private String type = "bar";
    private String stack = "vistors";
    private String barWidth = "60%";
    private List<Number> data;

}
