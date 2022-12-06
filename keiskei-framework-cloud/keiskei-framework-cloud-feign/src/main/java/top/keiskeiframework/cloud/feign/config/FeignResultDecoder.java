package top.keiskeiframework.cloud.feign.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import top.keiskeiframework.common.enums.exception.ApiErrorCode;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.vo.R;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * <p>
 * feign自定义结果异常转换
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/5/2 15:21
 */
@Slf4j
public class FeignResultDecoder implements Decoder {

    private final static ObjectMapper objectMapper;
    static {
        objectMapper= new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    }

    @Override
    public Object decode(Response response, Type type) throws FeignException {
        if (response.status() == HttpStatus.SC_OK) {
            if (null != response.body()) {
                String bodyStr;
                try {
                    bodyStr = Util.toString(response.body().asReader(Util.UTF_8));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                log.info(bodyStr);

                JavaType javaType = TypeFactory.defaultInstance().constructType(type);
                R<?> r;
                try {
                    r = objectMapper.readValue(bodyStr, javaType);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                if (ApiErrorCode.SUCCESS.getCode() != r.getCode()) {
                    throw new BizException(r.getCode(), r.getMsg());
                }
                return r;
            }
        }
        return null;
    }
}
