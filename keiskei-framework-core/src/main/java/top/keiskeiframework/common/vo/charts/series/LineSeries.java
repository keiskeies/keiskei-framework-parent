package top.keiskeiframework.common.vo.charts.series;

import lombok.*;
import top.keiskeiframework.common.vo.charts.Series;
import top.keiskeiframework.common.vo.charts.series.style.ItemStyle;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 21:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class LineSeries extends Series implements Serializable {
    private static final long serialVersionUID = -5085259754180445771L;
    private Boolean smooth = Boolean.TRUE;
    private ItemStyle itemStyle;
    private String type = "line";
    @NonNull
    private Collection<Number> data;
}
