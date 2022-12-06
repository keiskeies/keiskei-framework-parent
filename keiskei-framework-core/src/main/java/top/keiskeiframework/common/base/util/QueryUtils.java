package top.keiskeiframework.common.base.util;

import com.alibaba.fastjson.JSON;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.QueryCondition;
import top.keiskeiframework.common.base.constants.BaseConstants;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
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
public class QueryUtils {

    protected final static String SINGLE_CONDITION_FORMAT = "{\"c\":\"%s\",\"d\":\"%s\",\"v\": [%s]}";
    protected final static String SINGLE_VALUE_FORMAT = "\"%s\"";
    protected final static String VALUE_CONFIRM_FORMAT = "[%s]";
    protected final static String SPLIT = ",";

    public static String getCondition(String c, Serializable v) {
        return getCondition(c, ConditionEnum.EQ, v);
    }

    public static String getCondition(String c, ConditionEnum d, Serializable v) {
        return String.format(VALUE_CONFIRM_FORMAT, getConditionString(c, d, v));
    }

    protected static String getConditionString(String c, ConditionEnum d, Serializable v) {
        String value = v.toString();
        if (!(v instanceof Number)) {
            value = String.format(SINGLE_VALUE_FORMAT, value);
        }
        return String.format(SINGLE_CONDITION_FORMAT, c, d, value);
    }

    public static String getCondition(String c, List<? extends Serializable> vs) {
        return getCondition(c, ConditionEnum.IN, vs);
    }

    public static String getCondition(String c, ConditionEnum d, List<? extends Serializable> vs) {
        return String.format(VALUE_CONFIRM_FORMAT, getConditionString(c, d, vs));
    }

