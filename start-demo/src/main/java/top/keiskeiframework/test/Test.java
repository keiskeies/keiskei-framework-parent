package top.keiskeiframework.test;

import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import top.keiskeiframework.system.entity.SystemUser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 14:45
 */
public class Test {
    public static void main(String[] args) throws NoSuchMethodException {
        Class<SystemUser> systemUserClass = SystemUser.class;

        Field[] fields = systemUserClass.getDeclaredFields();

        for (Field field : fields) {
            System.out.println(field.getName() + "==================================");
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                System.out.println(annotation.annotationType().getName());

                for (Annotation annotation1 : annotation.annotationType().getAnnotations()) {
                    System.out.println(annotation1.annotationType().getName());
                }
            }
        }

    }
}
