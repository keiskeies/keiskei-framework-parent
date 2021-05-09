package top.keiskeiframework.dashboard.dto.option;

import lombok.Data;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/4 17:45
 */
@Data
public class TextStyleDTO {
    private String color;
    private String fontStyle;
    private Object fontWeight = "bolder";
    private String fontFamily = "sans-serif";
    private Integer fontSize;
    private Integer lineHeight;
    private Integer width;
    private Integer height;
    private String textBorderColor;
    private Integer textBorderWidth;
    private Object textBorderType = "solid";
    private Integer textBorderDashOffset;
    private String textShadowColor = "transparent";
    private Integer textShadowBlur;
    private Integer textShadowOffsetX;
    private Integer textShadowOffsetY;
    private String overflow = "none";
    private String ellipsis;
    private String lineOverflow = "none";
    private Object rich;
}
