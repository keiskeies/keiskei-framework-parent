package top.keiskeiframework.cpreading.feeling.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 图书领域VO
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/12/23 11:24
 */
@Data
@ApiModel(value="BookTerritoryVO", description="图书领域")
@NoArgsConstructor
@AllArgsConstructor
public class BookTerritoryVO implements Serializable {
    private static final long serialVersionUID = -866205587263837419L;

    @ApiModelProperty(value = "图书类型", dataType="BookType")
    private BookType bookType;

    @ApiModelProperty(value = "图书类型数量", dataType="Integer")
    private Integer bookCount;

}
