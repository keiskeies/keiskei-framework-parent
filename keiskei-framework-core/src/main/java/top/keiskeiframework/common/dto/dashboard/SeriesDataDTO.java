package top.keiskeiframework.common.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/10 17:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeriesDataDTO {

    private String index;
    private Long indexNumber;
}
