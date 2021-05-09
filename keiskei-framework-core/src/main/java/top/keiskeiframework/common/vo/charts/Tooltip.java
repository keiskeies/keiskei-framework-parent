package top.keiskeiframework.common.vo.charts;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:29
 */
@Data
public class Tooltip implements Serializable {
    private static final long serialVersionUID = 6888302578810899219L;

    private String trigger;
    private AxisPointer axisPointer;

    @Data
    public static class AxisPointer implements Serializable{
        private static final long serialVersionUID = 6037813552983556143L;
        private String type;
    }
}
