package top.keiskeiframework.cloud.feign.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.cloud.openfeign.support.SortJacksonModule;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p>
 * spring Page 序列化问题配置
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/5 21:47
 */
@Configuration
@EnableFeignClients
public class FeignConfig {
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void addObjectMapperModules() {
        objectMapper.registerModule(new PageJacksonModule());
        objectMapper.registerModule(new SortJacksonModule());
    }

}
