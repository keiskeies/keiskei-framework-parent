package top.keiskeiframework.generate.dto;

import lombok.Data;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/11 13:30
 */
@Data
public class FieldDTO {
    private String comment;
    private String propertyType;
    private String propertyName;
    private Integer length;
    private Integer decimalLength;
}
