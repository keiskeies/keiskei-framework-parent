package top.keiskeiframework.cpreading.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 阅读时间轴
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/12/23 13:48
 */
@Data
@ApiModel(value="BookTimelineVO", description="图书时间轴")
@NoArgsConstructor
@AllArgsConstructor
public class BookTimelineVO implements Serializable {
    private static final long serialVersionUID = -4339822444686095015L;

    @ApiModelProperty(value = "时间节点", dataType="String")
    private String date;

    @ApiModelProperty(value = "阅读数量", dataType="Integer")
    private Integer number;
}
