package top.keiskeiframework.common.vo.charts;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/9 22:20
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Legend implements Serializable {
    private static final long serialVersionUID = -194763321705309878L;
    private String left;
    private String bottom;
    @NonNull
    private List<String> data;




}
