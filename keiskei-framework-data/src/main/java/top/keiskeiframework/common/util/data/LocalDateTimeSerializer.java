package top.keiskeiframework.common.util.data;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 时间序列化
 * LocalDateTime 转 String
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/8 18:09
 */
public class LocalDateTimeSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Object o1, Type type, int i) throws IOException {
        SerializeWriter out = jsonSerializer.getWriter();
        if (o == null) {
            jsonSerializer.getWriter().writeNull();
            return;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        out.write("\"" + ((LocalDateTime) o).format(dtf) + "\"");
    }
}
