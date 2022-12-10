package top.keiskeiframework.common.base.util;

import com.alibaba.fastjson.JSON;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.constants.BaseConstants;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.enums.ConditionEnum;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        return String.format(VALUE_CONFIRM_FORMAT, getConditionItem(c, d, v));
    }

    public static String getCondition(String c, List<Serializable> vs) {
        return getCondition(c, ConditionEnum.IN, vs);
    }

    public static String getCondition(String c, ConditionEnum d, List<Serializable> vs) {
        return String.format(VALUE_CONFIRM_FORMAT, getConditionItem(c, d, vs));
    }

    public static String getCondition(String c, Serializable... vs) {
        return getCondition(c, ConditionEnum.IN, vs);
    }

    public static String getCondition(String c, ConditionEnum d, Serializable... vs) {
        return String.format(VALUE_CONFIRM_FORMAT, getConditionItem(c, d, vs));
    }

    protected static String getConditionItem(String c, ConditionEnum d, Serializable v) {
        String value = v.toString();
        if (!(v instanceof Number)) {
            value = String.format(SINGLE_VALUE_FORMAT, value);
        }
        return String.format(SINGLE_CONDITION_FORMAT, c, d, value);
    }


    protected static String getConditionItem(String c, ConditionEnum d, Serializable... vs) {
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

    protected static String getConditionItem(String c, ConditionEnum d, List<Serializable> vs) {
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

    public static MultiConditionBuilder buildMultiCondition() {
        return new MultiConditionBuilder();
    }

    public static BaseRequestVOBuilder builderRequestVO() {
        return new BaseRequestVOBuilder();
    }
    public static BaseRequestVOBuilder builderRequestVO(BaseRequestVO requestVO) {
        return new BaseRequestVOBuilder(requestVO);
    }

    protected interface QueryConditionBuilder<T extends QueryConditionBuilder<T>> {

        void appendCondition(String condition);

        default T addCondition(String condition) {
            this.appendCondition(condition);
            return (T)this;
        }

        default T addCondition(QueryConditionVO condition) {
            this.appendCondition(JSON.toJSONString(condition));
            return (T)this;
        }

        default T addCondition(String c, Serializable v) {
            this.appendCondition(getConditionItem(c, ConditionEnum.EQ, v));
            return (T)this;
        }

        default T addCondition(String c, ConditionEnum d, Serializable v) {
            this.appendCondition(getConditionItem(c, d, v));
            return (T)this;
        }

        default T addCondition(String c, Serializable... vs) {
            this.appendCondition(getCondition(c, ConditionEnum.IN, Arrays.asList(vs)));
            return (T)this;
        }

        default T addCondition(String c, List<Serializable> vs) {
            this.appendCondition(getCondition(c, ConditionEnum.IN, vs));
            return (T)this;
        }

        default T addCondition(String c, ConditionEnum d, List<Serializable> vs) {
            this.appendCondition(getCondition(c, d, vs));
            return (T)this;
        }

        default T addConditions(List<QueryConditionVO> conditions) {
            for (QueryConditionVO conditionVO : conditions) {
                this.addCondition(conditionVO);
            }
            return (T)this;
        }
    }

    public static class MultiConditionBuilder implements QueryConditionBuilder<MultiConditionBuilder>, Serializable {

        private static final long serialVersionUID = 1911766069196965871L;


        protected StringBuilder conditions;
        public void appendCondition(String condition) {
            if (null == conditions) {
                this.conditions = new StringBuilder();
            }
            if (this.conditions.length() > 0) {
                this.conditions.append(SPLIT);
            }
            this.conditions.append(condition);
        }
        private MultiConditionBuilder() {}
        public String build() {
            return String.format(VALUE_CONFIRM_FORMAT, conditions);
        }

    }

    public static class BaseRequestVOBuilder implements QueryConditionBuilder<BaseRequestVOBuilder>, Serializable {

        private static final long serialVersionUID = -6276523055281763433L;

        private final BaseRequestVO requestVO;
        private StringBuilder conditions;
        private StringBuilder shows;
        public void appendCondition(String condition) {
            if (null == conditions) {
                this.conditions = new StringBuilder();
            }
            if (this.conditions.length() > 0) {
                this.conditions.append(SPLIT);
            }
            this.conditions.append(condition);
        }

        private BaseRequestVOBuilder(BaseRequestVO requestVO) {
            this.requestVO = requestVO;
            if (!conditionEmpty(requestVO)) {
                this.conditions = new StringBuilder(requestVO.getConditions().substring(1, requestVO.getConditions().length() - 1));
            }
            if (!showEmpty(requestVO)) {
                this.shows = new StringBuilder(requestVO.getShow());
            }
        }

        private BaseRequestVOBuilder() {
            this.requestVO = new BaseRequestVO();
        }


        private void appendShow(String show) {
            if (null == this.shows) {
                this.shows = new StringBuilder();
            } else if (this.shows.length() > 0) {
                this.shows.append(SPLIT);
            }
            this.shows.append(show);
        }

        public BaseRequestVOBuilder addShow(String s) {
            if (!StringUtils.isEmpty(s)) {
                this.appendShow(s);
            }
            return this;
        }

        public BaseRequestVOBuilder addShows(String... ss) {
            if (null != ss) {
                for (String s : ss) {
                    this.addShow(s);
                }
            }
            return this;
        }

        public BaseRequestVOBuilder addShows(List<String> ss) {
            if (!CollectionUtils.isEmpty(ss)) {
                for (String s : ss) {
                    this.addShow(s);
                }
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
            this.appendShow(show);
            return this;
        }

        public BaseRequestVO build() {
            if (!StringUtils.isEmpty(this.conditions)) {
                this.requestVO.setConditions(String.format(VALUE_CONFIRM_FORMAT, conditions));
            }
            if (!StringUtils.isEmpty(this.shows)) {
                this.requestVO.setShow(this.shows.toString());
            }
            return requestVO;
        }
    }
}
