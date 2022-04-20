package top.keiskeiframework.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import top.keiskeiframework.common.util.MdcUtils;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/12 17:45
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "p", String.class, MdcUtils.getUserDepartment());
        this.strictInsertFill(metaObject, "d", Integer.class, 0);
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "createUserId", Long.class, MdcUtils.getLongUserIdNoException());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateUserId", Long.class, MdcUtils.getLongUserIdNoException());
    }
}

