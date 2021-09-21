package top.keiskeiframework.email.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import top.keiskeiframework.cache.service.CacheStorageService;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.ExcelUtils;
import top.keiskeiframework.email.entity.PushRecord;
import top.keiskeiframework.email.entity.PushTask;
import top.keiskeiframework.email.service.IPushRecordService;
import top.keiskeiframework.email.service.IPushTaskService;
import top.keiskeiframework.notify.dto.NotifyMessageDTO;
import top.keiskeiframework.notify.service.NotifyStorageService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 推送任务
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/17 23:23
 */
@Slf4j
@Service
public class PushTaskServiceImpl extends ListServiceImpl<PushTask> implements IPushTaskService {

    @Autowired
    private NotifyStorageService notifyStorageService;
    @Autowired
    private CacheStorageService cacheStorageService;
    @Autowired
    private IPushRecordService pushRecordService;

    private final static String PUSH_STATUS_SUFFIX = "PUSH_STATUS:%S";

    @Override
    public void toUsersImport(String id, MultipartFile file) throws IOException {
        List<Map<String, Object>> excelData = ExcelUtils.readExcel(file.getInputStream(), 0, 0);
        List<String> toUsers = excelData.stream()
                .filter(e -> e.values().stream().findAny().isPresent())
                .map(e -> e.values().stream().findAny().get().toString())
                .collect(Collectors.toList());
        PushTask pushTask = baseService.findById(id);
        pushTask.setToUsers(toUsers);
        baseService.update(pushTask);
    }

    @Override
    @Async
    public void sendStart(String id) {
        PushTask pushTask = baseService.findById(id);
        String emailContent;
        if (PushTask.PushTaskTypeEnum.DYNAMIC.equals(pushTask.getType())) {
            emailContent = pushTask.getTemplate().getContent();
        } else {
            emailContent = "<p>{user}</p>";
        }

        if (CollectionUtils.isEmpty(pushTask.getToUsers())) {
            return;
        }

        NotifyMessageDTO message;
        String statusKey = String.format(PUSH_STATUS_SUFFIX, id);
        cacheStorageService.save(statusKey, PushTask.PushTaskStatusEnum.STARTING.name());
        pushTask.setStatus(PushTask.PushTaskStatusEnum.STARTING);
        baseService.update(pushTask);

        for (String toUser : pushTask.getToUsers()) {
            String status = cacheStorageService.get(statusKey).toString();
            if (PushTask.PushTaskStatusEnum.STOP.name().equals(status)) {
                throw new BizException("已暂停");
            }
            emailContent = emailContent
                    .replaceAll("\\{title}", pushTask.getSubject())
                    .replaceAll("\\{user}", toUser);
            message = NotifyMessageDTO.builder()
                    .subject(pushTask.getSubject())
                    .to(toUser)
                    .text(emailContent)
                    .html(true)
                    .build();
            PushRecord pushRecord = PushRecord.builder()
                    .sentDate(LocalDateTime.now())
                    .toUser(toUser)
                    .subject(pushTask.getSubject())
                    .build();
            try {
                notifyStorageService.send(message);
                pushRecord.setStatus(PushRecord.PushStatusEnum.SUCCESS);
            } catch (Exception e) {
                pushRecord.setStatus(PushRecord.PushStatusEnum.FAIL);
                e.printStackTrace();
            }
            pushRecordService.save(pushRecord);
            log.info("邮件发送：\n{}", JSON.toJSONString(pushRecord));
        }
        pushTask.setStatus(PushTask.PushTaskStatusEnum.FINISH);
        baseService.update(pushTask);
    }

    @Override
    public void sendStop(String id) {
        PushTask pushTask = baseService.findById(id);
        String statusKey = String.format(PUSH_STATUS_SUFFIX, id);
        cacheStorageService.save(statusKey, PushTask.PushTaskStatusEnum.STOP.name());
        pushTask.setStatus(PushTask.PushTaskStatusEnum.STOP);
        baseService.update(pushTask);
    }
}
