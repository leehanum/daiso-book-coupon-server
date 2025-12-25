package com.nhnacademy.coupon.global.saga;

import com.nhnacademy.coupon.domain.coupon.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponEventPublisher {

    @Qualifier("outboxRabbitTemplate")
    private final AmqpTemplate rabbitTemplate;


    public void publishCouponOutboxMessage(String topic, String routingKey, String payload) {

        try {
            byte[] body = payload.getBytes(StandardCharsets.UTF_8);

            MessageProperties properties = new MessageProperties();
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON); // 👈 핵심 수정
            properties.setContentEncoding("UTF-8");
            Message message = new Message(body);

            rabbitTemplate.send(topic, routingKey, message); // 직렬화 해서 생으로 보냄

            log.info("[Coupon API] ===== 메세지 발송됨 =====");
            log.info("[Coupon API] Routing Key : {}", routingKey);
        } catch (Exception e) {
            log.warn("[Coupon API] RabbitMQ 발행 실패 : {}", e.getMessage());
            throw new ExternalServiceException("rabbitMQ 메세지 발행 실패");
        }
    }


}
