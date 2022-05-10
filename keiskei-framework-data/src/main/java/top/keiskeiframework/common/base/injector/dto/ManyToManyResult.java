package top.keiskeiframework.common.base.injector.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 多对多查询结果
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/4/14 11:08
 */
@Data
public class ManyToManyResult implements Serializable{
    private static final long serialVersionUID = 8394766169458529140L;
    private String entity;
    private String fieldName;
    private Object joinId;
}
