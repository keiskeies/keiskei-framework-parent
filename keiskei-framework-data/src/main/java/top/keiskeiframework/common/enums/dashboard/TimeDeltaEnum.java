package top.keiskeiframework.common.enums.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.keiskeiframework.common.util.DateTimeUtils;

/**
 * <p>
 * 时间跨度类型
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/13 22:41
 */
@AllArgsConstructor
@Getter
public enum TimeDeltaEnum {
    /**
     * 小时
     */
    HOUR(DateTimeUtils.H),
    /**
     * 所有小时
     */
    ALL_HOURS(DateTimeUtils.Y_M_D_H),
    /**
     * 星期几
     */
    WEEK_DAY(null),
    /**
     * 月份每天
     */
    MONTH_DAYS(DateTimeUtils.D),
    /**
     * 所有天
     */
    ALL_DAYS(DateTimeUtils.Y_M_D),
    /**
     * 每月份
     */
    MONTH(DateTimeUtils.M),
    /**
     * 所有月份
     */
    ALL_MONTHS(DateTimeUtils.M),
    /**
     * 季度
     */
    QUARTER(null),
    /**
     * 所有季度
     */
    ALL_QUARTERS(null),
    /**
     * 年份
     */
    YEAR(DateTimeUtils.Y),

    ;


    private final String value;


}
