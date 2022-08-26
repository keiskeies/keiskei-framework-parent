package top.keiskeiframework.common.util.data;

import com.fasterxml.jackson.databind.util.StdConverter;


/**
 * <p>
 * 比例反序列化工具 小数转整数
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/8/5 20:50
 */
public class RateDeserializer extends StdConverter<Double, Integer> {


    @Override
    public Integer convert(Double val) {
        return  (int)(val * 1000);
    }
}
