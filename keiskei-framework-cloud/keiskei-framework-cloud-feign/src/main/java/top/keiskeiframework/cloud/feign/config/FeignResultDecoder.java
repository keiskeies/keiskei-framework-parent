package top.keiskeiframework.cloud.feign.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import top.keiskeiframework.common.enums.exception.ApiErrorCode;
import top.keiskeiframework.common.exception.BizException;

/**
 * <p>
 * feign自定义结果异常转换
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/5/2 15:21
 */
@Slf4j
public class FeignResultDecoder implements ErrorDecoder {

    public final static Default DEFAULT = new ErrorDecoder.Default();

    @SneakyThrows
    @Override
    public Exception decode(String s, Response response) throws FeignException {
        if (response.status() == HttpStatus.SC_OK) {
            if (null != response.body()) {
                String bodyStr = Util.toString(response.body().asReader(Util.UTF_8));
                log.info(bodyStr);
                //对结果进行转换
                JSONObject result = JSON.parseObject(bodyStr);
                log.info(result.toString());
                Long code = result.getLong("code");
                if (ApiErrorCode.SUCCESS.getCode() != code) {
                    throw new BizException(code, result.getString("msg"));
                }
            }
        }
        return DEFAULT.decode(s, response);
    }
}
