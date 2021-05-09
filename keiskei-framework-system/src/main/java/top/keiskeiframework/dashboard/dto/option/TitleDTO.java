package top.keiskeiframework.dashboard.dto.option;

import lombok.Data;

/**
 * <p>
 * 标题
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/4 17:43
 */
@Data
public class TitleDTO {
    private String id;
    private Boolean show;
    private String text;
    private String link;
    private String target;
    private TextStyleDTO textStyle;
    private String subtext;
    private String sublink;
    private String subtarget;
}
