package top.keiskeiframework.common.vo.charts.series.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 21:51
 */
@Data
public class RadarSeriesData implements Serializable {

    private static final long serialVersionUID = 4252943431260003924L;
    private List<Number> value;
    private String name;
}
