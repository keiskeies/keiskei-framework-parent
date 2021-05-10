package top.keiskeiframework.dashboard.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/13 18:31
 */
@Getter
@AllArgsConstructor
public enum DashboardTypeEnum {
    //
    BAR("bar", "柱状图"),
    LINE("line", "折线图"),
    PIE("pie","饼图"),
    RADAR("radar", "雷达图")



    ;

    private final String id;
    private final String name;
}
