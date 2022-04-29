package top.keiskeiframework.log.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 操作日志DTO
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/19 16:14
 */
@Data
public class OperateLogDTO implements Serializable {

    private static final long serialVersionUID = -1383605274795200930L;

    /**
     * 操作人员
     */
    private Integer userId;
    /**
     * 操作IP
     */
    private String ip;
    /**
     * 操作名称
     */
    private String name;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 操作URL
     */
    private String url;
    /**
     * 请求参数
     */
    private String requestParam;
    /**
     * 返回结果
     */
    private String responseParam;

}
