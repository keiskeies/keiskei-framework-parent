package top.keiskeiframework.common.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import top.keiskeiframework.common.base.injector.FindManyToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/12 18:10
 */
public class MyLogicSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {

        //获取父类中的集合
        List<AbstractMethod> list = new ArrayList<>(super.getMethodList(mapperClass, tableInfo));
        //自定义的方法
        list.add(new FindManyToMany());
        return list;
    }
}
