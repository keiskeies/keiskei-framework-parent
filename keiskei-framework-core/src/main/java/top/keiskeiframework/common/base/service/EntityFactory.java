package top.keiskeiframework.common.base.service;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.reflections.Reflections;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.dto.cache.CacheDTO;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;
import java.util.*;

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


    public static List<CacheDTO> getEntityInfo(String entityClass) {
        List<CacheDTO> entityFields = null;

        if (!StringUtils.isEmpty(entityClass)) {
            try {
                Class<?> clazz = Class.forName(entityClass);
                Field[] fields = clazz.getDeclaredFields();
                entityFields = new ArrayList<>(fields.length);
                for (Field field : fields) {
                    if (DATA_CLASS_SET.contains(field.getType())) {
                        ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                        if (null != apiModelProperty) {
                            entityFields.add(new CacheDTO(field.getName(), apiModelProperty.value()));
                        } else {
                            entityFields.add(new CacheDTO(field.getName(), field.getName()));
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return entityFields;
    }

    public static boolean columnEntityContains(@NotBlank String entityClass, @NotBlank String entityField) {
        try {
            Class<?> clazz = Class.forName(entityClass);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (DATA_CLASS_SET.contains(field.getType())) {
                    if (field.getName().equals(entityField)) {
                        return true;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private final static List<Class<?>> DATA_CLASS_SET = Arrays.asList(
            java.lang.Integer.class,
            java.lang.Long.class,
            java.lang.Double.class,
            java.lang.String.class,
            java.lang.Float.class
    );

}
