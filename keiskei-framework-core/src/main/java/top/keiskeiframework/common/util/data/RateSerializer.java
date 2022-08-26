package top.keiskeiframework.common.util.data;


import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * <p>
 * 比例序列化工具 证书转百分比
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/8/5 20:45
 */
public class RateSerializer extends StdConverter<Integer, Double> {

    @Override
    public Double convert(Integer val) {
        return val / 1000D;
    }
}
