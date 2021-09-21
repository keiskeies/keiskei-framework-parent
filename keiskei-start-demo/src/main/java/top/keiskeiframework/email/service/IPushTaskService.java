package top.keiskeiframework.email.service;

import org.springframework.web.multipart.MultipartFile;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.email.entity.PushTask;

import java.io.IOException;

/**
 * <p>
 * 推送任务service
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/17 23:22
 */
public interface IPushTaskService extends BaseService<PushTask> {
    /**
     * 导入推送任务接收人
     *
     * @param id   任务ID
     * @param file 表格文件
     * @throws IOException excel读取异常
     */
    void toUsersImport(String id, MultipartFile file) throws IOException;

    /**
     * 发送邮件
     * @param id 任务ID
     */
    void sendStart(String id);

    /**
     * 停止发送邮件
     * @param id 任务ID
     */
    void sendStop(String id);
}