    protected static String getConditionString(String c, ConditionEnum d, List<? extends Serializable> vs) {
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
    public static <V extends Serializable> String getCondition(String c, V... vs) {
        return getCondition(c, ConditionEnum.IN, vs);
    }

    @SafeVarargs
    public static <V extends Serializable> String getCondition(String c, ConditionEnum d, V... vs) {
        return String.format(VALUE_CONFIRM_FORMAT, getConditionString(c, d, vs));
    }

    @SafeVarargs
    public static <V extends Serializable> String getConditionString(String c, ConditionEnum d, V... vs) {
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

    public static boolean conditionEmpty(BaseRequestVO requestVO) {
        return StringUtils.isEmpty(requestVO.getConditions());
    }

    public static boolean showEmpty(BaseRequestVO requestVO) {
        return StringUtils.isEmpty(requestVO.getShow());
    }

    public static boolean requestEmpty(BaseRequestVO requestVO) {
        return conditionEmpty(requestVO) && showEmpty(requestVO);
    }

    public static List<QueryConditionVO> getConditions(BaseRequestVO requestVO) {
        if (!conditionEmpty(requestVO)) {
            return JSON.parseArray(requestVO.getConditions(), QueryConditionVO.class);
        } else {
            return Collections.emptyList();
        }
    }

    public static List<String> getShow(BaseRequestVO requestVO) {
        if (!showEmpty(requestVO)) {
            List<String> listShow = Arrays.stream(requestVO.getShow().split(BaseConstants.SHOW_SPLIT))
                    .map(String::trim).collect(Collectors.toList());
            if (!listShow.contains(BaseConstants.ID_COLUMN)) {
                listShow.add(0, BaseConstants.ID_COLUMN);
            }
            return listShow;
        } else {
            return Collections.emptyList();
        }
    }

    public static MultiStringConditionBuilder buildMultiStringCondition() {
        return new MultiStringConditionBuilder();
    }

    public static MultiConditionBuilder buildMultiCondition() {
        return new MultiConditionBuilder();
    }

    public static BaseRequestVOBuilder builderRequestVO() {
        return new BaseRequestVOBuilder();
    }


    public static class MultiStringConditionBuilder extends QueryUtils implements Serializable {

        private static final long serialVersionUID = -7964700518842149238L;

        private final StringBuilder sb;

        public MultiStringConditionBuilder() {
            this.sb = new StringBuilder();
        }

        public MultiStringConditionBuilder addCondition(String condition) {
            sb.append(condition);
            return this;
        }

        public MultiStringConditionBuilder addCondition(String c, Serializable v) {
            sb.append(getConditionString(c, ConditionEnum.EQ, v));
            return this;
        }

        public MultiStringConditionBuilder addCondition(String c, Serializable... vs) {
            sb.append(getConditionString(c, ConditionEnum.IN, vs));
            return this;
        }

        public String build() {
            return String.format(VALUE_CONFIRM_FORMAT, sb);
        }
    }

    public static class MultiConditionBuilder implements QueryCondition, Serializable {

        private static final long serialVersionUID = 1911766069196965871L;
        private final List<QueryConditionVO> conditions;

        public MultiConditionBuilder() {
            this.conditions = new ArrayList<>();
        }

        @Override
        public MultiConditionBuilder addCondition(QueryConditionVO condition) {
            this.conditions.add(condition);
            return this;
        }

        @Override
        public MultiConditionBuilder addCondition(String c, Serializable v) {
            this.addCondition(new QueryConditionVO(c, v));
            return this;
        }

        @Override
        public MultiConditionBuilder addCondition(String c, ConditionEnum d, Serializable v) {
            this.addCondition(new QueryConditionVO(c, d, v));
            return this;
        }

        @Override
        public MultiConditionBuilder addCondition(String c, Serializable... vs) {
            this.addCondition(new QueryConditionVO(c, ConditionEnum.IN, Arrays.asList(vs)));
            return this;
        }

        @Override
        public MultiConditionBuilder addCondition(String c, List<? extends Serializable> vs) {
            this.addCondition(new QueryConditionVO(c, ConditionEnum.IN, vs));
            return this;
        }

        @Override
        public MultiConditionBuilder addCondition(String c, ConditionEnum d, List<? extends Serializable> vs) {
            this.addCondition(new QueryConditionVO(c, d, vs));
            return this;
        }

        @Override
        public MultiConditionBuilder addConditions(List<QueryConditionVO> conditions) {
            this.conditions.addAll(conditions);
            return this;
        }

        public String build() {
            return JSON.toJSONString(this.conditions);
        }

    }

    public static class BaseRequestVOBuilder implements QueryCondition, Serializable {


        private static final long serialVersionUID = -6276523055281763433L;
        private final BaseRequestVO requestVO;
        private List<QueryConditionVO> conditions;
        private Set<String> shows;

        public BaseRequestVOBuilder() {
            this.requestVO = new BaseRequestVO();
        }

        @Override
        public BaseRequestVOBuilder addCondition(String c, Serializable v) {
            initConditions();
            this.addCondition(new QueryConditionVO(c, v));
            return this;
        }

        @Override
        public BaseRequestVOBuilder addCondition(String c, ConditionEnum d, Serializable v) {
            this.addCondition(new QueryConditionVO(c, d, v));
            return this;
        }

        @Override
        public BaseRequestVOBuilder addCondition(String c, Serializable... vs) {
            this.addCondition(new QueryConditionVO(c, ConditionEnum.IN, Arrays.asList(vs)));
            return this;
        }

        @Override
        public BaseRequestVOBuilder addCondition(String c, List<? extends Serializable> vs) {
            this.addCondition(new QueryConditionVO(c, ConditionEnum.IN, vs));
            return this;
        }

        @Override
        public BaseRequestVOBuilder addCondition(String c, ConditionEnum d, List<? extends Serializable> vs) {
            this.addCondition(new QueryConditionVO(c, d, vs));
            return this;
        }

        @Override
        public BaseRequestVOBuilder addCondition(QueryConditionVO condition) {
            initConditions();
            this.conditions.add(condition);
            return this;
        }

        @Override
        public BaseRequestVOBuilder addConditions(List<QueryConditionVO> conditions) {
            initConditions();
            this.conditions.addAll(conditions);
            return this;
        }

        private void initConditions() {
            if (CollectionUtils.isEmpty(this.conditions)) {
                this.conditions = new ArrayList<>();
            }
        }

        public BaseRequestVOBuilder addShow(String s) {
            if (!StringUtils.isEmpty(s)) {
                initShow();
                this.shows.add(s);
            }
            return this;
        }

        public BaseRequestVOBuilder addShows(String... ss) {
            if (null != ss && ss.length > 0) {
                initShow();
                this.shows.addAll(Arrays.stream(ss).filter(s -> !StringUtils.isEmpty(s)).collect(Collectors.toList()));
            }
            return this;
        }

        public BaseRequestVOBuilder addShows(List<String> ss) {
            if (!CollectionUtils.isEmpty(ss)) {
                initShow();
                this.shows.addAll(ss.stream().filter(s -> !StringUtils.isEmpty(s)).collect(Collectors.toList()));
            }
            return this;
        }

        public BaseRequestVOBuilder desc(String desc) {
            this.requestVO.setDesc(desc);
            return this;
        }

        public BaseRequestVOBuilder asc(String asc) {
            this.requestVO.setDesc(asc);
            return this;
        }

        public BaseRequestVOBuilder tree(boolean tree) {
            this.requestVO.setTree(tree);
            return this;
        }

        public BaseRequestVOBuilder complete(boolean complete) {
            this.requestVO.setComplete(complete);
            return this;
        }

        public BaseRequestVOBuilder show(String show) {
            initShow();
            this.shows.addAll(Arrays.asList(show.split(SPLIT)));
            return this;
        }

        private void initShow() {
            if (CollectionUtils.isEmpty(this.shows)) {
                this.shows = new HashSet<>();
            }
        }

        public BaseRequestVO build() {
            if (!CollectionUtils.isEmpty(this.conditions)) {
                this.requestVO.setConditions(JSON.toJSONString(this.conditions));
            }
            if (!CollectionUtils.isEmpty(this.shows)) {
                this.requestVO.setShow(String.join(SPLIT, this.shows));
            }
            return requestVO;
        }
    }
}
