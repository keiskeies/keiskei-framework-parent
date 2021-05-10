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
 * @since 2021/5/9 22:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class BarSeries extends Series implements Serializable {
    private static final long serialVersionUID = -6477940205972847924L;

    private String type = "bar";
    private String stack = "vistors";
    private String barWidth = "60%";

    @NonNull
    private Collection<Number> data;

}
