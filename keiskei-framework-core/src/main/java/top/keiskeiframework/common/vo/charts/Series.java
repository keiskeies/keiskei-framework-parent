package top.keiskeiframework.common.vo.charts;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 21:44
 */
@Data
public class Series implements Serializable {
    private static final long serialVersionUID = -4978366786197732637L;
    private String name;
    private String type;
}
