package top.keiskeiframework.common.vo.dashboard.charts;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 图表series基础数据
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 21:44
 */
@Data
public class Series implements Serializable {
    private static final Long serialVersionUID = -4978366786197732637L;
    /**
     * 数据分类名称
     */
    private String name;
    /**
     * 图表类型
     */
    private String type;
}
