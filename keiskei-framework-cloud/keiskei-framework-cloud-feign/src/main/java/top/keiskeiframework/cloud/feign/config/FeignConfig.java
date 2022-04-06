package top.keiskeiframework.cloud.feign.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.cloud.openfeign.support.SortJacksonModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;

/**
 * <p>
 * spring Page xulie
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/5 21:47
 */
@Configuration(
        proxyBeanMethods = false
)
@ConditionalOnClass({Module.class, Page.class, Sort.class})
@EnableFeignClients
public class FeignConfig {
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @PostConstruct
//    public void addObjectMapperModules() {
//        objectMapper.registerModule(new PageJacksonModule());
//        objectMapper.registerModule(new SortJacksonModule());
//    }



    @Bean
    @ConditionalOnMissingBean(PageJacksonModule.class)
    public PageJacksonModule pageJacksonModule() {
        return new PageJacksonModule();
    }

    @Bean
    @ConditionalOnMissingBean(SortJacksonModule.class)
    public SortJacksonModule sortModule() {
        return new SortJacksonModule();
    }

}