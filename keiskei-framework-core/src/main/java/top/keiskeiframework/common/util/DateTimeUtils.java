package top.keiskeiframework.common.util;

import top.keiskeiframework.common.exception.BizException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    public final static String Y_M_D = "yyyy-MM-dd";
    public final static String Y_M = "yyyy-MM";
    public final static String M_D = "MM-dd";
    public final static String Y = "yyyy";
    public final static String H = "HH";

    public final static String DATE_REGEX = "([\\d]{4}[-|/|年][\\d]{2}[-|/|月][\\d]{2}[日]?)";
    public final static String TIME_REGEX = "([\\d]{2}[:|时][\\d]{2}[:|分][\\d]{2})[秒]?";
    public final static String SECOND_REGEX = "(.[\\d]{3})";
    public final static String DATE_TIME_REGEX = DATE_REGEX + "?([ |a-z|A-Z])" + TIME_REGEX + "?" + SECOND_REGEX + "?";


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
     * 时间转字符串
     *
     * @param time 时间
     * @param unit 时间格式
     * @return 。
     */
    public static String timeToString(LocalDateTime time, ChronoUnit unit) {
        if (ChronoUnit.HOURS.equals(unit)) {
            return timeToString(time, H);
        } else if (ChronoUnit.DAYS.equals(unit)) {
            return timeToString(time, Y_M_D);
        } else if (ChronoUnit.WEEKS.equals(unit)) {
            return time.getDayOfWeek().name();
        } else if (ChronoUnit.MONTHS.equals(unit)) {
            return timeToString(time, Y_M);
        } else if (ChronoUnit.YEARS.equals(unit)) {
            return timeToString(time, Y);
        } else {
            return timeToString(time);
        }
    }

    public final static List<String> HOURS_RANGE = Arrays.asList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
    public final static List<String> WEEKS_RANGE = Arrays.asList("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY");

    public static List<String> timeRange(LocalDateTime start, LocalDateTime end, ChronoUnit unit) {
        if (ChronoUnit.HOURS.equals(unit)) {
            return HOURS_RANGE;
        }
        if (ChronoUnit.WEEKS.equals(unit)) {
            return WEEKS_RANGE;
        }
        Duration duration = Duration.between(start, end);
        long durationLength;
        if (ChronoUnit.DAYS.equals(unit)) {
            durationLength = duration.toDays();
        } else if (ChronoUnit.MONTHS.equals(unit)) {
            durationLength = duration.toDays() / 30;
        } else if (ChronoUnit.YEARS.equals(unit)) {
            durationLength = duration.toDays() / 365;
        } else {
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);
        }
        Set<String> result = new HashSet<>((int) durationLength);
        LocalDateTime time;
        for (long i = 0; i <= durationLength; i++) {
            time = start.plus(i, unit);
            result.add(timeToString(time, unit));
        }
        return result.stream().sorted().collect(Collectors.toList());
    }

    public static ChronoUnit getUnitByString(String unitString) {
        switch (unitString) {
            case "Hours":
            case "Days":
            case "Weeks":
            case "Months":
            case "Years":
                for (ChronoUnit unit : ChronoUnit.values()) {
                    if (unit.toString().equalsIgnoreCase(unitString)) {
                        return unit;
                    }
                }
        }
        throw new UnsupportedTemporalTypeException("Unsupported unit: " + unitString);
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

}
