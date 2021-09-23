package top.keiskeiframework.common.dto.dashboard;

import lombok.Data;
import top.keiskeiframework.common.enums.dashboard.ChartType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.dashboard.TimeDeltaEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 图表参数
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:37
 */
@Data
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


}
