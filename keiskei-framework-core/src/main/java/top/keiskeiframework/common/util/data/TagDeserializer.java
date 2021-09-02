package top.keiskeiframework.common.util.data;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * <p>
 * 标签反序列化工具 数组转字符
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 */
public class TagDeserializer extends StdConverter<List<String>, String> {


    @Override
    public String convert(List<String> val) {
        if (CollectionUtils.isEmpty(val)) {
            return "";
        }
        return String.join("||", val);
    }

}
