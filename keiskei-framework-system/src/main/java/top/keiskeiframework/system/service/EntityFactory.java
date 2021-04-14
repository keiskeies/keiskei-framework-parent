package top.keiskeiframework.system.service;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.system.dto.CacheDTO;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/13 19:48
 */
public class EntityFactory {

    @Getter
    private static final List<CacheDTO> BASE_ENTITY_LIST = Lists.newArrayList();

    static {
        //获取该路径下所有类
        Reflections reflections = new Reflections("top.keiskeiframework");
        //获取继承了IAnimal的所有类
        Set<Class<? extends BaseEntity>> classSet = reflections.getSubTypesOf(BaseEntity.class);
        for (Class<? extends BaseEntity> clazz : classSet) {
            System.out.println(clazz.getName());
            ApiModel apiModel = clazz.getDeclaredAnnotation(ApiModel.class);
            if (null != apiModel) {
                BASE_ENTITY_LIST.add(new CacheDTO(clazz.getName(), apiModel.description()));
            } else {
                BASE_ENTITY_LIST.add(new CacheDTO(clazz.getName(), clazz.getName()));
            }
        }
    }

}
