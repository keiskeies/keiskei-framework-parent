package top.keiskeiframework.common.util.data;

import com.fasterxml.jackson.databind.util.StdConverter;


/**
 * <p>
 * 价格反序列化工具 元转换分
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/8/5 20:50
 */
public class MoneyDeserializer extends StdConverter<Double, Long> {


    @Override
    public Long convert(Double val) {
        return (long) (val * 100);
    }
}
