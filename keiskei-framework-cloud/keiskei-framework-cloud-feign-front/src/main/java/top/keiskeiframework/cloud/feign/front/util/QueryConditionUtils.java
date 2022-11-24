package top.keiskeiframework.cloud.feign.front.util;

import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.base.enums.ConditionEnum;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 查询条件String
 * </p>
 *
 * @author James Chen
 * @since 2022/11/24 23:34
 */
public class QueryConditionUtils {

    protected final static String SINGLE_CONDITION_FORMAT = "{\"c\":\"%s\",\"d\":\"%s\",\"v\": [%s]}";
    protected final static String SINGLE_VALUE_FORMAT = "\"%s\"";
    protected final static String VALUE_CONFIRM_FORMAT = "[%s]";

    public static String getSingleConditionString(String c, Serializable v) {
        return getSingleConditionString(c, ConditionEnum.EQ, v);
    }

    public static String getSingleConditionString(String c, ConditionEnum d, Serializable v) {
        return String.format(VALUE_CONFIRM_FORMAT, getSingleConditionItemString(c, d, v));
    }

    public static String getSingleConditionItemString(String c, ConditionEnum d, Serializable v) {
        String value = v.toString();
        if (!(v instanceof Number)) {
            value = String.format(SINGLE_VALUE_FORMAT, value);
        }
        return String.format(SINGLE_CONDITION_FORMAT, c, d, value);
    }

    public static String getSingleConditionString(String c, List<? extends Serializable> vs) {
        return getSingleConditionString(c, ConditionEnum.IN, vs);
    }

    public static String getSingleConditionString(String c, ConditionEnum d, List<? extends Serializable> vs) {
        return String.format(VALUE_CONFIRM_FORMAT, getSingleConditionItemString(c, d, vs));
    }

    public static String getSingleConditionItemString(String c, ConditionEnum d, List<? extends Serializable> vs) {
        if (CollectionUtils.isEmpty(vs)) {
            return null;
        }
        String value;
        if (vs.get(0) instanceof Number) {
            value = vs.stream().map(Object::toString).collect(Collectors.joining(","));
        } else {
            value = vs.stream().map(v -> String.format(SINGLE_VALUE_FORMAT, v)).collect(Collectors.joining(","));
        }
        return String.format(SINGLE_CONDITION_FORMAT, c, d, value);
    }

    @SafeVarargs
    public static <V extends Serializable> String getSingleConditionString(String c, V... vs) {
        return getSingleConditionString(c, ConditionEnum.IN, vs);
    }

    @SafeVarargs
    public static <V extends Serializable> String getSingleConditionString(String c, ConditionEnum d, V... vs) {
        return String.format(VALUE_CONFIRM_FORMAT, getSingleConditionItemString(c, d, vs));
    }

    @SafeVarargs
    public static <V extends Serializable> String getSingleConditionItemString(String c, ConditionEnum d, V... vs) {
        if (null == vs || vs.length == 0) {
            return null;
        }
        String value;
        if (vs[0] instanceof Number) {
            value = Arrays.stream(vs).map(Object::toString).collect(Collectors.joining(","));
        } else {
            value = Arrays.stream(vs).map(v -> String.format(SINGLE_VALUE_FORMAT, v)).collect(Collectors.joining(","));
        }
        return String.format(SINGLE_CONDITION_FORMAT, c, d, value);
    }

    public static MultiCondition buildMultiCondition() {
        return new MultiCondition();
    }

    public static class MultiCondition extends QueryConditionUtils implements Serializable {

        private static final long serialVersionUID = -7964700518842149238L;

        private final StringBuilder sb = new StringBuilder();

        public MultiCondition addCondition(String condition) {
            sb.append(condition);
            return this;
        }

        public MultiCondition addCondition(String c, Serializable v) {
            sb.append(getSingleConditionItemString(c, ConditionEnum.EQ, v));
            return this;
        }

        public MultiCondition addCondition(String c, Serializable... vs) {
            sb.append(getSingleConditionItemString(c, ConditionEnum.IN, vs));
            return this;
        }

        public String build() {
            return String.format(VALUE_CONFIRM_FORMAT, sb);
        }
    }
}
