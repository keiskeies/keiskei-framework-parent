package top.keiskeiframework.common.vo.dashboard.charts;

import lombok.*;

/**
 * <p>
 * 图表标题
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/13 10:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Title {
    /**
     * 主标题
     */
    @NonNull
    private String text;
    /**
     * 副标题
     */
    private String subtext;

}
