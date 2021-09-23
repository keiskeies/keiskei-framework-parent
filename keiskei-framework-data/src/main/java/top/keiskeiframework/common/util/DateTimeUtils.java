package top.keiskeiframework.common.util;

import top.keiskeiframework.common.enums.dashboard.TimeDeltaEnum;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 时间工具类
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午2:38:23
 */
public class DateTimeUtils {

    public final static String Y_M_D_H_M_S_S = "yyyy-MM-dd HH:mm:ss.SSS";
    public final static String Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    public final static String Y_M_D_H_M = "yyyy-MM-dd HH:mm";
    public final static String Y_M_D_H = "yyyy-MM-dd HH";
    public final static String Y_M_D = "yyyy-MM-dd";
    public final static String Y_M = "yyyy-MM";
    public final static String M = "MM";
    public final static String M_D = "MM-dd";
    public final static String D = "dd";
    public final static String Y = "yyyy";
    public final static String H = "HH";

    public final static String DATE_REGEX = "([\\d]{4}[-|/|年][\\d]{2}[-|/|月][\\d]{2}[日]?)";
    public final static String TIME_REGEX = "([\\d]{2}[:|时][\\d]{2}[:|分][\\d]{2})[秒]?";
    public final static String SECOND_REGEX = "(.[\\d]{3})";
    public final static String DATE_TIME_REGEX = DATE_REGEX + "?([ |a-z|A-Z])" + TIME_REGEX + "?" + SECOND_REGEX + "?";

    public final static List<String> HOUR_RANGE = Arrays.asList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
    public final static List<String> WEEK_RANGE = Arrays.asList("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY");
    public final static List<String> MONTH_RANGE = new ArrayList<>(12);
    public final static List<String> QUARTER_RANGE = new ArrayList<>(12);
    public final static List<String> MONTH_DAY_RANGE = new ArrayList<>(31);

    static {
        for (int i = 1; i < 32; i++) {
            MONTH_DAY_RANGE.add(String.format("%02d", i));
        }
        for (int i = 1; i < 13; i++) {
            MONTH_RANGE.add(String.format("%02d", i));
        }
        for (int i = 1; i < 5; i++) {
            QUARTER_RANGE.add(i + "");
        }
    }


    /**
     * 时间转字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param time 时间
     * @return 。
     */
    public static String timeToString(LocalDateTime time) {
        return timeToString(time, Y_M_D_H_M_S);

    }

