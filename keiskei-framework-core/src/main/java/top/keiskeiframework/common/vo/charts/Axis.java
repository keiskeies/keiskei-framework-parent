package top.keiskeiframework.common.vo.charts;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 方向坐标
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:15
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Axis implements Serializable {
    private static final long serialVersionUID = -8240149682292046994L;
    /**
     * 坐标类型
     */
    private String type;
    /**
     *
     */
    private AxisTick axisTick;

    /**
     * 数据
     */
    @NonNull
    private List<String> data;

    /**
     *
     */
    @Data
    public static class AxisTick implements Serializable {
        private static final long serialVersionUID = -426431462259414820L;
        private Boolean alignWidthLabel;
        private Boolean show;
    }
}
