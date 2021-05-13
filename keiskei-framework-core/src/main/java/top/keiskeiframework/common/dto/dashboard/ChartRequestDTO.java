package top.keiskeiframework.common.dto.dashboard;

import lombok.*;
import top.keiskeiframework.common.enums.dashboard.ChartType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.dashboard.TimeDeltaEnum;

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
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class ChartRequestDTO implements Serializable {
    private static final long serialVersionUID = -1354993776756493818L;

    private String column;
    private String entityName;
    private ChartType chartType;

    @NonNull
    private ColumnType columnType;
    private TimeDeltaEnum timeDelta;

    @NonNull
    private LocalDateTime start;
    @NonNull
    private LocalDateTime end;


}
