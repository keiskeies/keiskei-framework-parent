package top.keiskeiframework.common.base.util;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.QueryCondition;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.base.enums.ConditionEnum;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 查询条件组装工具
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

    protected static String getSingleConditionItemString(String c, ConditionEnum d, Serializable v) {
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

    protected static String getSingleConditionItemString(String c, ConditionEnum d, List<? extends Serializable> vs) {
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


    public static MultiStringCondition buildMultiStringCondition() {
        return new MultiStringCondition();
    }

    public static MultiCondition buildMultiCondition() {
        return new MultiCondition();
    }

    public static <T extends IBaseEntity<ID>, ID extends Serializable> ConditionQuery<T, ID> builderConditionQuery() {
        return new ConditionQuery<>();
    }


    public static class MultiStringCondition extends QueryConditionUtils implements Serializable {

        private static final long serialVersionUID = -7964700518842149238L;

        private final StringBuilder sb;

        public MultiStringCondition() {
            this.sb = new StringBuilder();
        }

        public MultiStringCondition addCondition(String condition) {
            sb.append(condition);
            return this;
        }

        public MultiStringCondition addCondition(String c, Serializable v) {
            sb.append(getSingleConditionItemString(c, ConditionEnum.EQ, v));
            return this;
        }

        public MultiStringCondition addCondition(String c, Serializable... vs) {
            sb.append(getSingleConditionItemString(c, ConditionEnum.IN, vs));
            return this;
        }

        public String build() {
            return String.format(VALUE_CONFIRM_FORMAT, sb);
        }
    }

    public static class MultiCondition implements QueryCondition, Serializable {

        private static final long serialVersionUID = 1911766069196965871L;
        private final List<QueryConditionVO> conditions;

        public MultiCondition() {
            this.conditions = new ArrayList<>();
        }

        @Override
        public MultiCondition addCondition(QueryConditionVO condition) {
            this.conditions.add(condition);
            return this;
        }

        @Override
        public MultiCondition addCondition(String c, Serializable v) {
            this.addCondition(new QueryConditionVO(c, v));
            return this;
        }

        @Override
        public MultiCondition addCondition(String c, ConditionEnum d, Serializable v) {
            this.addCondition(new QueryConditionVO(c, d, v));
            return this;
        }

        @Override
        public MultiCondition addCondition(String c, Serializable... vs) {
            this.addCondition(new QueryConditionVO(c, ConditionEnum.IN, Arrays.asList(vs)));
            return this;
        }

        @Override
        public MultiCondition addCondition(String c, List<? extends Serializable> vs) {
            this.addCondition(new QueryConditionVO(c, ConditionEnum.IN, vs));
            return this;
        }

        @Override
        public MultiCondition addCondition(String c, ConditionEnum d, List<? extends Serializable> vs) {
            this.addCondition(new QueryConditionVO(c, d, vs));
            return this;
        }

        @Override
        public MultiCondition addConditions(List<QueryConditionVO> conditions) {
            this.conditions.addAll(conditions);
            return this;
        }

        public List<QueryConditionVO> build() {
            return this.conditions;
        }

    }

    public static class ConditionQuery<T extends IBaseEntity<ID>, ID extends Serializable> implements QueryCondition, Serializable {


        private static final long serialVersionUID = -6276523055281763433L;
        private final BaseRequestVO<T, ID> requestVO;
        private List<QueryConditionVO> conditions;
        private Set<String> shows;

        public ConditionQuery() {
            this.requestVO = new BaseRequestVO<>();
        }

        @Override
        public ConditionQuery<T, ID> addCondition(String c, Serializable v) {
            initConditions();
            this.addCondition(new QueryConditionVO(c, v));
            return this;
        }

        @Override
        public ConditionQuery<T, ID> addCondition(String c, ConditionEnum d, Serializable v) {
            this.addCondition(new QueryConditionVO(c, d, v));
            return this;
        }

        @Override
        public ConditionQuery<T, ID> addCondition(String c, Serializable... vs) {
            this.addCondition(new QueryConditionVO(c, ConditionEnum.IN, Arrays.asList(vs)));
            return this;
        }

        @Override
        public ConditionQuery<T, ID> addCondition(String c, List<? extends Serializable> vs) {
            this.addCondition(new QueryConditionVO(c, ConditionEnum.IN, vs));
            return this;
        }

        @Override
        public ConditionQuery<T, ID> addCondition(String c, ConditionEnum d, List<? extends Serializable> vs) {
            this.addCondition(new QueryConditionVO(c, d, vs));
            return this;
        }

        @Override
        public ConditionQuery<T, ID> addCondition(QueryConditionVO condition) {
            initConditions();
            this.conditions.add(condition);
            return this;
        }

        @Override
        public ConditionQuery<T, ID> addConditions(List<QueryConditionVO> conditions) {
            initConditions();
            this.conditions.addAll(conditions);
            return this;
        }

        private void initConditions() {
            if (CollectionUtils.isEmpty(this.conditions)) {
                this.conditions = new ArrayList<>();
            }
        }

        public ConditionQuery<T, ID> addShow(String s) {
            if (!StringUtils.isEmpty(s)) {
                initShow();
                this.shows.add(s);
            }
            return this;
        }

        public ConditionQuery<T, ID> addShows(String... ss) {
            if (null != ss && ss.length > 0) {
                initShow();
                this.shows.addAll(Arrays.stream(ss).filter(s -> !StringUtils.isEmpty(s)).collect(Collectors.toList()));
            }
            return this;
        }

        public ConditionQuery<T, ID> addShows(List<String> ss) {
            if (!CollectionUtils.isEmpty(ss)) {
                initShow();
                this.shows.addAll(ss.stream().filter(s -> !StringUtils.isEmpty(s)).collect(Collectors.toList()));
            }
            return this;
        }

        private void initShow(){
            if (CollectionUtils.isEmpty(this.shows)) {
                this.shows = new HashSet<>();
            }
        }

        public BaseRequestVO<T, ID> build() {
            if (!CollectionUtils.isEmpty(this.conditions)) {
                this.requestVO.setListConditions(this.conditions);
            }
            if (!CollectionUtils.isEmpty(this.shows)) {
                this.requestVO.setListShow(new ArrayList<>(this.shows));
            }
            return requestVO;
        }
    }
}