    /**
     * 时间转字符串
     *
     * @param time      时间
     * @param formatter 格式
     * @return 。
     */
    public static String timeToString(LocalDateTime time, String formatter) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(formatter);
        return df.format(time);
    }


    /**
     * 获取时间范围
     *
     * @param start 起始时间
     * @param end   结束时间
     * @param delta 时间跨度
     * @return 。
     */
    public static List<String> timeRange(LocalDateTime start, LocalDateTime end, TimeDeltaEnum delta) {
        if (TimeDeltaEnum.HOUR.equals(delta)) {
            return HOUR_RANGE;
        }
        if (TimeDeltaEnum.WEEK_DAY.equals(delta)) {
            return WEEK_RANGE;
        }
        if (TimeDeltaEnum.MONTH_DAYS.equals(delta)) {
            return MONTH_DAY_RANGE;
        }
        if (TimeDeltaEnum.MONTH.equals(delta)) {
            return MONTH_RANGE;
        }
        if (TimeDeltaEnum.QUARTER.equals(delta)) {
            return QUARTER_RANGE;
        }
        Duration duration = Duration.between(start, end);
        long durationLength;
        ChronoUnit unit;
        LocalDateTime time;
        Set<String> result = new HashSet<>();
        if (TimeDeltaEnum.ALL_HOURS.equals(delta)) {
            durationLength = duration.toHours();
            unit = ChronoUnit.HOURS;
            for (long i = 0; i <= durationLength; i++) {
                time = start.plus(i, unit);
                result.add(timeToString(time, Y_M_D_H));
            }
        } else if (TimeDeltaEnum.ALL_DAYS.equals(delta)) {
            durationLength = duration.toDays();
            unit = ChronoUnit.DAYS;
            for (long i = 0; i <= durationLength; i++) {
                time = start.plus(i, unit);
                result.add(timeToString(time, Y_M_D));
            }
        } else if (TimeDeltaEnum.ALL_MONTHS.equals(delta)) {
            durationLength = duration.toDays() / 28;
            unit = ChronoUnit.DAYS;
            for (long i = 0; i <= durationLength; i++) {
                time = start.plus(i * 28, unit);
                result.add(timeToString(time, Y_M));
            }
        } else if (TimeDeltaEnum.ALL_QUARTERS.equals(delta)) {
            durationLength = duration.toDays() / 90;
            unit = ChronoUnit.DAYS;
            for (long i = 0; i <= durationLength; i++) {
                time = start.plus(i * 90, unit);
                result.add(timeToString(time, Y) + "-" + (i % 4 + 1));
            }
        } else if (TimeDeltaEnum.YEAR.equals(delta)) {
            durationLength = duration.toDays() / 365;
            unit = ChronoUnit.DAYS;
            for (long i = 0; i <= durationLength; i++) {
                time = start.plus(i * 365, unit);
                result.add(timeToString(time, Y));
            }
        } else {
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + delta);
        }
        return result.stream().sorted().collect(Collectors.toList());
    }

    public static ChronoUnit getByTimeDelta(TimeDeltaEnum delta) {
        switch (delta) {
            case HOUR:
            case ALL_HOURS:
                return ChronoUnit.HOURS;
            case ALL_DAYS:
            case MONTH_DAYS:
            case WEEK_DAY:
                return ChronoUnit.DAYS;
            case MONTH:
            case ALL_MONTHS:
                return ChronoUnit.MONTHS;
            case YEAR:
                return ChronoUnit.YEARS;
            default:
                return null;
        }
    }

    /**
     * 获取季度
     *
     * @param month 当月月份
     * @return .
     */
    public static int getQuarter(int month) {
        return month / 4 + month % 4;

    }

    /**
     * 字符串转时间 自动匹配
     *
     * @param str 格式时间字符串
     * @return 。
     */
    public static LocalDateTime strToTime(String str) {
        return strToTime(str, getFormatter(str));
    }

    /**
     * 字符串转时间
     *
     * @param str       格式时间字符串
     * @param formatter 格式
     * @return 。
     */
    public static LocalDateTime strToTime(String str, String formatter) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(formatter);
        LocalDateTime ldt;
        try {
            ldt = LocalDateTime.parse(str, df);
            return ldt;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 匹配时间格式
     *
     * @param time 时间
     * @return 。
     */
    public static String getFormatter(String time) {
        return time
                .replaceAll(DATE_TIME_REGEX, "$1 $3$4")
                .replaceFirst(DATE_REGEX, "yyyy-MM-dd")
                .replaceFirst(TIME_REGEX, "HH:mm:ss")
                .replaceFirst(SECOND_REGEX, ".SSS")
                .trim();

    }

    public static LocalDateTime[] getStartAndEndDayOfDay(LocalDate today) {
        if (today == null) {
            today = LocalDate.now();
        }
        return new LocalDateTime[]{LocalDateTime.of(today, LocalTime.MIN), LocalDateTime.of(today, LocalTime.MAX)};
    }

    public static LocalDateTime[] getStartAndEndDayOfWeek(LocalDate today) {
        if (today == null) {
            today = LocalDate.now();
        }
        DayOfWeek week = today.getDayOfWeek();
        int value = week.getValue();
        return new LocalDateTime[]{
                LocalDateTime.of(today.minusDays(value - 1), LocalTime.MIN),
                LocalDateTime.of(today.plusDays(7 - value), LocalTime.MAX)
        };
    }

    public static LocalDateTime[] getStartAndEndDayOfMonth(LocalDate today) {
        if (today == null) {
            today = LocalDate.now();
        }
        Month month = today.getMonth();
        int length = month.length(today.isLeapYear());
        return new LocalDateTime[]{
                LocalDateTime.of(LocalDate.of(today.getYear(), month, 1), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(today.getYear(), month, length), LocalTime.MAX)
        };
    }

    public static LocalDateTime[] getStartAndEndDayOfQuarter(LocalDate today) {
        if (today == null) {
            today = LocalDate.now();
        }
        Month month = today.getMonth();
        Month firstMonthOfQuarter = month.firstMonthOfQuarter();
        Month endMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
        return new LocalDateTime[]{
                LocalDateTime.of(LocalDate.of(today.getYear(), firstMonthOfQuarter, 1), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(today.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(today.isLeapYear())), LocalTime.MAX)
        };
    }

    public static LocalDateTime[] getStartAndEndDayOfYear(LocalDate today) {
        if (today == null) {
            today = LocalDate.now();
        }
        return new LocalDateTime[]{
                LocalDateTime.of(LocalDate.of(today.getYear(), Month.JANUARY, 1), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(today.getYear(), Month.DECEMBER, Month.DECEMBER.length(today.isLeapYear())), LocalTime.MAX)
        };
    }
}
