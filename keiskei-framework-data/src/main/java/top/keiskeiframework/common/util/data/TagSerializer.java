package top.keiskeiframework.common.util.data;


import com.fasterxml.jackson.databind.util.StdConverter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 标签序列化工具 字符转数组
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/8/5 20:45
 */
public class TagSerializer extends StdConverter<String, List<String>> {

    @Override
    public List<String> convert(String val) {
        if (StringUtils.isEmpty(val)) {
            return Collections.emptyList();
        }
        return Arrays.asList(val.split("\\|\\|"));
    }
}
