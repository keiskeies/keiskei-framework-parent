package top.keiskeiframework.common.vo.charts;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:15
 */
@Data
public class Axis implements Serializable {
    private static final long serialVersionUID = -8240149682292046994L;

    private String type;
    private List<String> data;
    private AxisTick axisTick;

    @Data
    public static class AxisTick implements Serializable {
        private static final long serialVersionUID = -426431462259414820L;
        private Boolean alignWidthLabel;
        private Boolean show;
    }
}
