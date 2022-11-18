package top.keiskeiframework.common.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 基础通用请求DTO
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/11/18 18:01
 */
@Data
public class BaseDTO<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = -3825108955287064603L;
    private ID id;
    private ID id2;
    private ID parentId;
    private String name;
    private String sign;
    private Boolean flag;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDate createDate;
    private LocalDate updateDate;
}
