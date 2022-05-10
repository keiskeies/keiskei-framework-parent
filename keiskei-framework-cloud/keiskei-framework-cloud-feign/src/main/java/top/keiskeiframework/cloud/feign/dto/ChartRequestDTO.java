package top.keiskeiframework.cloud.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.keiskeiframework.cloud.feign.enums.CalcType;
import top.keiskeiframework.cloud.feign.enums.ChartType;
import top.keiskeiframework.cloud.feign.enums.ColumnType;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图表参数
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartRequestDTO implements Serializable {
    private static final long serialVersionUID = -1354993776756493818L;

    /**
     * 字段
     */
    private String column;

    /**
     * 创建时间字段
     */
    private String timeField;
    /**
     * 实体类名称
     */
    private String entityName;
    /**
     * 图表类型
     */
    private ChartType chartType;

    /**
     * 字段类型
     */
    private ColumnType columnType;
    /**
     * 时间间隔
     */
    private TimeDeltaEnum timeDelta;
    /**
     * 起始时间
     */
    private LocalDateTime start;
    /**
     * 结束时间
     */
    private LocalDateTime end;

    /**
     * 计算方式
     */
    private CalcType calcType = CalcType.COUNT;

    /**
     * 查询条件
     * key  字段
     * value  范围
     */
    private Map<String, List<String>> conditions;

    public ChartRequestDTO(ChartType chartType, ColumnType columnType, LocalDateTime start, LocalDateTime end) {
        this.chartType = chartType;
        this.columnType = columnType;
        this.start = start;
        this.end = end;

    }


}
