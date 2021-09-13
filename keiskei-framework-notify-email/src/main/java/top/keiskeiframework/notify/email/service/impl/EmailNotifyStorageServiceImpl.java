package top.keiskeiframework.notify.email.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.keiskeiframework.notify.dto.NotifyMessageDTO;
import top.keiskeiframework.notify.service.NotifyStorageService;

import java.util.Date;
import java.util.Objects;

import static top.keiskeiframework.notify.dto.NotifyMessageDTO.SPLIT;

/**
 * <p>
 * 邮件消息推送
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/13 23:22
 */
@Service
public class EmailNotifyStorageServiceImpl implements NotifyStorageService {

    @Autowired
    private JavaMailSenderImpl javaMailSender;
    @Autowired
    private MailProperties mailProperties;

    @Override
    @Async
    public void send(NotifyMessageDTO mailVo) {
        try {
            //true表示支持复杂类型
            MimeMessageHelper messageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
            messageHelper.setFrom(mailProperties.getUsername());
            messageHelper.setTo(mailVo.getTo().split(SPLIT));
            messageHelper.setSubject(mailVo.getSubject());
            messageHelper.setText(mailVo.getText());
            if (!StringUtils.isEmpty(mailVo.getCc())) {
                messageHelper.setCc(mailVo.getCc().split(SPLIT));
            }
            if (!StringUtils.isEmpty(mailVo.getBcc())) {
                messageHelper.setCc(mailVo.getBcc().split(SPLIT));
            }
            if (mailVo.getMultipartFiles() != null) {
                for (MultipartFile multipartFile : mailVo.getMultipartFiles()) {
                    messageHelper.addAttachment(Objects.requireNonNull(multipartFile.getOriginalFilename()), multipartFile);
                }
            }
            if (StringUtils.isEmpty(mailVo.getSentDate())) {
                mailVo.setSentDate(new Date());
                messageHelper.setSentDate(mailVo.getSentDate());
            }
            javaMailSender.send(messageHelper.getMimeMessage());
            mailVo.setStatus("ok");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
