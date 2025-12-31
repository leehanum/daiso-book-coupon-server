package com.nhnacademy.coupon.global.saga;

import com.nhnacademy.coupon.domain.coupon.dto.request.usage.BatchCouponUseRequest;
import com.nhnacademy.coupon.domain.coupon.dto.request.usage.CouponCancelRequest;
import com.nhnacademy.coupon.domain.coupon.dto.request.usage.CouponUseRequest;
import com.nhnacademy.coupon.domain.coupon.exception.CouponUpdateFailedException;
import com.nhnacademy.coupon.domain.coupon.service.UserCouponService;
import com.nhnacademy.coupon.global.saga.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SagaHandler {

    private final SagaTestService testService;
    private final SagaReplyService replyService;
    private final UserCouponService userCouponService;

    @Transactional
    public void onMessage(SagaEvent event) {
        event.accept(this);
    }

    public void handleEvent(OrderConfirmedEvent event) {

        boolean isSuccess = true; // 성공 여부
        String reason = null; // 실패시 사유

        try {
            List<CouponUseRequest> coupons = event.getUsedCouponIds().stream()
                    .map(CouponUseRequest::new)
                    .toList();

            // BatchCouponUseRequest
            BatchCouponUseRequest request = new BatchCouponUseRequest(event.getOrderId(), coupons);

            userCouponService.useCoupons(event.getUserId(), request);

            log.info("[Coupon API] 쿠폰 사용내역 업데이트 성공 - Order : {}", event.getOrderId());

        } catch(CouponUpdateFailedException e) { // 쿠폰 사용내역 업데이트 실패
            // ---> 비즈니스상 실패를 의미함 (쿠폰 유효기간 지났다던가)
            isSuccess = false;
            reason = "UPDATE_FAILED";
            log.error("[Coupon API] 쿠폰 사용내역 업데이트 실패 - Order : {}", event.getOrderId());
            throw e;
        } catch(Exception e) {
            isSuccess = false;
            reason = "SYSTEM_ERROR";
            // 이렇게 예외 범위를 넓게 해놔야 무슨 에러가 터져도 finally 문이 실행됨
            log.error("[Coupon API] 예상치 못한 시스템 에러 발생 - Order : {}", event.getOrderId(), e);
            throw e;
        }
        finally {
            // 성공했든 실패했든 답장은 해야함
            SagaReply reply = new SagaReply(
                    event.getEventId(),
                    event.getOrderId(),
                    "COUPON",
                    isSuccess,
                    reason
            );

            // 응답 메시지 전송
            replyService.send(event, reply, SagaTopic.REPLY_RK);
        }
    }

    // Refund 전용 핸들러
    public void handleEvent(OrderRefundEvent event) {
        boolean isSuccess = true; // 성공 여부
        String reason = null; // 실패시 사유

        try {

            // 별다른 refund 로직이 없음 ^________^

        } catch(CouponUpdateFailedException e) { // 쿠폰 사용내역 업데이트 실패
            // ---> 비즈니스상 실패를 의미함 (쿠폰 유효기간 지났다던가)
            isSuccess = false;
            reason = "UPDATE_FAILED";
            log.error("[Coupon API] 쿠폰 사용내역 업데이트 실패 - Order : {}", event.getOrderId());
            throw e;
        } catch(Exception e) {
            isSuccess = false;
            reason = "SYSTEM_ERROR";
            log.error("[Coupon API] 예상치 못한 시스템 에러 발생 - Order : {}", event.getOrderId(), e);
            throw e;
        }
        finally {
            // 성공했든 실패했든 답장은 해야함
            SagaReply reply = new SagaReply(
                    event.getEventId(),
                    event.getOrderId(),
                    "COUPON",
                    isSuccess,
                    reason
            );
            // 응답 메시지 전송
            replyService.send(event, reply, SagaTopic.REPLY_RK);
        }
        // 이렇게 예외 범위를 넓게 해놔야 무슨 에러가 터져도 finally 문이 실행됨
    }

    public void handleEvent(OrderCompensateEvent event) {

        boolean isSuccess = true; // 성공 여부
        String reason = null; // 실패시 사유

        SagaEvent originalEvent = event.getOriginalEvent();

        try {
            if(originalEvent instanceof OrderConfirmedEvent confirmedEvent) {
                List<Long> usedCouponIds = confirmedEvent.getUsedCouponIds();
                if (usedCouponIds == null || usedCouponIds.isEmpty()) {
                    log.info("[Coupon API] 보상: 쿠폰 미사용 - orderId={}", confirmedEvent.getOrderId());
                    return;
                }
                Long orderId = confirmedEvent.getOrderId();
                String cancelReason = "SAGA_COMPENSATION";

                CouponCancelRequest request = new CouponCancelRequest(orderId, cancelReason, usedCouponIds);
                userCouponService.cancelCouponUsage(confirmedEvent.getUserId(), request);

                log.info("[Coupon API] 쿠폰 사용내용 복구 성공 - Order : {}", event.getOrderId());
            }
            if(originalEvent instanceof OrderRefundEvent refundEvent) {
                // 여기도 별다른 로직이 없음
            }

        } catch(Exception e) {
            isSuccess = false;
            reason = "SYSTEM_ERROR";
            log.error("[Coupon API] 예상치 못한 시스템 에러 발생 - Order : {}", event.getOrderId(), e);
        }
        finally {
            // 성공했든 실패했든 답장은 해야함
            SagaReply reply = new SagaReply(
                    event.getEventId(),
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
