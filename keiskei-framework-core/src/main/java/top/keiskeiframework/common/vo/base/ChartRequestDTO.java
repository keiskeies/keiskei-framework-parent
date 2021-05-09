package top.keiskeiframework.common.vo.base;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:37
 */
@Data
public class ChartRequestDTO implements Serializable {
    private static final long serialVersionUID = -1354993776756493818L;

    private String column;
    private String chartType;
    private Boolean yHorizontal = Boolean.FALSE;
    private ColumnType columnType;
    private ChronoUnit unit;
    private LocalDateTime start;
    private LocalDateTime end;

    public enum ColumnType {
        // 时间类型
        TIME,
        // 字段分类
        FIELD
    }
}
