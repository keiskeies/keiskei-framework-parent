package top.keiskeiframework.common.vo.charts;

import lombok.*;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/13 10:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Title {

    @NonNull
    private String text;
    private String subtext;

}
