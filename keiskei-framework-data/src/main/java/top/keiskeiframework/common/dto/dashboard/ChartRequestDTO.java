package top.keiskeiframework.common.dto.dashboard;

import lombok.Data;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ChartType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
    private CalcType calcType;

    /**
     * 求和字段
     */
    private String sumColumn;

    /**
     * 查询条件
     * key  字段
     * value  范围
     */
    private String conditions;

    public ChartRequestDTO () {
        this.calcType = CalcType.COUNT;
    }


}
