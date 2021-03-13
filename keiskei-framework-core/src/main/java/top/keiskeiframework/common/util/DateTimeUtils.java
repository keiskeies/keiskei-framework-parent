package top.keiskeiframework.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public final static String DATE_REGEX = "([\\d]{4}[-|/|年][\\d]{2}[-|/|月][\\d]{2}[日]?)";
    public final static String TIME_REGEX = "([\\d]{2}[:|时][\\d]{2}[:|分][\\d]{2})[秒]?";
    public final static String SECOND_REGEX = "(.[\\d]{3})";
    public final static String DATE_TIME_REGEX = DATE_REGEX + "?([ |a-z|A-Z])" + TIME_REGEX + "?" + SECOND_REGEX + "?";


    public static String timeToString(LocalDateTime time) {
        return timeToString(time, Y_M_D_H_M_S);

    }

    public static String timeToString(LocalDateTime time, String formatter) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(formatter);
        return df.format(time);
    }


    public static LocalDateTime strToTime(String str) {
        return strToTime(str, getFormatter(str));
    }

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

    public static String getFormatter(String time) {
        return time
                .replaceAll(DATE_TIME_REGEX, "$1 $3$4")
                .replaceFirst(DATE_REGEX, "yyyy-MM-dd")
                .replaceFirst(TIME_REGEX, "HH:mm:ss")
                .replaceFirst(SECOND_REGEX, ".SSS")
                .trim();

    }
}
