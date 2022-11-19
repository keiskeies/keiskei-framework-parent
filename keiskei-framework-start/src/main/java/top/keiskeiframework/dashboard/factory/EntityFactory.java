package top.keiskeiframework.dashboard.factory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.reflections.Reflections;
import org.springframework.lang.NonNull;
import top.keiskeiframework.common.annotation.dashboard.Chartable;
import top.keiskeiframework.common.base.entity.IListEntity;
import top.keiskeiframework.common.dto.cache.CacheDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 实体类工厂
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/13 19:48
 */
public class EntityFactory {

    @Getter
    private static final List<CacheDTO> BASE_ENTITY_LIST = new ArrayList<>();

    static {

        //获取该路径下所有类
        Reflections reflections = new Reflections("top.keiskeiframework");
        //获取继承了IAnimal的所有类
        Set<Class<? extends IListEntity>> classSet = reflections.getSubTypesOf(IListEntity.class);
        for (Class<? extends IListEntity> clazz : classSet) {
            System.out.println(clazz.getName());
            Chartable chartable = clazz.getDeclaredAnnotation(Chartable.class);
            if (null == chartable) {
                continue;
            }

            ApiModel apiModel = clazz.getDeclaredAnnotation(ApiModel.class);
            if (null != apiModel) {
                BASE_ENTITY_LIST.add(new CacheDTO(clazz.getName(), apiModel.description()));
            } else {
                BASE_ENTITY_LIST.add(new CacheDTO(clazz.getName(), clazz.getName()));
            }

        }
    }

    private final static CacheDTO CREATE_TIME = new CacheDTO("createTime", "创建时间");
    private final static CacheDTO UPDATE_TIME = new CacheDTO("updateTime", "更新时间");

    public static List<CacheDTO> getEntityInfo(@NonNull String entityClass) {
        List<CacheDTO> entityFields;

        try {
            Class<?> clazz = Class.forName(entityClass);
            Field[] fields = clazz.getDeclaredFields();
            entityFields = new ArrayList<>(fields.length);
            for (Field field : fields) {
                Chartable chartable = field.getAnnotation(Chartable.class);
                if (null != chartable && DATA_CLASS_SET.contains(field.getType())) {
                    ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                    if (null != apiModelProperty) {
                        entityFields.add(new CacheDTO(field.getName(), apiModelProperty.value()));
                    } else {
                        entityFields.add(new CacheDTO(field.getName(), field.getName()));
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            entityFields = new ArrayList<>();
            e.printStackTrace();
        }
        entityFields.add(CREATE_TIME);
        entityFields.add(UPDATE_TIME);
        return entityFields;
    }

    public static boolean columnEntityContains(@NotBlank String entityClass, @NotBlank String entityField) {
        try {
            Class<?> clazz = Class.forName(entityClass);
            if (columnEntityContains(clazz, entityField)) {
                return true;
            } else {
                clazz = clazz.getSuperclass();
                return columnEntityContains(clazz, entityField);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean columnEntityContains(@NotNull Class<?> clazz, @NotBlank String entityField) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (DATA_CLASS_SET.contains(field.getType())) {
                if (field.getName().equals(entityField)) {
                    return true;
                }
            }
        }
        return false;
    }

    private final static List<Class<?>> DATA_CLASS_SET = Arrays.asList(
            Integer.class,
            Integer.class,
            Double.class,
            String.class,
            Float.class,
            LocalDateTime.class
    );


}
