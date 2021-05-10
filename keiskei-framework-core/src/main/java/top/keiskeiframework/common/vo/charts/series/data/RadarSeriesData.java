package top.keiskeiframework.common.vo.charts.series.data;

import lombok.*;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 21:51
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class RadarSeriesData implements Serializable {

    private static final long serialVersionUID = 4252943431260003924L;
    private String name;

    @NonNull
    private Collection<?> value;
}
