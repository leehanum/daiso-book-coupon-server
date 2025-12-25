package com.nhnacademy.coupon.global.saga;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.coupon.domain.coupon.entity.saga.CouponOutbox;
import com.nhnacademy.coupon.domain.coupon.exception.CouponUpdateFailedException;
import com.nhnacademy.coupon.domain.coupon.exception.FailedSerializationException;
import com.nhnacademy.coupon.domain.coupon.repository.CouponOutboxRepository;
import com.nhnacademy.coupon.global.saga.event.OrderCompensateEvent;
import com.nhnacademy.coupon.global.saga.event.OrderConfirmedEvent;
import com.nhnacademy.coupon.global.saga.event.SagaEvent;
import com.nhnacademy.coupon.global.saga.event.SagaReply;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SagaHandler {

    private final SagaTestService testService;
    private final SagaReplyService replyService;

    @Transactional
    public void handleEvent(OrderConfirmedEvent event) {

        boolean isSuccess = true; // 성공 여부
        String reason = null; // 실패시 사유

        try {
            // TODO 쿠폰 사용 내역 업데이트 로직
            // 서비스 주입받아서 하시면 됨

//            testService.process(); // 일부러 재고 부족 터트리기

            // 로직 중에 재고 부족하면 해당 커스텀 예외 던지시면 됩니다.
            // 더 좋은 방법 있으면 추천 좀

            log.error("[Coupon API] 쿠폰 사용내역 업데이트 성공 - Order : {}", event.getOrderId());

        } catch(CouponUpdateFailedException e) { // 쿠폰 사용내역 업데이트 실패
            log.error("[Coupon API] 쿠폰 사용내역 업데이트 실패 - Order : {}", event.getOrderId());
            // ---> 비즈니스상 실패를 의미함 (쿠폰 유효기간 지났다던가)
            isSuccess = false;
            reason = "UPDATE_FAILED";
            throw e;
        } catch(Exception e) {
            log.error("[Coupon API] 예상치 못한 시스템 에러 발생 - Order : {}", event.getOrderId(), e);
            isSuccess = false;
            reason = "SYSTEM_ERROR";
            // 이렇게 예외 범위를 넓게 해놔야 무슨 에러가 터져도 finally 문이 실행됨
            throw e;
        }
        finally {
            // 성공했든 실패했든 답장은 해야함
            SagaReply reply = new SagaReply(
                    event.getOrderId(),
                    "COUPON",
                    isSuccess,
                    reason
            );

            // 응답 메시지 전송
            replyService.send(event, reply, SagaTopic.REPLY_RK);
        }
    }

    @Transactional
    public void handleRollbackEvent(OrderCompensateEvent event) {

        boolean isSuccess = true; // 성공 여부
        String reason = null; // 실패시 사유

        try {
            // TODO 재고 '보상' 로직
            // 서비스 주입받아서 하시면 됨

            log.error("[Coupon API] 쿠폰 사용내용 복구 성공 - Order : {}", event.getOrderId());

        } catch(Exception e) {
            log.error("[Coupon API] 예상치 못한 시스템 에러 발생 - Order : {}", event.getOrderId(), e);
            isSuccess = false;
            reason = "SYSTEM_ERROR";
        }
        finally {
            // 성공했든 실패했든 답장은 해야함
            SagaReply reply = new SagaReply(
                    event.getOrderId(),
                    "COUPON",
                    isSuccess,
                    reason
            );

            // 응답 메시지 전송
            replyService.send(event, reply, SagaTopic.REPLY_COMPENSATION_RK);
        }
    }
}
