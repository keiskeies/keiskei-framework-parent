package top.keiskeiframework.cpreading.log.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/25 16:10
 */
@Component
@Slf4j
public class KafkaLogController {
    @KafkaListener(topics = "operator_log", groupId = "operator_log_group_1")
    public void operatorLog(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("record: {}", record);
        // 手动提交offset
        ack.acknowledge();
    }

}
