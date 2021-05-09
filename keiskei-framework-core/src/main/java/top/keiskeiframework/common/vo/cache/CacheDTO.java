package top.keiskeiframework.common.vo.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 缓存
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/15 19:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheDTO {

    /**
     * key
     */
    private String key;
    /**
     * value
     */
    private Object value;

    public static List<CacheDTO> of (Map<String, Object> data) {
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        List<CacheDTO> result = new ArrayList<>(data.size());
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            result.add(new CacheDTO(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}
