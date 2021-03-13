package top.keiskeiframework.common.util.json;

import com.fasterxml.jackson.databind.util.StdConverter;


/**
 * 价格反序列化工具 元分转换
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/8/5 20:50
 */
public class MoneyDeserializer extends StdConverter<Double, Integer> {


    @Override
    public Integer convert(Double val) {
        return (int) (val * 100);
    }


    public static void main(String[] args) {

    }
}
