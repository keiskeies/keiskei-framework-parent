package top.keiskeiframework.common.base.service;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import org.reflections.Reflections;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.vo.CacheDTO;

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
        Set<Class<? extends ListEntity>> classSet = reflections.getSubTypesOf(ListEntity.class);
        for (Class<? extends ListEntity> clazz : classSet) {
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
