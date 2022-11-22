package top.keiskeiframework.common.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>
 * Java8通过Function函数获取字段名称(获取实体类的字段名称)
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/11/20 01:40
 */
public class ColumnFunctionUtils {


    static String defaultSplit = "_";
    static Integer defaultToType = 0;

    public static <T> String getFiledColumnName(SFunction<T, ?> fn) {
        return getFiledColumnName(fn, defaultSplit);
    }

    public static <T> String getFiledColumnName(SFunction<T, ?> fn, String split) {
        return getFiledColumnName(fn, split, defaultToType);
    }

    public static <T> String getFiledColumnName(SFunction<T, ?> fn, String split, Integer toType) {
        SerializedLambda serializedLambda = getSerializedLambda(fn);

        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        Field field;
        try {
            field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
            // 从field取出字段名，可以根据实际情况调整
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && tableField.value().length() > 0) {
                return tableField.value();
            }
        } catch (ClassNotFoundException | NoSuchFieldException ignored) {
        }

        //0.小写 1.大写 2.不做转换
        switch (toType) {
            case 1:
                return fieldName.replaceAll("[A-Z]", split + "$0");
            case 2:
                return fieldName.replaceAll("[A-Z]", split + "$0").toUpperCase();
            default:
                return fieldName.replaceAll("[A-Z]", split + "$0").toLowerCase();
        }

    }


    /**
     * 获取实体类的字段名称(实体声明的字段名称)
     */
    public static <T> String getFieldName(SFunction<T, ?> fn) {
        return getFieldName(fn, defaultSplit);
    }

    /**
     * 获取实体类的字段名称
     *
     * @param split 分隔符，多个字母自定义分隔符
     */
    public static <T> String getFieldName(SFunction<T, ?> fn, String split) {
        return getFieldName(fn, split, defaultToType);
    }

    /**
     * 获取实体类的字段名称
     *
     * @param split  分隔符，多个字母自定义分隔符
     * @param toType 转换方式，多个字母以大小写方式返回 0.不做转换 1.大写 2.小写
     */
    public static <T> String getFieldName(SFunction<T, ?> fn, String split, Integer toType) {
        SerializedLambda serializedLambda = getSerializedLambda(fn);

        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        return fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());

    }

    private static <T> SerializedLambda getSerializedLambda(SFunction<T, ?> fn) {
        // 从function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 从序列化方法取出序列化的lambda信息
        boolean isAccessible = writeReplaceMethod.isAccessible();
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);
        return serializedLambda;
    }
}
