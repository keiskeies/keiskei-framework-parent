package top.keiskeiframework.common.util.json;


import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * 价格序列化工具 分元转换
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/8/5 20:45
 */
public class MoneySerializer extends StdConverter<Integer, Double> {

    @Override
    public Double convert(Integer val) {
        return val / 100D;
    }
}
