package top.keiskeiframework.common.vo.charts.series;

import lombok.*;
import top.keiskeiframework.common.vo.charts.Series;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 折线图/柱状图 series 数据
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 21:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class LineOrBarSeries extends Series implements Serializable {
    private static final long serialVersionUID = -5085259754180445771L;
    /**
     * 数据
     */
    @NonNull
    private Collection<Number> data;
}
