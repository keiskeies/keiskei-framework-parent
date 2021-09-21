package top.keiskeiframework.common.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import top.keiskeiframework.common.base.entity.BaseEntity;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * bean操作
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年10月15日 下午12:59:14
 */
public class BeanUtils {


    /**
     * 对象属性赋值,忽略空值
     *
     * @param src    源文件
     * @param target 目标文件
     * @author James Chen right_way@foxmail.com
     * @since 2017年7月5日 下午6:01:37
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }
    /**
     * 对象属性赋值,忽略带有@JsonIgnore注解的字段
     *
     * @param src    源文件
     * @param target 目标文件
     * @author James Chen right_way@foxmail.com
     * @since 2017年7月5日 下午6:01:37
     */
    public static void copyPropertiesIgnoreJson(Object src, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(src, target, getIgnorePropertyNames(src));
    }

    private static String[] getNullPropertyNames(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
    private static String[] getIgnorePropertyNames(Object source) {
        Class<?> clazz = source.getClass();
        Set<String> emptyNames = new HashSet<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
            if (null != jsonIgnore) {
                emptyNames.add(field.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void main(String[] args) {
        BaseEntity baseEntity = new BaseEntity();
        BaseEntity baseEntity1 = new BaseEntity();
        BeanUtils.copyPropertiesIgnoreJson(baseEntity, baseEntity1);
    }

    /**
     * 将Map转换为Bean
     *
     * @param <T>   bean 类型
     * @param clazz bean class
     * @param map   map
     * @author James Chen right_way@foxmail.com
     * @since 2017年11月30日
     * @return 实体类
     */
    public static <T> T mapToBean(Map<String, String> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(map), clazz);
    }

    /**
     * 大写字母通配符
     */
    private final static Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");

    /**
     * Java驼峰字段转数据库下划线字段
     * userName 转换成 user_name
     *
     * @param hump java字段
     * @return 数据库下划线字段
     */
    public static String humpToUnderline(String hump) {
        Matcher matcher = HUMP_PATTERN.matcher(hump);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
