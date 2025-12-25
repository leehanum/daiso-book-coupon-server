package com.nhnacademy.coupon.global.config;

import com.nhnacademy.coupon.global.saga.SagaTopic;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SagaConfig {

    // --- orchestration ---
    @Bean
    public TopicExchange sagaExchange() {
        return new TopicExchange(SagaTopic.ORDER_EXCHANGE);
    }

    @Bean
    public Queue couponQueue() {
        return new Queue(SagaTopic.COUPON_QUEUE);
    }

    @Bean
    public Queue couponRollbackQueue() {
        return new Queue(SagaTopic.COUPON_COMPENSATION_QUEUE);
    }

    @Bean
    public Binding couponBinding(Queue couponQueue, TopicExchange sagaExchange) {
        return BindingBuilder.bind(couponQueue)
                .to(sagaExchange)
                .with(SagaTopic.COUPON_RK);
    }

    @Bean
    public Binding bookRollbackBinding(Queue couponRollbackQueue, TopicExchange sagaExchange) {
        return BindingBuilder.bind(couponRollbackQueue)
                .to(sagaExchange)
                .with(SagaTopic.COUPON_COMPENSATION_RK);
    }


    // =====================

    @Value("${rabbitmq.queue.coupon}")
    private String COUPON_QUEUE;

    @Value("${rabbitmq.queue.compensate}")
    private String COUPON_COMPENSATE_QUEUE;

    @Value("${rabbitmq.routing.deducted}")
    private String ROUTING_KEY_DEDUCTED;

    @Value("${rabbitmq.routing.deducted}")
    private String ROUTING_KEY_COMPENSATE;

    private static final String USER_EXCHANGE = "team3.saga.user.exchange";
    private static final String COUPON_EXCHANGE = "team3.saga.coupon.exchange";
    private static final String PAYMENT_EXCHANGE = "team3.saga.payment.exchange";

    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange(USER_EXCHANGE);
    }

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue couponQueue1() {
        return QueueBuilder.durable(COUPON_QUEUE)
                .withArgument("x-dead-letter-exchange", "team3.coupon.dlx") // DLQ 설정
                .withArgument("x-dead-letter-routing-key", "fail.coupon")
                .build();
    }

    @Bean
    public Binding bindingUserDeducted(Queue couponQueue, DirectExchange userExchange) {
        return BindingBuilder.bind(couponQueue)
                .to(userExchange)
                .with(ROUTING_KEY_DEDUCTED);
    }

    @Bean
    public DirectExchange couponExchange() {
        return new DirectExchange(COUPON_EXCHANGE);
    }



    // ----- 보상 트랜잭션 용 Queue, Binding -----
    @Bean
    public Queue couponCompensateQueue() {
        return QueueBuilder.durable(COUPON_COMPENSATE_QUEUE)
                // 실패 전용 DLQ 설정은 여기다 추가로 하면 됨 <<<<<<<<<<<
                .build();
    }

    @Bean
    public Binding bindingCouponCompensated(Queue couponCompensateQueue, DirectExchange paymentExchange) {
        return BindingBuilder.bind(couponCompensateQueue)
                .to(paymentExchange) // 보상은 역방향이므로!
                .with(ROUTING_KEY_COMPENSATE);
    }


    // ----- saga용 config -----

    @Bean(name = "outboxRabbitTemplate")
    public RabbitTemplate outboxRabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

}
