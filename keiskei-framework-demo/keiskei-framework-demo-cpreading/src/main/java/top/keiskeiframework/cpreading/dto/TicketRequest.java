package top.keiskeiframework.cpreading.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 *
 * </p>
 * @since 2020/11/21 14:53
 */
@Data
public class TicketRequest {
    @NotBlank
    private String url;
}
