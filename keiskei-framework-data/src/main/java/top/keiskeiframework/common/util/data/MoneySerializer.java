package top.keiskeiframework.common.util.data;


import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * <p>
 * 价格序列化工具 分转换元
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/8/5 20:45
 */
public class MoneySerializer extends StdConverter<Long, Double> {

    @Override
    public Double convert(Long val) {
        return val / 100D;
    }
}