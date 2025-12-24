package com.nhnacademy.coupon.global.saga;

import com.nhnacademy.coupon.domain.coupon.exception.CouponUpdateFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 포인트 내역 업데이트 실패 시나리오를 위한 테스트 서비스
 */
@Slf4j
@Service
public class SagaTestService {
    public void process() {
        throw new CouponUpdateFailedException("쿠폰 사용 내역 업데이트 실패!");
    }
}
