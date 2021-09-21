package top.keiskeiframework.common.util;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/21 21:25
 */

public class DateUtils {

    private static final Map<String, DateTimeFormatter> FORMATTER_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取本周的第一天
     *
     * @return
     */
    public static Date getFirstDayOfCurrentWeek() {

        return getFirstDayOfWeek(LocalDate.now());
    }

    public static Date getFirstDayOfWeek(LocalDate localDate) {
        LocalDate date = localDate;
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
            return toDate(date);
        }
        date = date.minus(dayOfWeek.minus(1).getValue(), ChronoUnit.DAYS);
        return toDate(date);
    }

    public static Date getFirstDayOfWeek(Date date) {
        return getFirstDayOfWeek(toLocalDate(date));
    }

    /**
     * 获取当月的第一天
     *
     * @return
     */
    public static Date getFirstDayOfCurrentMonth() {
        return getFirstDayOfMonth(LocalDate.now());
    }

    public static Date getFirstDayOfMonth(LocalDate localDate) {
        LocalDate date = localDate;
        int dayOfMonth = date.getDayOfMonth();
        date = date.minus(dayOfMonth - 1, ChronoUnit.DAYS);
        return toDate(date);
    }

    public static Date getFirstDayOfMonth(Date date) {
        return getFirstDayOfMonth(toLocalDate(date));
    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static Date getFirstDayOfCurrentYear() {
        return getFirstDayOfYear(LocalDate.now());
    }

    public static Date getFirstDayOfYear(LocalDate localDate) {
        LocalDate date = localDate;
        int dayOfYear = date.getDayOfYear();
        date = date.minus(dayOfYear - 1, ChronoUnit.DAYS);
        return toDate(date);
    }

    public static Date getFirstDayOfYear(Date date) {
        return getFirstDayOfYear(toLocalDate(date));
    }

    /**
     * 日期字符串解析
     *
     * @param source  字符串源
     * @param pattern 格式
     * @return
     */
    public static Date parse(String source, String pattern) {
        DateTimeFormatter formatter = getFormatter(pattern);
        /**
         * 24小时制小时占位符
         */
        String HOURS = "H";
        /**
         * 12小时制小时占位符
         */
        String HOURS_12 = "h";
        /**
         * 分钟站位符
         */
        String MINUTES = "m";
        /**
         * 秒占位符
         */
        String SECONDS = "s";
        if (pattern.contains(HOURS) || pattern.contains(HOURS_12) || pattern.contains(MINUTES)
                || pattern.contains(SECONDS)) {
            return toDate(LocalDateTime.parse(source, formatter));
        } else {
            return toDate(LocalDate.parse(source, formatter));
        }
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static String format(Date date, String pattern) {
        DateTimeFormatter formatter = getFormatter(pattern);
        return formatter.format(toLocalDateTime(date));
    }

    /**
     * 偏移指定时间段的时间，不改变原来的时间，返回新的时间对象
     *
     * @param date
     * @param duration
     * @param chronoUnit
     * @return
     */
    public static Date offSet(Date date, long duration, ChronoUnit chronoUnit) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        return toDate(localDateTime.plus(duration, chronoUnit));
    }

    /**
     * 获得两个日期之间的时间间隔
     *
     * @param start
     * @param end
     * @param chronoUnit
     * @return 返回以chronoUnit为时间单位的数值
     */
    public static long between(Date start, Date end, ChronoUnit chronoUnit) {
        return chronoUnit.between(toLocalDateTime(start), toLocalDateTime(end));
    }

    /**
     * 获取系统所在时区
     *
     * @return
     */
    private static ZoneId getSystemDefaultZoneId() {
        return ZoneOffset.systemDefault();
    }

    private static Date toDate(Instant instant) {
        return Date.from(instant);
    }

    private static Date toDate(LocalDateTime localDateTime) {
        return toDate(localDateTime.atZone(getSystemDefaultZoneId()).toInstant());
    }

    private static Date toDate(LocalDate localDate) {
        return toDate(localDate.atStartOfDay(getSystemDefaultZoneId()).toInstant());
    }

    private static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), getSystemDefaultZoneId());
    }

    public static LocalDate toLocalDate(Date date) {
        return LocalDate.from(toLocalDateTime(date));
    }

    private static DateTimeFormatter getFormatter(String pattern) {
        DateTimeFormatter formatter = FORMATTER_CACHE.get(pattern);
        if (formatter == null) {
            formatter = DateTimeFormatter.ofPattern(pattern);
            FORMATTER_CACHE.put(pattern, formatter);
        }
        return formatter;
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static Date ofTime(long timeMillis) {
        return new Date(timeMillis);
    }

    public static void main(String[] args) {
        Date parse = parse("20190101", "yyyyMMdd");
        System.out.println(parse);

        //Date parse = parse("20190101", "yyyyMMdd"); //报错
//        Date parse = parse("2019-01-01 01:01:01", "yyyy-MM-dd HH:mm:ss");

//        System.out.println(parse);
//        Date date = new Date();
//        Date d1 = offSet(date, 1, ChronoUnit.HOURS);
//        Date d2 = offSet(date, 1, ChronoUnit.DAYS);
//        Date d3 = offSet(date, 1, ChronoUnit.MINUTES);
//        Date d4 = offSet(date, -1, ChronoUnit.MINUTES);
//        System.out.println(date);
//        System.out.println(d1);
//        System.out.println(d2);
//        System.out.println(d3);
//        System.out.println(d4);
//
//        System.out.println(format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//
//        System.out.println(toDate(LocalDate.now()));
        LocalDate now = LocalDate.now();
        System.out.println(getFirstDayOfWeek(now));
        System.out.println(now);
        System.out.println(getFirstDayOfNextWeek());
        System.out.println(getFirstDayOfNextMonth());
        System.out.println(getFirstDayOfNextYear());
        String format = format(new Date(), "yyyyMM hh");
        System.out.println(format);

        System.out.println(parseMillis(11111111111111111L));
    }

    /**
     * 获取下周的第一天的日期
     */
    public static Date getFirstDayOfNextWeek() {
        return getFirstDayOfNextWeek(LocalDate.now());
    }

    public static Date getFirstDayOfNextWeek(LocalDate localDate) {
        LocalDate date = localDate;
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
            return toDate(date);
        }
        date = date.minus(dayOfWeek.minus(1).getValue(), ChronoUnit.DAYS);
        date = date.plusWeeks(1);
        return toDate(date);
    }

    /**
     * 获取下一个月的第一天的日期
     */
    public static Date getFirstDayOfNextMonth() {
        return getFirstDayOfNextMonth(LocalDate.now());
    }

    public static Date getFirstDayOfNextMonth(LocalDate localDate) {
        LocalDate date = localDate;
        int dayOfMonth = date.getDayOfMonth();
        date = date.minus(dayOfMonth - 1, ChronoUnit.DAYS);
        date = date.plusMonths(1);
        return toDate(date);
    }


    /**
     * 获取下一年的第一天的日期
     */
    public static Date getFirstDayOfNextYear() {
        return getFirstDayOfNextYear(LocalDate.now());
    }

    public static Date getFirstDayOfNextYear(LocalDate localDate) {
        LocalDate date = localDate;
        int dayOfYear = date.getDayOfYear();
        date = date.minus(dayOfYear - 1, ChronoUnit.DAYS);
        date = date.plusYears(1);
        return toDate(date);
    }

    /**
     * 获取当前日期
     */
    public static Date getNowDate() {
        return toDate(LocalDate.now());
    }

    /**
     * 获取当前时间
     */
    public static Date getNowDateTime() {
        return toDate(LocalDateTime.now());
    }

    /**
     * @param src
     * @return String:yyyy-MM-dd
     */
    public static String formatDate(Date src) {
        return format(src, "yyyy-MM-dd");
    }

    /**
     * @param src
     * @return String:yyyy-MM-dd HH:mm:ss
     */
    public static String formatDatetime(Date src) {
        return format(src, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @param src
     * @return Date:yyyy-MM-dd
     */
    public static Date parseDate(String src) {
        return parse(src, "yyyy-MM-dd");
    }

    /**
     * @param src
     * @return Date:yyyy-MM-dd HH:mm:ss
     */
    public static Date parseDatetime(String src) {
        return parse(src, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将毫秒转化（转换成*天*小时*分钟*秒）
     *
     * @param millis
     * @return
     * @throws ParseException
     */
    public static String parseMillis(long millis) {
        Duration duration = Duration.ofMillis(millis);
        long days = duration.toDays();
        duration = duration.minusDays(days);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long secs = duration.toMillis() / 1000;
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("天");
        }
        if (hours > 0) {
            sb.append(hours).append("小时");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分钟");
        }
        if (secs > 0) {
            sb.append(secs).append("秒");
        }
        return sb.toString();
    }
}



