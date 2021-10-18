package top.keiskeiframework.workflow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * <p>
 * 卡片字段 类型 字段枚举
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:38:26
 */
public enum IssueFieldTypeEnum {

    // 单行文字
    WORD,
    // 段落
    LONG_WORD,
    // 富文本
    HTML,
    // 日期
    DATE,
    // 时间
    TIME,
    // 日期时间
    DATE_TIME,
    // 单选
    SELECT,
    // 多选
    MULTI_SELECT,
    // 用户单选
    USER_SELECT,
    // 用户多选
    USER_MULTI_SELECT,
    // 整数
    NUMBER,
    // 小数
    DECIMAL,
    // 树形单选
    TREE_SELECT,
    // 树形多选
    TREE_MULTI_SELECT,
    // 标签
    TAGS,
    ;


}
