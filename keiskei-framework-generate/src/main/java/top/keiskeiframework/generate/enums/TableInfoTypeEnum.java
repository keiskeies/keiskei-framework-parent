package top.keiskeiframework.generate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.keiskeiframework.common.base.entity.ListEntity;

import java.lang.reflect.Field;

/**
 * <p>
 * 表类型
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/22 19:43
 */
@Getter
@AllArgsConstructor
public enum TableInfoTypeEnum {

    //
    BASE("基础类型"),
    TREE("层级类型"),
    ;
    private final String msg;


    public Field[] getDefaultColumns() {
        Class<?> clazz = null;
        if (this.equals(TREE)) {
            clazz = ListEntity.class;
        } else {
            clazz = ListEntity.class;
        }
        return clazz.getDeclaredFields();
    }
}
