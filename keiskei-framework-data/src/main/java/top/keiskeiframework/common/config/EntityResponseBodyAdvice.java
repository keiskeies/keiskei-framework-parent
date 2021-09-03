package top.keiskeiframework.common.config;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import org.bson.types.ObjectId;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.keiskeiframework.common.util.data.LocalDateSerializer;
import top.keiskeiframework.common.util.data.LocalDateTimeSerializer;
import top.keiskeiframework.common.util.data.LocalTimeSerializer;
import top.keiskeiframework.common.util.data.ObjectIdSerializer;
import top.keiskeiframework.common.vo.R;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/8 17:36
 */
@ControllerAdvice
public class EntityResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        String a = R.class.getName();
        Type type = returnType.getGenericParameterType();
        String b = type.getTypeName();
        return b.startsWith(a);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {

        SerializeConfig config = new SerializeConfig();
        config.put(ObjectId.class, new ObjectIdSerializer());
        config.put(LocalDateTime.class, new LocalDateTimeSerializer());
        config.put(LocalDate.class, new LocalDateSerializer());
        config.put(LocalTime.class, new LocalTimeSerializer());
        return JSONObject.parse(JSON.toJSONString(body, config));
    }
}
