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
 * @since 2021/5/9 21:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class LineOrBarSeries extends Series implements Serializable {
    private static final long serialVersionUID = -5085259754180445771L;

    @NonNull
    private Collection<Number> data;
}
